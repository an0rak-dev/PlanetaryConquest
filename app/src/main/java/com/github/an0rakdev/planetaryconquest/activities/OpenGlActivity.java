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

import com.github.an0rakdev.planetaryconquest.graphics.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Model;
import com.github.an0rakdev.planetaryconquest.graphics.ScaleShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Scaling;
import com.github.an0rakdev.planetaryconquest.graphics.ShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private float lastZPosition;
    private float deltaZ;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new OpenGLSurfaceView(this));
        this.lastZPosition = 0.0f;
        this.deltaZ = 0f;
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            this.deltaZ = sensorEvent.values[2] - this.lastZPosition;
            this.lastZPosition = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing.
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
            if (Scaling.class.isAssignableFrom(this.shaderProgram.getClass())) {
                if (deltaZ != 0) {
                    // FIXME : Define min and max scale bounds + a z(0) point.
                    ((Scaling) this.shaderProgram).rescale(1 + deltaZ, 1 + deltaZ, 1 + deltaZ);
                    deltaZ = 0f;
                }
            }
        }

        public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
            GLES20.glViewport(0, 0, width, height);
            this.shaderProgram.adaptToScene(width, height);
        }
    }
}