package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.AlienShip;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

public class AliensRenderer extends SpaceRenderer {
    private Sphere mars;
    private int program;
    private final float[] camera;
    private final float[] view;
    private final float[] mvp;
    private float distanceMade;
    private final List<Polyhedron> aliens;

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
        this.aliens = new ArrayList<>();
        this.aliens.add(new AlienShip(new Coordinates(1, 3, 50)));
        this.aliens.add(new AlienShip(new Coordinates(-2, 4, 58)));
        this.aliens.add(new AlienShip(new Coordinates(4, 1.5f, 54)));
        this.aliens.add(new AlienShip(new Coordinates(-4, -2, 51)));
        this.aliens.add(new AlienShip(new Coordinates(0, -3.5f, 56)));
        this.aliens.add(new AlienShip(new Coordinates(2.3f, -3, 52)));
        this.aliens.add(new AlienShip(new Coordinates(-5, 3, 57)));
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
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        final AliensProperties config = (AliensProperties) getProperties();

        if (userHasToMoveAgain()) {
            long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
            final float currentDistance = (config.getMovementSpeed() / 1000f) * time;
            this.distanceMade += currentDistance;
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
            for (final Polyhedron alien : this.aliens) {
                final int alienVHandle = OpenGLUtils.bindVerticesToProgram(this.program, alien.bufferize(), "vVertices");
                final int alienCHandle = OpenGLUtils.bindColorToProgram(this.program, alien.colors(), "vColors");
                OpenGLUtils.drawTriangles(alien.size(), alienVHandle, alienCHandle);
            }
        }
    }

    private boolean userHasToMoveAgain() {
        final AliensProperties config = (AliensProperties) getProperties();
        return config.getCameraPositionZ() + this.distanceMade < config.getDistanceToTravel();
    }

}
