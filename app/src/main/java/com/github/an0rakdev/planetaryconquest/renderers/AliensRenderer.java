package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.AlienShip;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.Laser;
import com.github.an0rakdev.planetaryconquest.graphics.models.SphericalBody;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;

public class AliensRenderer extends SpaceRenderer implements GvrView.StereoRenderer {
    private static final String BANG_SOUNDFILE = "bang.wav";
    private static final String LASER_SOUNDFILE = "laser.wav";
    private static final int NUMBER_OF_SHIPS = 3;
    private static final long LASER_COOLDOWN = 200L; // ms
    private SphericalBody mars;
    private int program;
    private final float[] camera;
    private final float[] view;
    private final float[] mvp;
    private float distanceMade;
    private final Queue<Polyhedron> aliens;
    private final List<Polyhedron> aliensDisplayed;
    private float timeUntilNextAlien;
    private final Map<Polyhedron, Long> coolDownPerShip;
    private final List<Laser> lasers;
    private final int distanceToTravel;
    private final int timeBetweenShips;
    private final int movementSpeed;
    private final int laserSpeed;

    private final GvrAudioEngine audioEngine;
    private final float[] headQuaternion;

    public AliensRenderer(Context context) {
        super(context);
        this.mars = new SphericalBody(new Coordinates(0, 0, 20), 2);
        this.mars.background(253, 153, 58);
        this.camera = new float[16];
        this.view = new float[16];
        this.mvp = new float[16];
        this.distanceMade = 0;
        this.distanceToTravel = 15;
        this.timeBetweenShips = 100;
        this.aliens = new LinkedList<>();
        this.aliensDisplayed = new ArrayList<>();
        this.coolDownPerShip = new HashMap<>();
        this.movementSpeed = 8; // m/s
        this.laserSpeed = 12; // m/s
        this.lasers = new ArrayList<>();
        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        this.headQuaternion = new float[4];
        initializeAliens();
        Matrix.setLookAtM(this.camera, 0,
                0, 0, 0,
                0, 0, 1,
                0, 1, 0);

    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        // Create the stars program
        this.createStarsProgram();
        this.program = OpenGLUtils.newProgram();
        String vertexSources = readContentOf(R.raw.mvp_vertex);
        String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.program);
        int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.program);
        OpenGLUtils.linkProgram(this.program, vertexShader, fragmentShader);

        new Thread(new Runnable() {
            @Override
            public void run() {
                audioEngine.preloadSoundFile(BANG_SOUNDFILE);
                audioEngine.preloadSoundFile(LASER_SOUNDFILE);
            }
        }).start();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();

        if (this.distanceMade < this.distanceToTravel) {
            float currentDistance = (this.movementSpeed / 1000f) * time;
            this.distanceMade += currentDistance;
            this.mars.moveForward(-currentDistance);
        } else if (!this.aliens.isEmpty()) {
            this.timeUntilNextAlien -= time;
            if (0 >= this.timeUntilNextAlien) {
                Polyhedron ship = this.aliens.poll();
                this.aliensDisplayed.add(ship);
                this.timeUntilNextAlien = this.timeBetweenShips;
                int arrivalSound = this.audioEngine.createStereoSound(BANG_SOUNDFILE);
                this.audioEngine.setSoundObjectPosition(arrivalSound,
                        ship.getPosition().x, ship.getPosition().y, ship.getPosition().z);
                this.audioEngine.playSound(arrivalSound, false);
            }
        }

        for (Polyhedron alien : this.aliensDisplayed) {
            final long currentCoolDown = this.coolDownPerShip.get(alien);
            if (currentCoolDown <= 0L) {
                fireFrom(alien);
                this.coolDownPerShip.put(alien, LASER_COOLDOWN);
            } else {
                this.coolDownPerShip.put(alien, currentCoolDown - time);
            }
        }

        float laserDistance = (this.laserSpeed / 1000) * time;
        for (Laser laser : this.lasers) {
            laser.move(0,0,-laserDistance);
        }

        headTransform.getQuaternion(this.headQuaternion, 0);
        audioEngine.setHeadRotation(this.headQuaternion[0], this.headQuaternion[1],
                this.headQuaternion[2], this.headQuaternion[3]);
        audioEngine.update();
        this.countNewFrame();
    }

    @Override
    public void onDrawEye(Eye eye) {
        OpenGLUtils.clear();
        this.drawStars(eye);

        float[] marsModel = this.mars.model();
        float[] marsModelView = new float[16];
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(marsModelView, 0, this.view, 0, marsModel, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, marsModelView, 0);
        OpenGLUtils.use(this.program);
        OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");

        int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.program, this.mars.getShape().bufferize(), "vVertices");
        int colorHandle = OpenGLUtils.bindColorToProgram(this.program, this.mars.getShape().colors(), "vColors");
        OpenGLUtils.drawTriangles(this.mars.getShape().size(), verticesHandle, colorHandle);

        float[] alienView = new float[16];
        float[] alienMVP = new float[16];
        if (this.distanceMade >= this.distanceToTravel) {
            for (Polyhedron alien : this.aliensDisplayed) {
                float[] alienModel = alien.model();
                Matrix.multiplyMM(alienView, 0, this.view, 0, alienModel, 0);
                Matrix.multiplyMM(alienMVP, 0, eye.getPerspective(0.1f, 100f), 0, alienView, 0);
                OpenGLUtils.bindMVPToProgram(this.program, alienMVP, "vMatrix");
                int alienVHandle = OpenGLUtils.bindVerticesToProgram(this.program, alien.bufferize(), "vVertices");
                int alienCHandle = OpenGLUtils.bindColorToProgram(this.program, alien.colors(), "vColors");
                OpenGLUtils.drawTriangles(alien.size(), alienVHandle, alienCHandle);
            }
        }


        float[] laserModelView = new float[16];
        float[] lasersMvp = new float[16];
        for (Laser laser : this.lasers) {
            float[] laserModel = laser.model();
            Matrix.multiplyMM(laserModelView, 0, this.view, 0, laserModel, 0);
            Matrix.multiplyMM(lasersMvp, 0, eye.getPerspective(0.1f, 100f), 0, laserModelView, 0);
            OpenGLUtils.bindMVPToProgram(this.program, lasersMvp, "vMatrix");
            int lasersVHandle = OpenGLUtils.bindVerticesToProgram(this.program, laser.bufferize(), "vVertices");
            int lasersCHandle = OpenGLUtils.bindColorToProgram(this.program, laser.colors(), "vColors");
            OpenGLUtils.drawLines(laser.size(), 120, lasersVHandle, lasersCHandle);
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

    private void initializeAliens() {
        while (this.aliens.size() < NUMBER_OF_SHIPS) {
            float z = MathUtils.randRange(this.distanceToTravel - 4, this.distanceToTravel - 2);
            float x = MathUtils.randRange(-2, 2);
            float y = MathUtils.randRange(-2, 2);
            if (Math.abs(x) > 0.7 && Math.abs(y) > 0.7
                && willNotCollideWithOthers(x, y, z)) {
                AlienShip ship = new AlienShip(new Coordinates(x, y, z), 3);
                this.aliens.add(ship);
                this.coolDownPerShip.put(ship, 0L);
            }
        }
        this.timeUntilNextAlien = this.timeBetweenShips;
    }

    private boolean willNotCollideWithOthers(float x, float y, float z) {
        for (Polyhedron ship : this.aliens) {
            if (Math.abs(ship.getPosition().z - z) <= 0.4
                && Math.abs(ship.getPosition().y - y) <= 0.4
                && Math.abs(ship.getPosition().x - x) <= 0.4) {
                return false;
            }
        }
        return true;
    }

    private void fireFrom(Polyhedron ship) {
        Coordinates start = new Coordinates(
                ship.getPosition().x,
                ship.getPosition().y,
                ship.getPosition().z
        );
        Coordinates end = new Coordinates(
                ship.getPosition().x,
                ship.getPosition().y,
                ship.getPosition().z - 0.5f
        );
        Laser laser = new Laser(start, end);
        laser.color(102, 238, 94);

        int laserAudioId = this.audioEngine.createSoundObject(LASER_SOUNDFILE);
        laser.audio(laserAudioId);
        this.audioEngine.setSoundObjectPosition(laserAudioId, end.x, end.y, end.z);
        this.audioEngine.playSound(laserAudioId, false);
        this.lasers.add(laser);
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

}
