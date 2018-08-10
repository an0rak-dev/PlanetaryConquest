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
        if (event.getY() < getHeight() / 2) {
            if (event.getX() > getWidth() / 2) {
                this.openGlRenderer.rotateLeft();
            } else {
                this.openGlRenderer.rotateRight();
            }
        } else {
            this.openGlRenderer.move();
        }
        return true;
    }
}
