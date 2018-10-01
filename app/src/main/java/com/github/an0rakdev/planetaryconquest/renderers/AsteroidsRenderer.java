package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLProgram;
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
    private OpenGLProgram program;
    private OpenGLProgram laserProgram;
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
        this.mars = new SphericalBody(new Coordinates(0,0, this.distanceToTravel + 10), 3);
        this.mars.background(253,153,58);

        this.view = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];

        this.lasers = new ArrayList<>();
        this.currentCooldown = 0L;
        this.currentMovement = 0f;

        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        this.headQuaternion = new float[4];
        this.headView = new float[16];
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        // Create the stars program
        this.createStarsProgram();
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);

        this.program = new OpenGLProgram(OpenGLProgram.DrawType.TRIANGLES);
        this.program.compile(vertexSources, fragmentSources);
        this.program.setAttributesNames("vMatrix", "vVertices", "vColors");

        this.laserProgram = new OpenGLProgram(OpenGLProgram.DrawType.LINES);
        this.laserProgram.compile(vertexSources, fragmentSources);
        this.laserProgram.setAttributesNames("vMatrix", "vVertices", "vColors");

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
        audioEngine.setHeadRotation(this.headQuaternion[0], this.headQuaternion[1],
                this.headQuaternion[2], this.headQuaternion[3]);
        audioEngine.update();

        this.currentCooldown -= time;
        for (final Sphere asteroid : this.field.asteroids()) {
            if (this.currentMovement < this.distanceToTravel) {
                asteroid.move(0, 0, -cameraMovement); // https://www.youtube.com/watch?v=1RtMMupdOC4
            }
            if (shouldFireAt(asteroid)) {
                fireAt(asteroid);
            }
        }

        this.currentMovement += cameraMovement;
        this.mars.moveForward(-cameraMovement);
        for (final Laser laser : this.lasers) {
            laser.move(0, 0, laserDistance);
        }
        checkCollisions();

        this.countNewFrame();
    }

    @Override
    public void onDrawEye(final Eye eye) {
        OpenGLUtils.clear();
        this.drawStars(eye);

        float[] perspective = eye.getPerspective(0.1f, 100f);
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.getCamera(), 0);

        this.program.activate();
        Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.view, 0);
        this.program.useMVP(this.mvp);
        this.program.draw(this.mars.getShape());

        for (final Sphere asteroid : this.field.asteroids()) {
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, asteroid.model(), 0);
            Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.modelView, 0);
            this.program.useMVP(this.mvp);
            this.program.draw(asteroid);
        }

        this.laserProgram.activate();
        this.laserProgram.setLineWidth(120);
        for (final Laser laser : this.lasers) {
            float[] laserModel = laser.model();
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, laserModel, 0);
            Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.modelView, 0);
            this.laserProgram.useMVP(this.mvp);
            this.laserProgram.draw(laser);
            this.audioEngine.setSoundObjectPosition(laser.audio(),
                    MathUtils.getX(laserModel), MathUtils.getY(laserModel), MathUtils.getZ(laserModel));
        }
    }

    public void pause() {
        this.audioEngine.pause();
    }

    public void resume() {
        this.audioEngine.resume();
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        // Do nothing.
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        // Do nothing.
    }

    @Override
    public void onRendererShutdown() {
        // Do nothing.
    }

    private boolean shouldFireAt(final Sphere shape) {
        return this.isHorizontallyLookingAt(shape)
                && this.isVerticallyLookingAt(shape)
                && 0L >= this.currentCooldown;
    }

    private void fireAt(final Sphere shape) {
        final Coordinates start = new Coordinates(0,-0.1f,0);
        Laser laser = new Laser(start, new Coordinates(0,-0.1f,-0.5f));
        laser.color(253,106,2);

        final int laserAudioId = this.audioEngine.createSoundObject(LASER_SOUNDFILE);
        laser.audio(laserAudioId);
        this.audioEngine.setSoundObjectPosition(laserAudioId, start.x, start.y, start.z);
        this.audioEngine.playSound(laserAudioId, true);

        final float yawInRad = MathUtils.coordYaw(shape.model());
        float yaw = -(float) Math.toDegrees(yawInRad);
        laser.yaw(yaw);
        final float pitchInRad = MathUtils.coordPitch(shape.model());
        float pitch = (float) Math.toDegrees(pitchInRad);
        laser.pitch(pitch);

        this.lasers.add(laser);
        this.currentCooldown = LASER_COOLDOWN;
    }

    private boolean isHorizontallyLookingAt(final Sphere shape) {
        float x = MathUtils.getX(shape.model());
        float y = MathUtils.getY(shape.model());
        float z = Math.abs(MathUtils.getZ(shape.model()));
        float sightAngle = this.sightYaw(z);
        float xtremLeftAngle = MathUtils.coordPitch(new Coordinates(x + shape.getRadius(), y, z));
        float xtremRightAngle = MathUtils.coordPitch(new Coordinates(x - shape.getRadius(), y, z));
        return xtremRightAngle <= sightAngle && sightAngle <= xtremLeftAngle;
    }

    private boolean isVerticallyLookingAt(final Sphere shape) {
        float x = MathUtils.getX(shape.model());
        float y = MathUtils.getY(shape.model());
        float z = Math.abs(MathUtils.getZ(shape.model()));
        float sightAngle = this.sightPitch(z) * -1;
        float xtremUpAngle = MathUtils.coordYaw(new Coordinates(x, y + shape.getRadius(), z));
        float xtremDownAngle = MathUtils.coordYaw(new Coordinates(x, y - shape.getRadius(), z));
        return xtremDownAngle <= sightAngle && sightAngle <= xtremUpAngle;
    }

    private float sightYaw(float z) {
        float[] sightModel = MathUtils.convertPositionToMatrix(new Coordinates(0, 0, z));
        // z == 0 to increase the deltaZ when calculating the yawBetween.
        float[] sightUntouchedModel = MathUtils.convertPositionToMatrix(new Coordinates(0, 0, 0));
        float[] sightReal = new float[16];
        Matrix.multiplyMM(sightReal, 0, this.headView, 0, sightModel, 0);
        return MathUtils.pitchBetween(sightUntouchedModel, sightReal);
    }

    private float sightPitch(float z) {
        float[] sightModel = MathUtils.convertPositionToMatrix(new Coordinates(0, 0, z));
        // z == 0 to increase the deltaZ when calculating the pitchBetween.
        float[] sightUntouchedModel = MathUtils.convertPositionToMatrix(new Coordinates(0, 0, 0));
        float[] sightReal = new float[16];
        Matrix.multiplyMM(sightReal, 0, this.headView, 0, sightModel, 0);
        return MathUtils.yawBetween(sightUntouchedModel, sightReal);
    }

    private void checkCollisions() {
        float maxDistance = this.distanceToTravel + 1;
        List<Laser> lasersToRemove = new ArrayList<>();
        for (Laser laser : this.lasers) {
            if (MathUtils.getZ(laser.model()) > maxDistance) {
                this.audioEngine.stopSound(laser.audio());
                lasersToRemove.add(laser);
            } else {
                handleCollisionWithAsteroid(lasersToRemove, laser);
            }
        }
        this.lasers.removeAll(lasersToRemove);
    }

    private void handleCollisionWithAsteroid(List<Laser> lasersToRemove, Laser laser) {
        final List<Sphere> asteroidsToRemove = new ArrayList<>();
        for (final Sphere asteroid : this.field.asteroids()) {
            if (MathUtils.collide(laser, asteroid)) {
                lasersToRemove.add(laser);
                this.audioEngine.stopSound(laser.audio());
                asteroidsToRemove.add(asteroid);
                final int explosionSound = this.audioEngine.createSoundObject(EXPLOSION_SOUNDFILE);
                this.audioEngine.setSoundObjectPosition(explosionSound,
                        MathUtils.getX(asteroid.model()), MathUtils.getY(asteroid.model()), MathUtils.getZ(asteroid.model()));
                this.audioEngine.playSound(explosionSound, false);
                break;
            }
        }
        this.field.removeAll(asteroidsToRemove);
    }
}
