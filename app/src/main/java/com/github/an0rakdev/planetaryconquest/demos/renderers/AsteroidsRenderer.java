package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class AsteroidsRenderer extends SpaceRenderer {

	public AsteroidsRenderer(final Context context) {
		super(context, new AsteroidsProperties(context));
	}

	@Override
	public void onSurfaceCreated(final EGLConfig config) {

	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {

	}

	@Override
	public void onDrawEye(final Eye eye) {

	}

	@Override
	public void onFinishFrame(final Viewport viewport) {
		// Do nothing.
	}

	@Override
	public void onSurfaceChanged(final int width, final int height) {
		// Do nothing.
	}

	@Override
	public void onRendererShutdown() {
		// Do nothing.
	}
}
