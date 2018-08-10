package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim2.Square;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim3.Octahedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.dim3.Tetrahedron;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.MVPShaderProgram;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private TriangleBasedModel shape;
    private TriangleBasedModel shape2;
    private MVPShaderProgram shaderProgram;

    public OpenGlRenderer(final Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        this.shape = new Octahedron(new Coordinates(
                1, 0, -5
        ), 1f,1f);
        this.shape2 = new Tetrahedron(new Coordinates(-1, 0, -5), 1f);
        this.shaderProgram = new MVPShaderProgram(this.context);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); // Reset background
        this.shaderProgram.draw(this.shape);
        this.shaderProgram.draw(this.shape2);
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
        this.shaderProgram.adaptToScene(width, height);
    }

    private int currentHRotation = 0;

    public void rotateLeft() {
        this.currentHRotation -= 5;
        this.shaderProgram.rotateCamera(this.currentHRotation, 0);
    }

    public void rotateRight() {
        this.currentHRotation += 5;
        this.shaderProgram.rotateCamera(this.currentHRotation, 0);
    }
}
