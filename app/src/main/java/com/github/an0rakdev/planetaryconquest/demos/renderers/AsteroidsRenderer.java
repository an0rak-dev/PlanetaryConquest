package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the second demo of the application which
 * make the user croos an asteroid fields and shoot them.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class AsteroidsRenderer extends SpaceRenderer {

    /**
     * Create a new Asteroids Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
	public AsteroidsRenderer(final Context context) {
		super(context, new AsteroidsProperties(context));
	}

	@Override
	public void onSurfaceCreated(final EGLConfig config) {
		super.onSurfaceCreated(config);
	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
	}

	@Override
	public void onDrawEye(final Eye eye) {
		super.onDrawEye(eye);
	}
}
