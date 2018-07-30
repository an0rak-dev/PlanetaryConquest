package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim3.Sphere;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.VRShaderProgram;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class FlyingRenderer implements GvrView.StereoRenderer {
    private Context context;
    private VRShaderProgram shaderProgram;
    private Model moon;

    public FlyingRenderer(final Context context) {
        this.context = context;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Called when a new frame is calculated and needs to be rendered.
        // The logic of your app goes here.
    }

    @Override
    public void onDrawEye(final Eye eye) {
        // This method is called twice (one for each eye) to draw the frame.
        // The given eye param offers the possibility to adapt perspective.
        // The drawing specifities of your app goes here.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        this.shaderProgram.adaptToEye(eye);
        this.shaderProgram.draw(this.moon);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        // Do nothing.
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        // Classic one.
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        // Classic.
        this.moon = new Sphere(new Coordinates(-2.5f, 3f, -6f), 1f);
        this.moon.precision(3);
        this.moon.setBackgroundColor(new Color(0.545f, 0.533f, 0.513f));
        this.shaderProgram = new VRShaderProgram(context);
    }

    @Override
    public void onRendererShutdown() {
        // Do nothing.
    }
}
