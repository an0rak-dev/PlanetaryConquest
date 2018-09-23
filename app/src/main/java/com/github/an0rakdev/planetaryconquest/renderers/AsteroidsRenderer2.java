package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.Laser;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

public class AsteroidsRenderer2 extends SpaceRenderer  {
    // MUST BE a single channel track, or the gvrAudioEngine.createSoundObject will return -1.
    private static final String LASER_SOUNDFILE = "laser.wav";
    private static final String EXPLOSION_SOUNDFILE = "explosion.wav";
    private int program;
    private float[] perspective;
    private final float[] view;
    private final float[] modelView;
    private final float[] mvp;
    private final float[] camera;
    private Sphere asteroid;
    private final float laserSpeed;
    private final List<Laser> lasers;
    private long currentCooldown;

    private final GvrAudioEngine audioEngine;
    private final float[] headQuaternion;
    private final float[] headView;

    public AsteroidsRenderer2(Context context) {
        super(context, new AsteroidsProperties(context));
        final AsteroidsProperties config = (AsteroidsProperties) getProperties();
        this.view = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];
        this.camera = new float[16];
        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        this.headQuaternion = new float[4];
        this.headView = new float[16];
        this.laserSpeed = config.getLaserSpeed() / 1000;
        this.lasers = new ArrayList<>();
        this.currentCooldown = 0L;
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        super.onSurfaceCreated(config);

