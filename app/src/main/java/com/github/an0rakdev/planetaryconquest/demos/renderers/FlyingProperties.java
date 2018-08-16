package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;

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


	/**
	 * Return the initial camera position in the Flying Demo.
	 *
	 * @return the intal camera position.
	 */
	public Coordinates getCameraPosition() {
		return new Coordinates(
				Float.valueOf(this.getProperty("flying.camera.position.x", "0")),
				Float.valueOf(this.getProperty("flying.camera.position.y", "0")),
				Float.valueOf(this.getProperty("flying.camera.position.z", "0"))
		);
	}
}
