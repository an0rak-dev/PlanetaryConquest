package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.github.an0rakdev.planetaryconquest.graphics.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Model;
import com.github.an0rakdev.planetaryconquest.graphics.ScaleShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Scaling;
import com.github.an0rakdev.planetaryconquest.graphics.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private Model model;
    private MVPShaderProgram shaderProgram;
    private float dy;

    public OpenGlRenderer(final Context context) {
        this.context = context;
        this.dy = 0f;
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        this.model = new Triangle();
        this.shaderProgram = new ScaleShaderProgram(this.context);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        this.shaderProgram.draw(this.model);
        if (0.0f != this.dy && Scaling.class.isAssignableFrom(this.shaderProgram.getClass())) {
            final float delta = 1 - this.dy;
            ((Scaling) this.shaderProgram).rescale(delta, delta, delta);
            this.dy = 0f;
        }
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
        this.shaderProgram.adaptToScene(width, height);
    }

    public void setVTouchMovement(final float d) {
        if (0f != d) {
            this.dy = d;
        }
    }
}
