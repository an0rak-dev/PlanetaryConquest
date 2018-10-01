package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.AsteroidField;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.Laser;
import com.github.an0rakdev.planetaryconquest.graphics.models.SphericalBody;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

public class AsteroidsRenderer extends SpaceRenderer implements GvrView.StereoRenderer {
    // MUST BE a single channel track, or the gvrAudioEngine.createSoundObject will return -1.
    private static final String LASER_SOUNDFILE = "laser.wav";
    private static final String EXPLOSION_SOUNDFILE = "explosion.wav";
    private static final long LASER_COOLDOWN = 200; // ms
    private int program;
    private final float[] view;
    private final float[] modelView;
    private final float[] mvp;
    private AsteroidField field;
    private final float laserSpeed;
    private final List<Laser> lasers;
    private long currentCooldown;
    private float currentMovement;
    private final float movementSpeed;
    private final SphericalBody mars;
    private final float distanceToTravel;

    private final GvrAudioEngine audioEngine;
    private final float[] headQuaternion;
    private final float[] headView;

    public AsteroidsRenderer(Context context) {
        super(context, null);

        this.field = new AsteroidField(
                50, 0.2f, 1f,
                -10, -5, 12,
                10, 5, 16,
                138, 135, 130);
        this.laserSpeed = 20; // m/s
        this.movementSpeed = 5; // m/s
        this.distanceToTravel = 30; // meters

        this.view = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];
        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        this.headQuaternion = new float[4];
        this.headView = new float[16];

        this.lasers = new ArrayList<>();
        this.currentCooldown = 0L;
        this.currentMovement = 0f;

        this.mars = new SphericalBody(new Coordinates(0,0, this.distanceToTravel + 10), 3);
        this.mars.background(253,153,58);
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        // Create the stars program
        this.createStarsProgram();

