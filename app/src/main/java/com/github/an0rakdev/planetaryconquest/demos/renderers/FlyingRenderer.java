package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.StationaryBody;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.configs.EarthConfiguration;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.configs.MoonConfiguration;
import com.github.an0rakdev.planetaryconquest.graphics.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the first demo of the application which
 * make a VR fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class FlyingRenderer extends SpaceRenderer {
    private StationaryBody moon;
    private StationaryBody earth;
    private float distanceElapsed;

    private int celestialProgram;
    private float cameraZPos;
    private final float[] camera;
    private final float[] view;
    private final float[] model;
    private final float[] modelView;
    private final float[] mvp;

	/**
	 * Create a new Flying Renderer with the given Android Context.
	 *
	 * @param context the current Android context.
	 */
	public FlyingRenderer(final Context context) {
		super(context, new FlyingProperties(context));
		this.distanceElapsed = ((FlyingProperties) this.getProperties()).getDistanceToTravel();
		this.camera = new float[16];
		this.view = new float[16];
		this.model = new float[16];
		this.modelView = new float[16];
		this.mvp = new float[16];
		this.cameraZPos = getProperties().getCameraPositionZ();
	}

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
		super.onSurfaceCreated(config);
        this.moon = new StationaryBody(new MoonConfiguration(this.getContext()));
        this.earth = new StationaryBody(new EarthConfiguration(this.getContext()));

        this.celestialProgram = OpenGLUtils.newProgram();
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.celestialProgram);
        final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.celestialProgram);
        OpenGLUtils.linkProgram(this.celestialProgram, vertexShader, fragmentShader);
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final FlyingProperties properties = (FlyingProperties) this.getProperties();
        final float currentDistance = (properties.getCameraSpeed() / 1000f) * time;
        final float cameraPosX = getProperties().getCameraPositionX();
        final float cameraPosY = getProperties().getCameraPositionY();
        final float cameraDirX = getProperties().getCameraDirectionX();
        final float cameraDirY = getProperties().getCameraDirectionY();
        final float cameraDirZ = getProperties().getCameraDirectionZ();
        if (distanceElapsed >0f) {
            this.cameraZPos += currentDistance;
            distanceElapsed -= currentDistance;
        }
        Matrix.setLookAtM(this.camera, 0,
                cameraPosX, cameraPosY, this.cameraZPos,
                cameraPosX + cameraDirX, cameraPosY + cameraDirY, this.cameraZPos + cameraDirZ,
                0, 1, 0);
        Matrix.setIdentityM(this.model, 0);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);
        GLES20.glUseProgram(this.celestialProgram);

        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.modelView, 0, this.view, 0, this.model, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
        // Pass the MVP to the shader.
        final int mvpHandle = GLES20.glGetUniformLocation(this.celestialProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, this.mvp, 0);

        draw(moon.model());
        draw(earth.model());
    }

    private void draw(final Polyhedron sphere)  {
        // Pass the vertices to the shader.
        final int verticesHandle = GLES20.glGetAttribLocation(this.celestialProgram, "vVertices");
        GLES20.glEnableVertexAttribArray(verticesHandle);
        GLES20.glVertexAttribPointer(verticesHandle, DIMENSION, GLES20.GL_FLOAT, false, 3* FLOAT_BYTES, sphere.bufferize());
        // Pass the color to the shader.
        final int colorHandle = GLES20.glGetAttribLocation(this.celestialProgram, "vColors");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, sphere.colors());

        // Draw.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, sphere.size());
        GLES20.glDisableVertexAttribArray(colorHandle);
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }
}
