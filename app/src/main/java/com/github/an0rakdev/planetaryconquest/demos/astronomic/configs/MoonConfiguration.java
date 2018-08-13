package com.github.an0rakdev.planetaryconquest.demos.astronomic.configs;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.Configuration;
import com.github.an0rakdev.planetaryconquest.graphics.Color;

import java.io.IOException;

/**
 * The Moon display configuration.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class MoonConfiguration extends Configuration {
	/**
	 * Create a new configuration and load the file "assets/moon.properties".
	 *
	 * @param context the current Android Context.
	 */
	public MoonConfiguration(final Context context) {
		super();
		try {
			this.load(context.getAssets().open("moon.properties"));
		} catch (final IOException ex) {
			Log.w("MoonConfiguration", "Unable to load the Moon configuration.");
		}
	}

	public float getXPosition() {
		return this.getFloat("moon.position.x", 0f);
	}

	public float getYPosition() {
		return this.getFloat("moon.position.y", 0f);
	}

	public float getZPosition() {
		return this.getFloat("moon.position.z", 0f);
	}

	public int getSize() {
		return this.getInt("moon.size", 1);
	}

	public Color getColor() {
		final float r = this.getFloat("moon.color.r", 1f);
		final float g = this.getFloat("moon.color.g", 1f);
		final float b = this.getFloat("moon.color.b", 1f);
		return new Color(r,g,b);

	}
}
