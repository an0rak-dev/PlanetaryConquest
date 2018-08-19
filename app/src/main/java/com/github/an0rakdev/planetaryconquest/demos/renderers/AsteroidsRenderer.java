package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.AsteroidField;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.programs.VRProgram;
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
	private VRProgram vrShader;
	private AsteroidsProperties config;
	private AsteroidField field;

    /**
     * Create a new Asteroids Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
	public AsteroidsRenderer(final Context context) {
		super(context, new AsteroidsProperties(context));
		this.config = new AsteroidsProperties(context);
		this.field = new AsteroidField(this.config.getAsteroidsCount(),
				this.config.getAsteroidsFieldMin(), this.config.getAsteroidsFieldMax());
	}

	@Override
	public void onSurfaceCreated(final EGLConfig config) {
		super.onSurfaceCreated(config);
		this.vrShader = new VRProgram(this.getContext(), this.config.getCameraPosition());
	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
	}

	@Override
	public void onDrawEye(final Eye eye) {
		super.onDrawEye(eye);
		this.vrShader.adaptToEye(eye);
		for (final Polyhedron asteroid : this.field.asteroids()) {
			this.vrShader.draw(asteroid);
		}
	}
}
