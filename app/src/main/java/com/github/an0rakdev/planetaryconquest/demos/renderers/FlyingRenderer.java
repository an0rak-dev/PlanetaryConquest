package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.Stars;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.StationaryBody;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.configs.EarthConfiguration;
import com.github.an0rakdev.planetaryconquest.demos.astronomic.configs.MoonConfiguration;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.programs.VRPointProgram;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.programs.VRProgram;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the first demo of the application which
 * make a VR fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class FlyingRenderer implements GvrView.StereoRenderer {
    private final FlyingProperties properties;
    private final long delayBetweenFrames;
    private Context context;
    private VRPointProgram starsShaderProgram;
    private VRProgram vrShaderProgram;
    private StationaryBody moon;
    private StationaryBody earth;
	private Stars stars;
    private float distanceElapsed;

	/**
	 * Create a new Flying Renderer with the given Android Context.
	 *
	 * @param context the current Android context.
	 */
	public FlyingRenderer(final Context context) {
		this.context = context;
		this.properties = new FlyingProperties(context);
		this.delayBetweenFrames = (1000 / this.properties.getFps()) - 1L;
		this.distanceElapsed = this.properties.getDistanceToTravel();
	}

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        this.moon = new StationaryBody(new MoonConfiguration(this.context));
        this.earth = new StationaryBody(new EarthConfiguration(this.context));
        this.stars = new Stars(this.context);
        this.vrShaderProgram = new VRProgram(this.context, this.properties.getCameraPosition());
		this.starsShaderProgram = new VRPointProgram(this.context,
				this.properties.getCameraDirection(), this.properties.getStarsSize());
        this.vrShaderProgram.getCamera().setLookAt(this.properties.getCameraDirection());
        this.starsShaderProgram.getCamera().setLookAt(this.properties.getCameraDirection());
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        long time = SystemClock.uptimeMillis() % this.delayBetweenFrames;
        final float currentDistance = (this.properties.getCameraSpeed() / 1000f) * time;
        if (distanceElapsed >0f) {
			this.vrShaderProgram.getCamera().move(currentDistance);
            distanceElapsed -= currentDistance;
        }
    }

    @Override
    public void onDrawEye(final Eye eye) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        this.vrShaderProgram.adaptToEye(eye);
        this.starsShaderProgram.adaptToEye(eye);
        this.starsShaderProgram.draw(this.stars.model());
        this.vrShaderProgram.draw(this.moon.model());
        this.vrShaderProgram.draw(this.earth.model());
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
}
