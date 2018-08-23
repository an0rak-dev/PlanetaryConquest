package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the first demo of the application which
 * make a VR fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class FlyingRenderer extends SpaceRenderer {
    private Polyhedron moon;
    private Polyhedron earth;
    private float distanceElapsed;

    private int celestialProgram;
    private float cameraZPos;
    private final float[] camera;
    private final float[] view;
    private final float[] model;
    private final float[] modelView;
    private final float[] mvp;

    /**
     * Create a new Flying Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
    public FlyingRenderer(final Context context) {
        super(context, new FlyingProperties(context));

        final FlyingProperties config = (FlyingProperties) this.getProperties();
        this.distanceElapsed = config.getDistanceToTravel();
        this.camera = new float[16];
        this.view = new float[16];
        this.model = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];
        this.cameraZPos = getProperties().getCameraPositionZ();
        initializeCelestialBodies(config);
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        super.onSurfaceCreated(config);
        this.celestialProgram = OpenGLUtils.newProgram();
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.celestialProgram);
        final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.celestialProgram);
        OpenGLUtils.linkProgram(this.celestialProgram, vertexShader, fragmentShader);
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final FlyingProperties properties = (FlyingProperties) this.getProperties();
        final float currentDistance = (properties.getCameraSpeed() / 1000f) * time;
        final float cameraPosX = getProperties().getCameraPositionX();
        final float cameraPosY = getProperties().getCameraPositionY();
        final float cameraDirX = getProperties().getCameraDirectionX();
        final float cameraDirY = getProperties().getCameraDirectionY();
        final float cameraDirZ = getProperties().getCameraDirectionZ();
        if (distanceElapsed > 0f) {
            this.cameraZPos += currentDistance;
            distanceElapsed -= currentDistance;
        }
        Matrix.setLookAtM(this.camera, 0,
                cameraPosX, cameraPosY, this.cameraZPos,
                cameraPosX + cameraDirX, cameraPosY + cameraDirY, this.cameraZPos + cameraDirZ,
                0, 1, 0);
        Matrix.setIdentityM(this.model, 0);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);

        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.modelView, 0, this.view, 0, this.model, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
        OpenGLUtils.use(this.celestialProgram);
        OpenGLUtils.bindMVPToProgram(this.celestialProgram, this.mvp, "vMatrix");

        draw(this.moon);
        draw(this.earth);
    }

    private void draw(final Polyhedron sphere) {
        final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.celestialProgram, sphere.bufferize(), "vVertices");
        final int colorHandle = OpenGLUtils.bindColorToProgram(this.celestialProgram, sphere.colors(), "vColors");
        OpenGLUtils.drawTriangles(sphere.size(), verticesHandle, colorHandle);
    }

    private void initializeCelestialBodies(FlyingProperties config) {
        this.moon = new Sphere(
                new Coordinates(config.getMoonCenterX(), config.getMoonCenterY(), config.getMoonCenterZ()),
                config.getMoonSize());
        this.moon.precision(3);
        this.moon.background(
                OpenGLUtils.toOpenGLColor(config.getMoonColorR(), config.getMoonColorG(), config.getMoonColorB()));

        this.earth = new Sphere(
                new Coordinates(config.getEarthCenterX(), config.getEarthCenterY(), config.getEarthCenterZ()),
                config.getEarthSize());
        this.earth.precision(3);
        this.earth.background(
                OpenGLUtils.toOpenGLColor(config.getEarthColorR(), config.getEarthColorG(), config.getEarthColorB()));
    }
}
