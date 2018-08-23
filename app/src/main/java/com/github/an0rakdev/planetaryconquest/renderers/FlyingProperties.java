package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * The Flying Demo configuration.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class FlyingProperties extends SpaceProperties {
	/**
	 * Load the file "assets/flying.properties" and return a new FlyingProperties config.
	 *
	 * @param context the current Android's context.
	 */
	FlyingProperties(final Context context) {
		super();
		try {
			this.load(context.getAssets().open("flying.properties"));
		} catch (final IOException ex) {
			Log.w("FlyingProperties", "Unable to load the flying configuration.");
		}
	}

	/**
	 * Return the camera movement's speed for the Flying Demo.
	 *
	 * @return the speed of the Camera movement.
	 */
	public long getCameraSpeed() {
		return Long.valueOf(this.getProperty("flying.camera.speed.meter.per.s", "3"));
	}

	/**
	 * Return the total distance to travel by the Camera in the Flying Demo.
	 *
	 * @return the total distance made by the Camera.
	 */
	public long getDistanceToTravel() {
		return Long.valueOf(this.getProperty("flying.distance.to.travel.in.meters", "10"));
	}

	public float getMoonCenterX() {
		return Float.valueOf(this.getProperty("flying.moon.center.x", "1"));
	}

	public float getMoonCenterY() {
		return Float.valueOf(this.getProperty("flying.moon.center.y", "1"));
	}

	public float getMoonCenterZ() {
		return Float.valueOf(this.getProperty("flying.moon.center.z", "1"));
	}

	public float getMoonSize() {
		return Float.valueOf(this.getProperty("flying.moon.size", "1"));
	}

	public float getMoonColorR() {
		return Float.valueOf(this.getProperty("flying.moon.color.r", "120"));
	}


	public float getMoonColorG() {
		return Float.valueOf(this.getProperty("flying.moon.color.g", "120"));
	}


	public float getMoonColorB() {
		return Float.valueOf(this.getProperty("flying.moon.color.b", "120"));
	}


	public float getEarthCenterX() {
		return Float.valueOf(this.getProperty("flying.earth.center.x", "1"));
	}

	public float getEarthCenterY() {
		return Float.valueOf(this.getProperty("flying.earth.center.y", "1"));
	}

	public float getEarthCenterZ() {
		return Float.valueOf(this.getProperty("flying.earth.center.z", "1"));
	}

	public float getEarthSize() {
		return Float.valueOf(this.getProperty("flying.earth.size", "1"));
	}

	public float getEarthColorR() {
		return Float.valueOf(this.getProperty("flying.earth.color.r", "0"));
	}


	public float getEarthColorG() {
		return Float.valueOf(this.getProperty("flying.earth.color.g", "0"));
	}


	public float getEarthColorB() {
		return Float.valueOf(this.getProperty("flying.earth.color.b", "120"));
	}
}
