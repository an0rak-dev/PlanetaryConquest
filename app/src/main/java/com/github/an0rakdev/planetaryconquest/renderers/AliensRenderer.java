package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.AlienShip;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;

public class AliensRenderer extends SpaceRenderer {
    private static final String BANG_SOUNDFILE = "bang.wav";
    private Sphere mars;
    private int program;
    private final float[] camera;
    private final float[] view;
    private final float[] mvp;
    private float distanceMade;
    private final Queue<Polyhedron> aliens;
    private final List<Polyhedron> aliensDisplayed;
    private float timeUntilNextAlien;

    private final GvrAudioEngine audioEngine;
    private final float[] headQuaternion;

    public AliensRenderer(Context context) {
        super(context, new AliensProperties(context));
        final AliensProperties config = (AliensProperties) this.getProperties();
        this.mars = new Sphere(
                new Coordinates(config.getMarsXPos(),
                    config.getMarsYPos(),
                    config.getMarsZPos()),
                config.getMarsRadius());
        this.mars.precision(3);
        this.mars.background(OpenGLUtils.toOpenGLColor(
                config.getMarsRed(),
                config.getMarsGreen(),
                config.getMarsBlue()
                ));
        this.camera = new float[16];
        this.view = new float[16];
        this.mvp = new float[16];
        this.distanceMade = 0;
        this.aliens = new LinkedList<>();
        this.aliensDisplayed = new ArrayList<>();
        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        this.headQuaternion = new float[4];
        initializeAliens(config);
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


        new Thread(new Runnable() {
            @Override
            public void run() {
                audioEngine.preloadSoundFile(BANG_SOUNDFILE);
            }
        }).start();
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        final AliensProperties config = (AliensProperties) getProperties();
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();

        if (userHasToMoveAgain()) {
            final float currentDistance = (config.getMovementSpeed() / 1000f) * time;
            this.distanceMade += currentDistance;
        } else if (!this.aliens.isEmpty()) {
            this.timeUntilNextAlien -= time;
            if (0 >= this.timeUntilNextAlien) {
                final Polyhedron ship = this.aliens.poll();
                this.aliensDisplayed.add(ship);
                this.timeUntilNextAlien = config.getTimeBetweenShips();
                final int arrivalSound = this.audioEngine.createStereoSound(BANG_SOUNDFILE);
                this.audioEngine.setSoundObjectPosition(arrivalSound,
                        ship.getPosition().x, ship.getPosition().y, ship.getPosition().z);
                this.audioEngine.playSound(arrivalSound, false);
            }
        }

        float cameraCurrentZ = config.getCameraPositionZ() + this.distanceMade;
        Matrix.setLookAtM(this.camera, 0,
                config.getCameraPositionX(),
                config.getCameraPositionY(),
                cameraCurrentZ,
                config.getCameraDirectionX(),
                config.getCameraDirectionY(),
                config.getCameraDirectionZ() + cameraCurrentZ,
                0, 1, 0);

        headTransform.getQuaternion(this.headQuaternion, 0);
        audioEngine.setHeadRotation(this.headQuaternion[0], this.headQuaternion[1],
                this.headQuaternion[2], this.headQuaternion[3]);
        audioEngine.update();
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
        OpenGLUtils.use(this.program);
        OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");

        final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.program, this.mars.bufferize(), "vVertices");
        final int colorHandle = OpenGLUtils.bindColorToProgram(this.program, this.mars.colors(), "vColors");
        OpenGLUtils.drawTriangles(this.mars.size(), verticesHandle, colorHandle);

        if (!userHasToMoveAgain()) {
            for (final Polyhedron alien : this.aliensDisplayed) {
                final int alienVHandle = OpenGLUtils.bindVerticesToProgram(this.program, alien.bufferize(), "vVertices");
                final int alienCHandle = OpenGLUtils.bindColorToProgram(this.program, alien.colors(), "vColors");
                OpenGLUtils.drawTriangles(alien.size(), alienVHandle, alienCHandle);
            }
        }
    }

    public void pause() {
        this.audioEngine.pause();
    }

    public void resume() {
        this.audioEngine.resume();
    }

    private boolean userHasToMoveAgain() {
        final AliensProperties config = (AliensProperties) getProperties();
        return config.getCameraPositionZ() + this.distanceMade < config.getDistanceToTravel();
    }

    private void initializeAliens(AliensProperties config) {
        while (this.aliens.size() < config.getNumberOfShips()) {
            float z = MathUtils.randRange(config.getDistanceToTravel() + config.getMinZShipPosition(),
                    config.getDistanceToTravel() + config.getMaxZShipPosition());
            float x = MathUtils.randRange(config.getMinXShipPosition(), config.getMaxXShipPosition());
            float y = MathUtils.randRange(config.getMinYShipPosition(), config.getMaxYShipPosition());
            if (Math.abs(x) > 0.7 && Math.abs(y) > 0.7
                && willNotCollideWithOthers(x, y, z)) {
                this.aliens.add(new AlienShip(new Coordinates(x, y, z)));
            }
        }
        this.timeUntilNextAlien = config.getTimeBetweenShips();
    }

    private boolean willNotCollideWithOthers(final float x, final float y, final float z) {
        for (final Polyhedron ship : this.aliens) {
            if (Math.abs(ship.getPosition().z - z) <= 0.4
                && Math.abs(ship.getPosition().y - y) <= 0.4
                && Math.abs(ship.getPosition().x - x) <= 0.4) {
                return false;
            }
        }
        return true;
    }
}
