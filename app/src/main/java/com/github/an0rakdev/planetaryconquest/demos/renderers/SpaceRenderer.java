package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.Stars;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.programs.VRPointProgram;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public abstract class SpaceRenderer implements GvrView.StereoRenderer {
	private final Context context;
	private final SpaceProperties properties;
	private final long timeBetweenFrames;
	private VRPointProgram starsShaderProgram;
	private static Stars stars = null;

	SpaceRenderer(final Context context, final SpaceProperties properties) {
		this.context = context;
		this.properties = properties;
		this.timeBetweenFrames = (1000 / this.properties.getFps()) - 1L;
	}

	@Override
	public void onSurfaceCreated(final EGLConfig config) {
		if (null == stars) {
			stars = new Stars(this.context);
		}
		this.starsShaderProgram = new VRPointProgram(this.context,
					this.properties.getCameraDirection(), this.properties.getStarsSize());
		this.starsShaderProgram.getCamera().setLookAt(this.properties.getCameraDirection());
	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {
		// Do nothing.
	}

	@Override
	public void onDrawEye(final Eye eye) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		starsShaderProgram.adaptToEye(eye);
		starsShaderProgram.draw(stars.model());
	}

	@Override
	public void onFinishFrame(final Viewport viewport) {
		// Do nothing.
	}

	@Override
	public void onSurfaceChanged(final int width, final int height) {
		// Classic one.
	}

	@Override
	public void onRendererShutdown() {
		// Do nothing.
	}

	final long getTimeBetweenFrames() {
		return this.timeBetweenFrames;
	}

	protected final Context getContext() {
		return this.context;
	}
}
