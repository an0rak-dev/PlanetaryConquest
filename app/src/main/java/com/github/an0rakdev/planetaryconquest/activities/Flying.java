package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.FlyingRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

/**
 * This activity is created for displaying the fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class Flying extends Activity {
    private GLSurfaceView.Renderer renderer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(new FlyingView(this));
    }

    private class FlyingView extends GLSurfaceView {

        public FlyingView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            renderer = new FlyingRenderer(context);
            setRenderer(renderer);
        }
    }
}
