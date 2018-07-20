package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.graphics.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Model;
import com.github.an0rakdev.planetaryconquest.graphics.ScaleShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Scaling;
import com.github.an0rakdev.planetaryconquest.graphics.ShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlActivity extends Activity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new OpenGLSurfaceView(this));
    }

    private class OpenGLSurfaceView extends GLSurfaceView {
        public OpenGLSurfaceView(final Context context) {
            super(context);
            setEGLContextClientVersion(2);
            setRenderer(new OpenGLRenderer(context));
        }
    }

    private class OpenGLRenderer implements GLSurfaceView.Renderer {
        private final Context context;
        private Model model;
        private MVPShaderProgram shaderProgram;
        private boolean transformationOk;

        OpenGLRenderer(final Context context) {
            this.context = context;
            this.transformationOk = false;
        }

        public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
            GLES20.glClearColor(0f, 0f, 0f, 1f);
            this.model = new Triangle();
            this.shaderProgram = new ScaleShaderProgram(this.context);
        }

        public void onDrawFrame(final GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            this.shaderProgram.draw(this.model);
            if (!this.transformationOk) {
                if (Scaling.class.isAssignableFrom(this.shaderProgram.getClass())) {
                    ((Scaling) this.shaderProgram).rescale(0.2f, 0.2f, 0.2f);
                    transformationOk = true;
                }
            }
        }

        public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
            GLES20.glViewport(0, 0, width, height);
            this.shaderProgram.adaptToScene(width, height);
        }
    }
}