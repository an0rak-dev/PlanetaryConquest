/* ****************************************************************************
 * FlyingRenderer.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.OpenGLProgram;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.SphericalBody;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the first demo of the application which
 * make a VR fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class FlyingRenderer extends SpaceRenderer implements GvrView.StereoRenderer {
    private final float movementSpeed;
    private final float[] view;
    private final float[] mvp;
    private final float near;
    private final float far;

    private SphericalBody moon;
    private SphericalBody earth;
    private float distanceElapsed;
    private OpenGLProgram openGlProgram;

    /**
     * Create a new Flying Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
    public FlyingRenderer(Context context) {
        super(context);

        this.distanceElapsed = 25; // meters.
        this.movementSpeed = 5; // m/s
        this.near = 0.1f;
        this.far = 100f;

        this.view = new float[16];
        this.mvp = new float[16];
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        this.moon = new SphericalBody(new Coordinates(2.5f, 3.5f, 20), 1);
        this.moon.background(138, 135, 130);
        this.earth = new SphericalBody(new Coordinates(0, -8, 10), 5);
        this.earth.background(32, 119, 238);

        // Create the OpenGL Program
        this.openGlProgram = new OpenGLProgram(OpenGLProgram.DrawType.TRIANGLES);
        String vertexSources = readContentOf(R.raw.mvp_vertex);
        String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        this.openGlProgram.compile(vertexSources, fragmentSources);
        this.openGlProgram.setAttributesNames("vMatrix", "vVertices", "vColors");

        // Create the stars program
        this.createStarsProgram();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        float currentDistance = (this.movementSpeed / 1000f) * time;
        if (distanceElapsed > 0f) {
            this.moon.moveForward(-currentDistance);
            this.earth.moveForward(-currentDistance);
            distanceElapsed -= currentDistance;
        }
        this.countNewFrame();
    }

    @Override
    public void onDrawEye(Eye eye) {
        OpenGLUtils.clear();
        this.drawStars(eye);

        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.getCamera(), 0);
        float[] perspective = eye.getPerspective(this.near, this.far);

        this.openGlProgram.activate();
        float[] moonModelView = new float[16];
        Matrix.multiplyMM(moonModelView, 0, this.view, 0, this.moon.model(), 0);
        Matrix.multiplyMM(this.mvp, 0, perspective, 0, moonModelView, 0);
        this.openGlProgram.useMVP(this.mvp);
        this.openGlProgram.draw(this.moon.getShape());

        float[] earthModelView = new float[16];
        Matrix.multiplyMM(earthModelView, 0, this.view, 0, this.earth.model(), 0);
        Matrix.multiplyMM(this.mvp, 0, perspective, 0, earthModelView, 0);
        this.openGlProgram.useMVP(this.mvp);
        this.openGlProgram.draw(this.earth.getShape());
    }

    @Override
    public void onFinishFrame(Viewport viewport) { /* Do nothing. */ }

    @Override
    public void onSurfaceChanged(int width, int height) { /* Do nothing. */ }

    @Override
    public void onRendererShutdown() { /* Do nothing. */ }
}
