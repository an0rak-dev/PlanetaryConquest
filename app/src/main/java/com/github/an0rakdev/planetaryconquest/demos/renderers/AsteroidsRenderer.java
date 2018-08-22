package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.AsteroidField;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.programs.VRProgram;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the second demo of the application which
 * make the user across an asteroid fields and shoot them.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class AsteroidsRenderer extends SpaceRenderer {
	private VRProgram vrShader;
	private AsteroidField field;
	private final float asteroidsSpeed;
	private final float cameraSpeed;
	private float distanceElapsed;

    /**
     * Create a new Asteroids Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
	public AsteroidsRenderer(final Context context) {
		super(context, new AsteroidsProperties(context));
		final AsteroidsProperties config = (AsteroidsProperties) this.getProperties();
		this.field = new AsteroidField(config.getAsteroidsCount());
		this.field.setBounds(config.getAsteroidsFieldMin(),
				config.getAsteroidsFieldMax());
		this.field.setMinSize(config.getMinAsteroidSize());
		this.field.setMaxSize(config.getMaxAsteroidSize());
		this.field.setDefaultColor(config.getAsteroidsColor());
		this.asteroidsSpeed = config.getAsteroidsSpeed() / 1000;
		this.cameraSpeed = config.getCameraSpeed() / 1000;
		this.distanceElapsed = config.getDistanceToTravel();
	}

	@Override
	public void onSurfaceCreated(final EGLConfig config) {
		super.onSurfaceCreated(config);
		final AsteroidsProperties conf = (AsteroidsProperties) this.getProperties();
		this.vrShader = new VRProgram(this.getContext(), conf.getCameraPosition());
		this.vrShader.getCamera().setLookAt(conf.getCameraDirection());
	}

	@Override
	public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
		long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
		float asteroidsDistance = this.asteroidsSpeed * time;
		this.vrShader.move(asteroidsDistance, 0f,0f);
		final float cameraDistance = this.cameraSpeed * time;
		if (distanceElapsed >0f) {
			this.vrShader.getCamera().move(0f, 0f, cameraDistance);
			distanceElapsed -= cameraDistance;
		}
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
