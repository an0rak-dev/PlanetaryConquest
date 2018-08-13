package com.github.an0rakdev.planetaryconquest.demos.astronomic;

import com.github.an0rakdev.planetaryconquest.graphics.Color;

import java.util.Properties;

/**
 * The base configuration expected for an astronomic body.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class Configuration extends Properties {
	/**
	 * Return the horizontal position of the current body's center.
	 *
	 * @return the x-axis position
	 */
	public abstract float getXPosition();

	/**
	 * Return the vertical position of the current body's center.
	 *
	 * @return the y-axis position
	 */
	public abstract float getYPosition();

	/**
	 * Return the depth position of the current body's center.
	 *
	 * @return the z-axis position
	 */
	public abstract float getZPosition();

	/**
	 * Return the size (radius) of the current body.
	 *
	 * @return the size of the body.
	 */
	public abstract int getSize();

	/**
	 * Return the color of the current body.
	 *
	 * @return the uniform color
	 */
	public abstract Color getColor();

	/**
	 * Return the float value linked to the given key.
	 *
	 * @param name the key of the wanted value
	 * @param defaultValue the value to use if the given key doesn't exists.
	 * @return the wanted value or the default one if it doesn't exists.
	 */
	protected final float getFloat(final String name, final float defaultValue) {
		return Float.valueOf(this.getProperty(name,String.valueOf(defaultValue)));
	}

	/**
	 * Return the int value linked to the given key.
	 *
	 * @param name the key of the wanted value
	 * @param defaultValue the value to use if the given key doesn't exists.
	 * @return the wanted value or the default one if it doesn't exists.
	 */
	protected final int getInt(final String name, final int defaultValue) {
		return Integer.valueOf(this.getProperty(name,String.valueOf(defaultValue)));
	}
}
