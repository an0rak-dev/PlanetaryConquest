package com.github.an0rakdev.planetaryconquest.activities;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.FlyingRenderer;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;

public class FlyingActivity extends GvrActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flying_activity);

        GvrView gvrView = findViewById(R.id.flying_gvr_view);

        // Add a Renderer, here a custom Renderer which implements the StereoRenderer
        gvrView.setRenderer(new FlyingRenderer(this));
        // Display the view which says the user to put his phone in the Cardboard.
        gvrView.setTransitionViewEnabled(true);
        setGvrView(gvrView);
    }
}
