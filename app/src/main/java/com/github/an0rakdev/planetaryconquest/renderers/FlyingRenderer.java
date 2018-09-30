package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.SphericalBody;
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
    private SphericalBody moon;
    private SphericalBody earth;
    private float distanceElapsed;

    private int celestialProgram;
    private float cameraZPos;
    private final float[] camera;
    private final float[] view;
    private final float[] model;
    private final float[] mvp;

    /**
     * Create a new Flying Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
    public FlyingRenderer(final Context context) {
        super(context, null);

        this.distanceElapsed = 25; // meters.
        this.camera = new float[16];
        this.view = new float[16];
        this.model = new float[16];
        this.mvp = new float[16];
        this.cameraZPos = getCameraPosition().z;

        this.moon = new SphericalBody(new Coordinates(2.5f, 3.5f, 20), 1);
        this.moon.background(138, 135, 130);
        this.earth = new SphericalBody(new Coordinates(0, -8, 10), 5);
        this.earth.background(32, 119, 238);
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
        final float cameraSpeed = 7; // m/s
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final float currentDistance = (cameraSpeed / 1000f) * time;
        final float cameraPosX = getCameraPosition().x;
        final float cameraPosY = getCameraPosition().y;
        final float cameraDirX = getCameraDirection().x;
        final float cameraDirY = getCameraDirection().y;
        final float cameraDirZ = getCameraDirection().z;
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
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
        OpenGLUtils.use(this.celestialProgram);
        OpenGLUtils.bindMVPToProgram(this.celestialProgram, this.mvp, "vMatrix");
        this.moon.draw(this.celestialProgram);
        this.earth.draw(this.celestialProgram);
    }
}