        this.program = OpenGLUtils.newProgram();
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.program);
        final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.program);
        OpenGLUtils.linkProgram(this.program, vertexShader, fragmentShader);

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
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final float laserDistance = (this.laserSpeed / 1000) * time;
        final float cameraMovement = (this.movementSpeed / 1000) * time;
        headTransform.getHeadView(this.headView, 0);
        headTransform.getQuaternion(this.headQuaternion, 0);

        this.currentCooldown -= time;
        this.mars.moveForward(-cameraMovement);
        for (final Sphere asteroid : this.field.asteroids()) {
            if (this.currentMovement < this.distanceToTravel) {
                asteroid.move(0, 0, -cameraMovement); // https://www.youtube.com/watch?v=1RtMMupdOC4
            }
            if (isLookingAt(asteroid) && 0L >= this.currentCooldown) {
                fireAt(asteroid);
            }
        }
        this.currentMovement += cameraMovement;

        for (final Laser laser : this.lasers) {
            laser.move(0, 0, laserDistance);
        }
        checkCollisions(this.distanceToTravel +1);

        audioEngine.setHeadRotation(this.headQuaternion[0], this.headQuaternion[1],
                this.headQuaternion[2], this.headQuaternion[3]);
        audioEngine.update();
        this.countNewFrame();
    }

    @Override
    public void onDrawEye(final Eye eye) {
        OpenGLUtils.clear();
        this.drawStars(eye);
        float[] perspective = eye.getPerspective(0.1f, 100f);
        OpenGLUtils.use(this.program);
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.getCamera(), 0);

        Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.view, 0);
        OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");
        final int marsVHandle = OpenGLUtils.bindVerticesToProgram(this.program, this.mars.getShape().bufferize(), "vVertices");
        final int marsCHandle = OpenGLUtils.bindColorToProgram(this.program, this.mars.getShape().colors(), "vColors");
        OpenGLUtils.drawTriangles(this.mars.getShape().size(), marsVHandle, marsCHandle);

        for (final Sphere asteroid : this.field.asteroids()) {
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, asteroid.model(), 0);
            Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.modelView, 0);
            OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");
            final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.program, asteroid.bufferize(), "vVertices");
            final int colorsHandle = OpenGLUtils.bindColorToProgram(this.program, asteroid.colors(), "vColors");
            OpenGLUtils.drawTriangles(asteroid.size(), verticesHandle, colorsHandle);
        }

        for (final Laser laser : this.lasers) {
            float[] laserModel = laser.model();
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, laserModel, 0);
            Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.modelView, 0);
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


        final float yawInRad = this.coordYaw(shape.model());
        float yaw = -(float) Math.toDegrees(yawInRad);
        laser.yaw(yaw);
        final float pitchInRad = this.coordPitch(shape.model());
        float pitch = (float) Math.toDegrees(pitchInRad);
        laser.pitch(pitch);

        this.lasers.add(laser);
        this.currentCooldown = LASER_COOLDOWN;
    }

    private boolean isHorizontallyLookingAt(final Sphere shape) {
        float x = shape.model()[12];
        float y = shape.model()[13];
        float z = Math.abs(shape.model()[14]);
        final float sightAngle = this.sightYaw(z);
        final float xtremLeftAngle = this.coordPitch(new Coordinates(x + shape.getRadius(), y, z));
        final float xtremRightAngle = this.coordPitch(new Coordinates(x - shape.getRadius(), y, z));
        return xtremRightAngle <= sightAngle && sightAngle <= xtremLeftAngle;
    }

    private boolean isVerticallyLookingAt(final Sphere shape) {
        float x = shape.model()[12];
        float y = shape.model()[13];
        float z = Math.abs(shape.model()[14]);
        final float sightAngle = this.sightPitch(z) * -1;
        float xtremUpAngle = this.coordYaw(new Coordinates(x, y + shape.getRadius(), z));
        float xtremDownAngle = this.coordYaw(new Coordinates(x, y - shape.getRadius(), z));
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

    private void checkCollisions(final float maxDistance) {
        final List<Laser> lasersToRemove = new ArrayList<>();
        for (final Laser laser : this.lasers) {
            if (laser.model()[14] > maxDistance) {
                this.audioEngine.stopSound(laser.audio());
                lasersToRemove.add(laser);
            } else {
                final List<Sphere> asteroidsToRemove = new ArrayList<>();
                for (final Sphere asteroid : this.field.asteroids()) {
                    if (collide(laser, asteroid)) {
                        lasersToRemove.add(laser);
                        this.audioEngine.stopSound(laser.audio());
                        asteroidsToRemove.add(asteroid);
                        final int explosionSound = this.audioEngine.createSoundObject(EXPLOSION_SOUNDFILE);
                        this.audioEngine.setSoundObjectPosition(explosionSound,
                                asteroid.model()[12], asteroid.model()[13], asteroid.model()[14]);
                        this.audioEngine.playSound(explosionSound, false);
                        break;
                    }
                }
                this.field.removeAll(asteroidsToRemove);
            }
        }
        this.lasers.removeAll(lasersToRemove);
    }

    private boolean collide(Laser laser, Sphere shape) {
        final float[] laserModel = laser.model();
        final float[] shapeModel = shape.model();
        final float left = shapeModel[12] - shape.getRadius();
        final float right = shapeModel[12] + shape.getRadius();
        final float up = shapeModel[13] + shape.getRadius();
        final float down = shapeModel[13] - shape.getRadius();
        final float forward = shapeModel[14] - shape.getRadius();

        return left <= laserModel[12] && laserModel[12] <= right
                && down <= laserModel[13] && laserModel[13] <= up
                && forward <= laserModel[14];
    }

    private float coordPitch(Coordinates coord) {
        return this.coordPitch(this.convertPositionToMatrix(coord));
    }

    private float coordPitch(final float[] coordsModel) {
        return (float) Math.atan2(coordsModel[12], Math.abs(coordsModel[14]));
    }

    private float coordYaw(Coordinates coord) {
        return this.coordYaw(this.convertPositionToMatrix(coord));
    }

    private float coordYaw(final float[] coordsModel) {
        return (float) Math.atan2(coordsModel[13], Math.abs(coordsModel[14]));
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

    @Override
    public void onFinishFrame(final Viewport viewport) {
        // Do nothing.
    }

    @Override
    public void onSurfaceChanged(final int width, final int height) {
        // Do nothing.
    }

    @Override
    public void onRendererShutdown() {
        // Do nothing.
    }

}
