package com.github.an0rakdev.planetaryconquest;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

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
            setRenderer(new OpenGLRenderer());
        }
    }

    private class OpenGLRenderer implements GLSurfaceView.Renderer {
        private Triangle triangle;
        private ShaderProgram shaderProgram;

        public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
            GLES20.glClearColor(0f, 0f, 0f, 1f);
            this.triangle = new Triangle();
            this.shaderProgram = new ShaderProgram();
            this.shaderProgram.addShader(Triangle.VERTEX_SHADER, GLES20.GL_VERTEX_SHADER, "Vertex");
            this.shaderProgram.addShader(Triangle.FRAGMENT_SHADER, GLES20.GL_FRAGMENT_SHADER, "Fragment");
            this.shaderProgram.prepare();
        }

        public void onDrawFrame(final GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            this.shaderProgram.draw(this.triangle);
        }

        public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
            GLES20.glViewport(0, 0, width, height);
        }
    }
}