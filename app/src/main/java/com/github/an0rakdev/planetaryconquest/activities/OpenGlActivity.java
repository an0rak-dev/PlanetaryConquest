package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.github.an0rakdev.planetaryconquest.graphics.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Model;
import com.github.an0rakdev.planetaryconquest.graphics.ScaleShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Scaling;
import com.github.an0rakdev.planetaryconquest.graphics.ShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlActivity extends Activity {
    private float dy;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new OpenGLSurfaceView(this));
        this.dy = 0.0f;
    }

    private class OpenGLSurfaceView extends GLSurfaceView {

        public OpenGLSurfaceView(final Context context) {
            super(context);
            setEGLContextClientVersion(2);
            setRenderer(new OpenGLRenderer(context));
        }

        @Override
        public boolean onTouchEvent(final MotionEvent event) {
            if (MotionEvent.ACTION_MOVE == event.getAction()) {
                float zero = getHeight() / 2;
                float y = event.getY();
                dy = (y - zero) / (getHeight() / 2);
            }
            return true;
        }
    }

    private class OpenGLRenderer implements GLSurfaceView.Renderer {
        private final Context context;
        private Model model;
        private MVPShaderProgram shaderProgram;

        OpenGLRenderer(final Context context) {
            this.context = context;
        }

        public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
            GLES20.glClearColor(0f, 0f, 0f, 1f);
            this.model = new Triangle();
            this.shaderProgram = new ScaleShaderProgram(this.context);
        }

        public void onDrawFrame(final GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            this.shaderProgram.draw(this.model);
            if (0.0f != dy && Scaling.class.isAssignableFrom(this.shaderProgram.getClass())) {
                ((Scaling) this.shaderProgram).rescale(1-dy, 1-dy, 1-dy);
            }
        }

        public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
            GLES20.glViewport(0, 0, width, height);
            this.shaderProgram.adaptToScene(width, height);
        }
    }
}