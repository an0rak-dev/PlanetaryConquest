package com.github.an0rakdev.planetaryconquest.demos.renderers;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.Properties;

public class SpaceProperties extends Properties {

	/**
	 * Return the number of frames per second used for a Space Demo.
	 *
	 * (key : space.fps)
	 *
	 * @return the FPS of the Flying Demo.
	 */
	public long getFps() {
		return Long.valueOf(this.getProperty("space.fps", "60"));
	}

	/**
	 * Return the initial camera direction in Space Demo.
	 *
	 * (keys : space.camera.direction.{x|y|z} )
	 *
	 * @return the inital camera direction.
	 */
	public Coordinates getCameraDirection() {
		return new Coordinates(
				Float.valueOf(this.getProperty("space.camera.direction.x", "0")),
				Float.valueOf(this.getProperty("space.camera.direction.y", "0")),
				Float.valueOf(this.getProperty("space.camera.direction.z", "0"))
		);
	}

	/**
	 * Return the size of each stars to display.
	 *
	 * (key : space.stars.size)
	 *
	 * @return the stars size.
	 */
	public int getStarsSize() {
		return Integer.valueOf(this.getProperty("space.stars.size", "1"));
	}
}
