package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class AsteroidsRenderer implements GvrView.StereoRenderer {
	private final Context context;

	public AsteroidsRenderer(final Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(EGLConfig config) {

	}

	@Override
	public void onNewFrame(HeadTransform headTransform) {

	}

	@Override
	public void onDrawEye(Eye eye) {

	}

	@Override
	public void onFinishFrame(Viewport viewport) {

	}

	@Override
	public void onSurfaceChanged(int width, int height) {

	}

	@Override
	public void onRendererShutdown() {

	}
}
