package com.github.an0rakdev.planetaryconquest.activities;

import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.R;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class FlyingActivity extends GvrActivity implements GvrView.StereoRenderer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flying_activity);

        GvrView gvrView = findViewById(R.id.flying_gvr_view);

        // Add a Renderer,  here a StereoRenderer to display the left and right image (one per eye).
        gvrView.setRenderer(this);
        // Display the view which says the user to put his phone in the Cardboard.
        gvrView.setTransitionViewEnabled(true);

        setGvrView(gvrView);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Log.i("FlyingActivity", "onNewFrame: ");
    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        Log.i("FlyingActivity", "onFinishFrame");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i("FlyingActivity", "onSurfaceChanged");
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i("FlyingActivity", "onSurfaceCreated");
    }

    @Override
    public void onRendererShutdown() {
        Log.i("FlyingActivity", "onRendererShutdown");
    }
}
