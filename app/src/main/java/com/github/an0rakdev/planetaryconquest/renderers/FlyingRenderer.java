package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.PointBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.StarsModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim3.Sphere;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.VRPointShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.VRShaderProgram;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class FlyingRenderer implements GvrView.StereoRenderer {
    private static final int FPS = 90;
    private static final long DELAY_BETWEEN_FRAMES = (1000 / FPS) - 1L;
    private static final float SPEED_M_PER_MS = 0.005f;
    private Context context;
    private VRPointShaderProgram starsShaderProgram;
    private PointBasedModel stars;
    private VRShaderProgram vrShaderProgram;
    private TriangleBasedModel moon;
    private TriangleBasedModel earth;
    private float distanceElapsed;

    public FlyingRenderer(final Context context) {
        this.context = context;
        this.distanceElapsed = 25f;
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        // Classic.
        this.moon = new Sphere(new Coordinates(2.5f, 3.5f, 20f), 1f);
        this.moon.precision(3);
        this.moon.setBackgroundColor(Color.LUNAR_GREY);
        this.earth = new Sphere(new Coordinates(0f, -8f, 10f), 5f);
        this.earth.precision(3);
        this.earth.setBackgroundColor(Color.EARTH_BLUE);
        this.stars = new StarsModel(320, 5, 3, 3, -3);
        this.vrShaderProgram = new VRShaderProgram(this.context);
        this.starsShaderProgram = new VRPointShaderProgram(this.context, 4);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        long time = SystemClock.uptimeMillis() % DELAY_BETWEEN_FRAMES;
        final float currentDistance = SPEED_M_PER_MS * time;
        if (distanceElapsed >0f) {
            this.vrShaderProgram.moveCamera(currentDistance);
            distanceElapsed -= currentDistance;
        }
    }

    @Override
    public void onDrawEye(final Eye eye) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        this.vrShaderProgram.adaptToEye(eye);
        this.starsShaderProgram.adaptToEye(eye);
        this.starsShaderProgram.draw(this.stars);
        this.vrShaderProgram.draw(this.moon);
        this.vrShaderProgram.draw(this.earth);
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
    public void onRendererShutdown() {
        // Do nothing.
    }
}
