package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.graphics.models.dim2.Triangle;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.transformations.ScaleShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.transformations.YRotationShaderProgram;

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
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); // Reset background
        if (this.shaderProgram instanceof YRotationShaderProgram) {
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.090f * ((int) time);
            ((YRotationShaderProgram) this.shaderProgram).applyRotation(angle);
        }
        if (0.0f != this.dy && this.shaderProgram instanceof ScaleShaderProgram) {
            final float delta = 1 - this.dy;
            ((ScaleShaderProgram) this.shaderProgram).setScaleTo(delta, delta, delta);
            this.dy = 0f;
        }
        this.shaderProgram.draw(this.model);

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
