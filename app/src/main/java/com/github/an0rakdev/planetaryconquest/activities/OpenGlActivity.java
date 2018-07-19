package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.ShaderProgram;
import com.github.an0rakdev.planetaryconquest.graphics.Triangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlActivity extends Activity {
    private static final String TAG = "OpenGLActivity";

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
            this.attachShader(R.raw.simple_vertex, GLES20.GL_VERTEX_SHADER);
            this.attachShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
            this.shaderProgram.prepare();
        }

        public void onDrawFrame(final GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            this.shaderProgram.draw(this.triangle);
        }

        public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        private void attachShader(final int shaderFd, final int type) {
            final String shaderSource = this.readContentOf(shaderFd);
            if (!shaderSource.isEmpty()) {
                this.shaderProgram.addShader(shaderSource, type);
            }
        }

        private String readContentOf(final int fd) {
            final InputStream inputStream = getResources().openRawResource(fd);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final StringBuilder contentSb = new StringBuilder();
            final String lineSep = System.getProperty("line.separator");
            try {
                String line = reader.readLine();
                while (null != line) {
                    contentSb.append(line).append(lineSep);
                    line = reader.readLine();
                }
                reader.close();
            } catch (final IOException ex) {
                Log.e(TAG, "Unable to read the content of " + fd);
            }
            return contentSb.toString();
        }
    }
}