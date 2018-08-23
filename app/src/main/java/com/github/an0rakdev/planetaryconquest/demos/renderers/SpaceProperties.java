package com.github.an0rakdev.planetaryconquest.demos.renderers;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.Properties;

/**
 * The base class for the configurations of demo in Space.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class SpaceProperties extends Properties {

	/**
	 * Return the number of frames per second used for a Space Demo.
	 *
	 * (key : space.fps)
	 *
	 * @return the FPS of the Demo.
	 */
	public long getFps() {
		return Long.valueOf(this.getProperty("space.fps", "60"));
	}

	/**
	 * Return the number of stars to display in the view.
	 *
	 * (key : space.stars.size)
	 *
	 * @return the stars size.
	 */
	public int getStarsCount() { return Integer.valueOf(this.getProperty("space.stars.count", "10")); }

	public float getStarsXBound() {
		return Float.valueOf(this.getProperty("space.stars.bounds.x", "5.0"));
	}

	public float getStarsYBound() {
		return Float.valueOf(this.getProperty("space.stars.bounds.y", "4.0"));
	}

	public float getStarsZBound() {
		return Float.valueOf(this.getProperty("space.stars.bounds.z", "3.0"));
	}

	public float getStarsColorR() {
		return Float.valueOf(this.getProperty("space.stars.color.r", "255.0"));
	}

	public float getStarsColorG() {
		return Float.valueOf(this.getProperty("space.stars.color.g", "255.0"));
	}

	public float getStarsColorB() {
		return Float.valueOf(this.getProperty("space.stars.color.b", "255.0"));
	}

	public float getCameraPositionX() {
		return Float.valueOf(this.getProperty("space.camera.position.x", "0"));
	}

	public float getCameraPositionY() {
		return Float.valueOf(this.getProperty("space.camera.position.y", "0"));
	}

	public float getCameraPositionZ() {
		return Float.valueOf(this.getProperty("space.camera.position.z", "0"));
	}

	public float getCameraDirectionX() {
		return Float.valueOf(this.getProperty("space.camera.direction.x", "0"));
	}

	public float getCameraDirectionY() {
		return Float.valueOf(this.getProperty("space.camera.direction.y", "0"));
	}

	public float getCameraDirectionZ() {
		return Float.valueOf(this.getProperty("space.camera.direction.z", "1"));
	}
}
