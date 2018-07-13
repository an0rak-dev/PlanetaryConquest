package com.github.an0rakdev.planetaryconquest;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.Constraints;
import android.util.Log;

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
        Log.i(Constraints.TAG, "onNewFrame: ");
    }

    @Override
    public void onDrawEye(Eye eye) {
        Log.i(Constraints.TAG, "onDrawEye");
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        Log.i(Constraints.TAG, "onFinishFrame");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(Constraints.TAG, "onSurfaceChanged");
    }

    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(Constraints.TAG, "onSurfaceCreated");
    }

    @Override
    public void onRendererShutdown() {
        Log.i(Constraints.TAG, "onRendererShutdown");
    }
}
