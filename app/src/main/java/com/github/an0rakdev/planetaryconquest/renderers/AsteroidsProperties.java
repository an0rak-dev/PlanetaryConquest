package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;

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

	public float getAsteroidMinX() {
		return Float.valueOf(this.getProperty("asteroids.field.min.x", "0"));
	}

	public float getAsteroidMinY() {
		return Float.valueOf(this.getProperty("asteroids.field.min.y", "0"));
	}

	public float getAsteroidMinZ() {
		return Float.valueOf(this.getProperty("asteroids.field.min.z", "1"));
	}

	public float getAsteroidMaxX() {
		return Float.valueOf(this.getProperty("asteroids.field.max.x", "5"));
	}

	public float getAsteroidMaxY() {
		return Float.valueOf(this.getProperty("asteroids.field.max.y", "2"));
	}

	public float getAsteroidMaxZ() {
		return Float.valueOf(this.getProperty("asteroids.field.max.z", "2"));
	}

	public float getMinAsteroidSize() {
		return Float.valueOf(this.getProperty("asteroids.field.unit.min.size", "0"));
	}

	public float getMaxAsteroidSize() {
		return Float.valueOf(this.getProperty("asteroids.field.unit.max.size", "0"));
	}

	public float getAsteroidsColorR() {
		return Float.valueOf(this.getProperty("asteroids.field.color.r", "100"));
	}

	public float getAsteroidsColorG() {
		return Float.valueOf(this.getProperty("asteroids.field.color.g", "100"));
	}

	public float getAsteroidsColorB() {
		return Float.valueOf(this.getProperty("asteroids.field.color.b", "100"));
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

    public float getCrossSize() {
    	return Float.valueOf(this.getProperty("asteroids.hud.cross.size", "1"));
	}

	public float getCrossColorR() {
		return Float.valueOf(this.getProperty("asteroids.hud.cross.color.r", "255"));
	}

	public float getCrossColorG() {
		return Float.valueOf(this.getProperty("asteroids.hud.cross.color.g", "255"));
	}

	public float getCrossColorB() {
		return Float.valueOf(this.getProperty("asteroids.hud.cross.color.b", "255"));
	}

	public float getLaserSpeed() {
    	return Float.valueOf(this.getProperty("asteroids.laser.speed", "10"));
	}

	public long getLaserCoolDown() {
    	return Long.valueOf(this.getProperty("asteroids.laser.cooldown.in.ms", "250"));
	}
}
