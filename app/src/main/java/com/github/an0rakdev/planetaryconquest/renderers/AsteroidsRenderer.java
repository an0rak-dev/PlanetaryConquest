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

/**
 * This renderer manages the second demo of the application which
 * shown how interactions can be made with Cardboard.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
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
    private final SphericalBody mars;
    private final float distanceToTravel;

    private GvrAudioEngine audioEngine;
    private final float[] headQuaternion;
    private final float[] headView;
    private final float[] origin;
    private final float[] sight;

    /**
     * Create a new Renderer and sets all the objects of the world at their beginning position.
     *
     * @param context the current Android context.
     */
    public AsteroidsRenderer(Context context) {
        super(context);

        this.field = new AsteroidField(
                100, 0.2f, 1f,
                -15, -10, 8, 15, 10, 14,
                138, 135, 130);
        this.laserSpeed = 20; // m/s
        this.distanceToTravel = 30; // meters
        this.mars = new SphericalBody(new Coordinates(0,0, this.distanceToTravel + 10), 3);
        this.mars.background(253,153,58);

        this.view = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];

        this.lasers = new ArrayList<>();
        this.currentCooldown = 0L;

        this.headQuaternion = new float[4];
        this.headView = new float[16];
        this.origin = MathUtils.convertPositionToMatrix(new Coordinates());
        this.sight = new float[16];
        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        this.initializeOpenGLPrograms();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        for (Sphere asteroid : this.field.asteroids()) {
            float sightHAngle = MathUtils.horizontalAngleBetween(this.origin, new float[16]);
            float sightVAngle = MathUtils.verticalAngleBetween(this.origin, new float[16]);
            if (shouldFireAt(asteroid, sightHAngle, sightVAngle)) {
                Laser laser = createLaser(asteroid);
                this.lasers.add(laser);
            }
        }

        this.moveWorld();
        this.countNewFrame();
    }

    @Override
    public void onDrawEye(Eye eye) {
        OpenGLUtils.clear();
        float[] perspective = eye.getPerspective(0.1f, 100f);
        this.drawStars(eye.getEyeView(), perspective);
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.getCamera(), 0);

        this.drawCelestialBodies(perspective);
        for (Laser laser : this.lasers) {
            draw(laser, perspective);
        }
    }

    /**
     * Pause the audio tracks played.
     */
    public void pause() {
        this.audioEngine.pause();
    }

    /**
     * Resume the audio tracks played.
     */
    public void resume() {
        this.audioEngine.resume();
    }

    /***********************************************************************************************
     *                                 NOT REVELANT FOR DEMO
     **********************************************************************************************/
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

    private void initializeOpenGLPrograms() {
        this.createStarsProgram();
        String vertexSources = readContentOf(R.raw.mvp_vertex);
        String fragmentSources = readContentOf(R.raw.multicolor_fragment);

        this.program = new OpenGLProgram(OpenGLProgram.DrawType.TRIANGLES);
        this.program.compile(vertexSources, fragmentSources);
        this.program.setAttributesNames("vMatrix", "vVertices", "vColors");

        this.laserProgram = new OpenGLProgram(OpenGLProgram.DrawType.LINES);
        this.laserProgram.compile(vertexSources, fragmentSources);
        this.laserProgram.setAttributesNames("vMatrix", "vVertices", "vColors");
    }

    private void moveWorld() {
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        float laserDistance = (this.laserSpeed / 1000) * time;
        this.currentCooldown -= time;
        for (Laser laser : this.lasers) {
            laser.move(0, 0, laserDistance);
        }

        this.checkCollisions();
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

    private void draw(Laser laser, float[] perspective) {
        float[] laserModel = laser.model();
        this.laserProgram.activate();
        this.laserProgram.setLineWidth(120);
        Matrix.multiplyMM(this.modelView, 0, this.view, 0, laserModel, 0);
        Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.modelView, 0);
        this.laserProgram.useMVP(this.mvp);
        this.laserProgram.draw(laser);
    }

    private void drawCelestialBodies(float[] perspective) {
        this.program.activate();
        Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.view, 0);
        this.program.useMVP(this.mvp);
        this.program.draw(this.mars.getShape());

        for (Sphere asteroid : this.field.asteroids()) {
            Matrix.multiplyMM(this.modelView, 0, this.view, 0, asteroid.model(), 0);
            Matrix.multiplyMM(this.mvp, 0, perspective, 0, this.modelView, 0);
            this.program.useMVP(this.mvp);
            this.program.draw(asteroid);
        }
    }

    private boolean shouldFireAt(Sphere shape, float sightHAngle, float sightVAngle) {
        return this.isHorizontallyLookingAt(shape, sightHAngle)
                && this.isVerticallyLookingAt(shape, sightVAngle)
                && 0L >= this.currentCooldown;
    }

    private Laser createLaser(Sphere direction) {
        final Coordinates start = new Coordinates(0,-0.1f,0);
        Laser laser = new Laser(start, new Coordinates(0,-0.1f,-0.5f));
        laser.color(253,106,2);

        float yawInRad = MathUtils.verticalAngleOf(direction.model());
        float yaw = -(float) Math.toDegrees(yawInRad);
        laser.yaw(yaw);
        float pitchInRad = MathUtils.horizontalAngleOf(direction.model());
        float pitch = (float) Math.toDegrees(pitchInRad);
        laser.pitch(pitch);

        this.currentCooldown = LASER_COOLDOWN;
        return laser;
    }

    private boolean isHorizontallyLookingAt(Sphere shape, float sightHAngle) {
        float x = MathUtils.getX(shape.model());
        float y = MathUtils.getY(shape.model());
        float z = Math.abs(MathUtils.getZ(shape.model()));
        float xtremLeftAngle = MathUtils.horizontalAngleOf(new Coordinates(x + shape.getRadius(), y, z));
        float xtremRightAngle = MathUtils.horizontalAngleOf(new Coordinates(x - shape.getRadius(), y, z));
        return xtremRightAngle <= sightHAngle && sightHAngle <= xtremLeftAngle;
    }

    private boolean isVerticallyLookingAt(Sphere shape, float sightVAngle) {
        float x = MathUtils.getX(shape.model());
        float y = MathUtils.getY(shape.model());
        float z = Math.abs(MathUtils.getZ(shape.model()));
        float realSightAngle = sightVAngle * -1;
        float xtremUpAngle = MathUtils.verticalAngleOf(new Coordinates(x, y + shape.getRadius(), z));
        float xtremDownAngle = MathUtils.verticalAngleOf(new Coordinates(x, y - shape.getRadius(), z));
        return xtremDownAngle <= realSightAngle && realSightAngle <= xtremUpAngle;
    }

    private void handleCollisionWithAsteroid(List<Laser> lasersToRemove, Laser laser) {
        List<Sphere> asteroidsToRemove = new ArrayList<>();
        for (Sphere asteroid : this.field.asteroids()) {
            if (MathUtils.collide(laser, asteroid)) {
                lasersToRemove.add(laser);
                asteroidsToRemove.add(asteroid);
                /*
                this.audioEngine.stopSound(laser.audio());
                int explosionSound = this.audioEngine.createSoundObject(EXPLOSION_SOUNDFILE);
                this.audioEngine.setSoundObjectPosition(explosionSound,
                        MathUtils.getX(asteroid.model()), MathUtils.getY(asteroid.model()), MathUtils.getZ(asteroid.model()));
                this.audioEngine.playSound(explosionSound, false);
                */
                break;
            }
        }
        this.field.removeAll(asteroidsToRemove);
    }
}
