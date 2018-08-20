package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.io.IOException;

/**
 * The Asteroids Demo configuration.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
final class AsteroidsProperties extends SpaceProperties {
    /**
     * Load the file "assets/asteroids.properties" and return a new AsteroidsProperties config.
     *
     * @param context the current Android's context.
     */
	AsteroidsProperties(final Context context) {
		super();
		try {
			this.load(context.getAssets().open("asteroids.properties"));
		} catch (final IOException ex) {
			Log.w("AsteroidsProperties", "Unable to load the asteroids configuration.");
		}
	}

	/**
	 * Return the initial camera position in the Asteroids Demo.
	 *
	 * @return the intal camera position.
	 */
	public Coordinates getCameraPosition() {
		return new Coordinates(
				Float.valueOf(this.getProperty("asteroids.camera.position.x", "0")),
				Float.valueOf(this.getProperty("asteroids.camera.position.y", "0")),
				Float.valueOf(this.getProperty("asteroids.camera.position.z", "0"))
		);
	}

	public int getAsteroidsCount() {
		return Integer.valueOf(this.getProperty("asteroids.field.count", "0"));
	}

	public float getAsteroidsSpeed() {
		return Float.valueOf(this.getProperty("asteroids.field.speed.meter.per.second", "1"));
	}


	public Coordinates getAsteroidsFieldMin() {
		return new Coordinates(
				Float.valueOf(this.getProperty("asteroids.field.bound.min.x", "0")),
				Float.valueOf(this.getProperty("asteroids.field.bound.min.y", "0")),
				Float.valueOf(this.getProperty("asteroids.field.bound.min.z", "0"))
		);
	}

	public Coordinates getAsteroidsFieldMax() {
		return new Coordinates(
				Float.valueOf(this.getProperty("asteroids.field.bound.max.x", "0")),
				Float.valueOf(this.getProperty("asteroids.field.bound.max.y", "0")),
				Float.valueOf(this.getProperty("asteroids.field.bound.max.z", "0"))
		);
	}

	public float getMinAsteroidSize() {
		return Float.valueOf(this.getProperty("asteroids.field.unit.min.size", "0"));
	}

	public float getMaxAsteroidSize() {
		return Float.valueOf(this.getProperty("asteroids.field.unit.max.size", "0"));
	}

	public Color getAsteroidsColor() {
		return new Color(
				Float.valueOf(this.getProperty("asteroids.field.unit.color.r", "0")),
				Float.valueOf(this.getProperty("asteroids.field.unit.color.g", "0")),
				Float.valueOf(this.getProperty("asteroids.field.unit.color.b", "0"))
		);
	}

    /**
     * Return the camera movement's speed for the Asteroids Demo.
     *
     * @return the speed of the Camera movement.
     */
    public float getCameraSpeed() {
        return Float.valueOf(this.getProperty("asteroids.camera.speed.meter.per.s", "3"));
    }

    /**
     * Return the total distance to travel by the Camera in the Asteroids Demo.
     *
     * @return the total distance made by the Camera.
     */
    public long getDistanceToTravel() {
        return Long.valueOf(this.getProperty("asteroids.distance.to.travel.in.meters", "10"));
    }

}