        this.program = OpenGLUtils.newProgram();
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.program);
        final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.program);
        OpenGLUtils.linkProgram(this.program, vertexShader, fragmentShader);

        this.asteroid = new Sphere(new Coordinates(4,6,8), 1);
        this.asteroid.precision(1);
        this.asteroid.background(OpenGLUtils.toOpenGLColor(190, 190, 190));

        new Thread(new Runnable() {
            @Override
            public void run() {
                audioEngine.preloadSoundFile(LASER_SOUNDFILE);
                audioEngine.preloadSoundFile(EXPLOSION_SOUNDFILE);
            }
        }).start();
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final float laserDistance = this.laserSpeed * time;
        final AsteroidsProperties config = (AsteroidsProperties) getProperties();
        final float cameraPosX = config.getCameraPositionX();
        final float cameraPosY = config.getCameraPositionY();
        final float cameraPosZ = config.getCameraPositionZ();
        final float cameraDirX = config.getCameraDirectionX();
        final float cameraDirY = config.getCameraDirectionY();
        final float cameraDirZ = config.getCameraDirectionZ();

        Matrix.setLookAtM(this.camera, 0,
                cameraPosX, cameraPosY, cameraPosZ,
                cameraPosX + cameraDirX, cameraPosY + cameraDirY, cameraPosZ + cameraDirZ,
                0, 1, 0);
        headTransform.getHeadView(this.headView, 0);

        this.currentCooldown -= time;

        final List<Laser> toRemove = new ArrayList<>();
        for (final Laser laser : this.lasers) {
            laser.move(0, 0, laserDistance);
            if (laser.model()[14] > (config.getDistanceToTravel() + 1)) {
                toRemove.add(laser);
                this.audioEngine.stopSound(laser.audio());
            } else if (collide(laser, this.asteroid)) {
                toRemove.add(laser);
                this.audioEngine.stopSound(laser.audio());
                final int explosionSound = this.audioEngine.createSoundObject(EXPLOSION_SOUNDFILE);
                this.audioEngine.setSoundObjectPosition(explosionSound,
                        this.asteroid.getPosition().x, this.asteroid.getPosition().y, this.asteroid.getPosition().z);
                this.audioEngine.playSound(explosionSound, false);
                this.asteroid = null;
            }
        }
        this.lasers.removeAll(toRemove);

        headTransform.getQuaternion(this.headQuaternion, 0);
        audioEngine.setHeadRotation(this.headQuaternion[0], this.headQuaternion[1],
                this.headQuaternion[2], this.headQuaternion[3]);
        audioEngine.update();
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        this.perspective = eye.getPerspective(0.1f, 100f);

        OpenGLUtils.use(this.program);

        if (isLookingAt(this.asteroid) && 0L >= this.currentCooldown) {
            fireAt(this.asteroid);
        }

        // Draw asteroid
        if (null != this.asteroid) {
            final float[] asteroidModel = this.convertPositionToMatrix(this.asteroid.getPosition());
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, asteroidModel, 0);
            Matrix.multiplyMM(this.mvp, 0, this.perspective, 0, this.modelView, 0);
            OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");
            final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.program, this.asteroid.bufferize(), "vVertices");
            final int colorsHandle = OpenGLUtils.bindColorToProgram(this.program, this.asteroid.colors(), "vColors");
            OpenGLUtils.drawTriangles(this.asteroid.size(), verticesHandle, colorsHandle);
        }

        for (final Laser laser : this.lasers) {
            float[] laserModel = laser.model();
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, laserModel, 0);
            Matrix.multiplyMM(this.mvp, 0, this.perspective, 0, this.modelView, 0);
            OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");
            int laserVHandle = OpenGLUtils.bindVerticesToProgram(this.program, laser.bufferize(), "vVertices");
            int laserCHandle = OpenGLUtils.bindColorToProgram(this.program, laser.colors(), "vColors");
            OpenGLUtils.drawLines(laser.size(), 120, laserVHandle, laserCHandle);
            this.audioEngine.setSoundObjectPosition(laser.audio(),
                    laserModel[12], laserModel[13], laserModel[14]);
        }
    }

    public void pause() {
        this.audioEngine.pause();
    }

    public void resume() {
        this.audioEngine.resume();
    }

    private boolean isLookingAt(final Sphere shape) {
       return this.isHorizontallyLookingAt(shape) && this.isVerticallyLookingAt(shape);
    }

    private void fireAt(final Sphere shape) {
        final Coordinates start = new Coordinates(0,-0.1f,0);
        Laser laser = new Laser(start, new Coordinates(0,-0.1f,-0.5f));
        laser.color(253,106,2);

        final int laserAudioId = this.audioEngine.createSoundObject(LASER_SOUNDFILE);
        laser.audio(laserAudioId);
        this.audioEngine.setSoundObjectPosition(laserAudioId, start.x, start.y, start.z);
        this.audioEngine.playSound(laserAudioId, true);

        final float yawInRad = this.coordYaw(shape.getPosition());
        float yaw = -(float) Math.toDegrees(yawInRad);
        laser.yaw(yaw);
        final float pitchInRad = this.coordPitch(shape.getPosition());
        float pitch = (float) Math.toDegrees(pitchInRad);
        laser.pitch(pitch);

        this.lasers.add(laser);
        this.currentCooldown = ((AsteroidsProperties)getProperties()).getLaserCoolDown();
    }

    private boolean isHorizontallyLookingAt(final Sphere shape) {
        final float sightAngle = this.sightYaw(shape.getPosition().z);
        final float xtremLeftAngle = this.coordPitch(new Coordinates(
                shape.getPosition().x + shape.getRadius(), shape.getPosition().y, shape.getPosition().z
        ));
        final float xtremRightAngle = this.coordPitch(new Coordinates(
                shape.getPosition().x - shape.getRadius(), shape.getPosition().y, shape.getPosition().z
        ));
        return xtremRightAngle <= sightAngle && sightAngle <= xtremLeftAngle;
    }

    private boolean isVerticallyLookingAt(final Sphere shape) {
        final float sightAngle = this.sightPitch(shape.getPosition().z) * -1;
        float xtremUpAngle = this.coordYaw(new Coordinates(
                shape.getPosition().x, shape.getPosition().y + shape.getRadius(), shape.getPosition().z
        ));
        float xtremDownAngle = this.coordYaw(new Coordinates(
                shape.getPosition().x, shape.getPosition().y - shape.getRadius(), shape.getPosition().z
        ));
        return xtremDownAngle <= sightAngle && sightAngle <= xtremUpAngle;
    }

    private float sightYaw(float z) {
        float[] sightModel = this.convertPositionToMatrix(new Coordinates(0, 0, z));
        // z == 0 to increase the deltaZ when calculating the yawBetween.
        float[] sightUntouchedModel = this.convertPositionToMatrix(new Coordinates(0, 0, 0));
        float[] sightReal = new float[16];
        Matrix.multiplyMM(sightReal, 0, this.headView, 0, sightModel, 0);
        return this.pitchBetween(sightUntouchedModel, sightReal);
    }

    private float sightPitch(float z) {
        float[] sightModel = this.convertPositionToMatrix(new Coordinates(0, 0, z));
        // z == 0 to increase the deltaZ when calculating the pitchBetween.
        float[] sightUntouchedModel = this.convertPositionToMatrix(new Coordinates(0, 0, 0));
        float[] sightReal = new float[16];
        Matrix.multiplyMM(sightReal, 0, this.headView, 0, sightModel, 0);
        return this.yawBetween(sightUntouchedModel, sightReal);
    }

    private float coordPitch(Coordinates coord) {
        float[] coordModel = this.convertPositionToMatrix(coord);
        return (float) Math.atan2(coordModel[12], coordModel[14]);
    }

    private float coordYaw(Coordinates coord) {
        float[] coordModel = this.convertPositionToMatrix(coord);
        return (float) Math.atan2(coordModel[13], coordModel[14]);
    }

    private float pitchBetween(final float[] v1, final float[] v2) {
        final float deltaX = v1[12] - v2[12];
        final float deltaZ = v1[14] - v2[14];
        final float deltaY = v1[13] - v2[13];
        final float distance = (float)Math.sqrt(deltaY * deltaY + deltaZ * deltaZ);

        return (float) Math.atan2(deltaX, distance);
    }

    private float yawBetween(float[] v1, float[] v2) {
        float deltaY = v1[13] - v2[13];
        float deltaZ = v1[14] - v2[14];
        float deltaX = v1[12] - v2[12];
        float distance = (float)Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        return (float) Math.atan2(deltaY, distance);
    }

    private float[] convertPositionToMatrix(final Coordinates coord) {
        final float[] model = new float[16];
        Matrix.setIdentityM(model, 0);
        Matrix.translateM(model, 0, coord.x, coord.y, coord.z);
        return model;
    }

    private boolean collide(Laser laser, Sphere shape) {
        final float[] laserModel = laser.model();
        final float[] shapeModel = this.convertPositionToMatrix(shape.getPosition());
        final float left = shapeModel[12] - shape.getRadius();
        final float right = shapeModel[12] + shape.getRadius();
        final float up = shapeModel[13] + shape.getRadius();
        final float down = shapeModel[13] - shape.getRadius();
        final float forward = shapeModel[14] - shape.getRadius();

        return left <= laserModel[12] && laserModel[12] <= right
                && down <= laserModel[13] && laserModel[13] <= up
                && forward <= laserModel[14];
    }
}
