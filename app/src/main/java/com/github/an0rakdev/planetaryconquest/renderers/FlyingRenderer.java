package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.PointBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.StarsModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim3.Sphere;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.PointShaderProgram;
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
    private static final float SPEED_M_PER_MS = 0.003f;
    private Context context;
    private PointShaderProgram starsShaderProgram;
    private PointBasedModel stars;
    private VRShaderProgram vrShaderProgram;
    private TriangleBasedModel moon;

    public FlyingRenderer(final Context context) {
        this.context = context;
    }

    private TriangleBasedModel shape2;
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        long time = SystemClock.uptimeMillis() % DELAY_BETWEEN_FRAMES;
        final float headRotation[] = new float[4];
        headTransform.getQuaternion(headRotation, 0);

        float x = 0f;
        float y = 0f;
        float z = 0f;

        float yRotation = Math.abs(headRotation[1]);
        if (Math.abs(headRotation[0]) > 0.25) {
            y = SPEED_M_PER_MS * time;
            if (0f < headRotation[0]) { y = -y; }
        }
        if (yRotation > 0.25 && yRotation < 0.8) {
            x = SPEED_M_PER_MS * time;
            if (0f > yRotation) { x = -x; }
        }
        if (x == 0f && y == 0f) {
            z = SPEED_M_PER_MS * time;
            if (yRotation >= 0.8) { z = -z; }
        }
        this.vrShaderProgram.moveCameraOf(x,y,z);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        // This method is called twice (one for each eye) to draw the frame.
        // The given eye param offers the possibility to adapt perspective.
        // The drawing specifities of your app goes here.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        this.starsShaderProgram.draw(this.stars);

        this.vrShaderProgram.adaptToEye(eye);
        this.vrShaderProgram.draw(this.moon);
        this.vrShaderProgram.draw(this.shape2);
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
        this.moon = new Sphere(new Coordinates(-2.5f, 3f, -16f), 1f);
        this.moon.precision(3);
        this.moon.setBackgroundColor(new Color(0.545f, 0.533f, 0.513f));
        this.vrShaderProgram = new VRShaderProgram(this.context);

        this.stars = new StarsModel(200, 1,1,1);
        this.starsShaderProgram = new PointShaderProgram(this.context, 4);

        this.shape2 = new Sphere(new Coordinates(2f, 5f, 0f), 1f);
        this.shape2.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onRendererShutdown() {
        // Do nothing.
    }
}
