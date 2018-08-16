package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.StationaryBody;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.configs.EarthConfiguration;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.configs.MoonConfiguration;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.programs.VRProgram;
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
    private final FlyingProperties properties;
    private VRProgram vrShaderProgram;
    private StationaryBody moon;
    private StationaryBody earth;
    private float distanceElapsed;

	/**
	 * Create a new Flying Renderer with the given Android Context.
	 *
	 * @param context the current Android context.
	 */
	public FlyingRenderer(final Context context) {
		super(context, new FlyingProperties(context));
		this.properties = new FlyingProperties(context);
		this.distanceElapsed = this.properties.getDistanceToTravel();
	}

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
		super.onSurfaceCreated(config);
        this.moon = new StationaryBody(new MoonConfiguration(this.getContext()));
        this.earth = new StationaryBody(new EarthConfiguration(this.getContext()));
        this.vrShaderProgram = new VRProgram(this.getContext(), this.properties.getCameraPosition());
        this.vrShaderProgram.getCamera().setLookAt(this.properties.getCameraDirection());
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
		super.onNewFrame(headTransform);
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final float currentDistance = (this.properties.getCameraSpeed() / 1000f) * time;
        if (distanceElapsed >0f) {
			this.vrShaderProgram.getCamera().move(currentDistance);
            distanceElapsed -= currentDistance;
        }
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);
        this.vrShaderProgram.adaptToEye(eye);
        this.vrShaderProgram.draw(this.moon.model());
        this.vrShaderProgram.draw(this.earth.model());
    }
}
