package com.github.an0rakdev.planetaryconquest.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.github.an0rakdev.planetaryconquest.renderers.OpenGlRenderer;

public class OpenGlSurfaceView extends GLSurfaceView {
    private OpenGlRenderer openGlRenderer;

    public OpenGlSurfaceView(final Context context) {
        super(context);
        setEGLContextClientVersion(2);
        this.openGlRenderer = new OpenGlRenderer(context);
        setRenderer(this.openGlRenderer);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final float vScreenMiddle = getHeight() / 2;
        if (MotionEvent.ACTION_MOVE == event.getAction()) {
            float y = event.getY() - vScreenMiddle;
            y /= vScreenMiddle;
            this.openGlRenderer.setVTouchMovement(y);
        }
        return true;
    }
}
