package com.github.an0rakdev.planetaryconquest.demos.astronomic.configs;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.demos.astronomic.Configuration;
import com.github.an0rakdev.planetaryconquest.graphics.Color;

import java.io.IOException;

/**
 * The Earth display configuration.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class EarthConfiguration extends Configuration {
	/**
	 * Create a new configuration and load the file "assets/earth.properties".
	 *
	 * @param context the current Android Context.
	 */
	public EarthConfiguration(final Context context) {
		super();
		try {
			this.load(context.getAssets().open("earth.properties"));
		} catch (final IOException ex) {
			Log.w("EarthConfiguration", "Unable to load the Earth configuration.");
		}
	}

	@Override
	public float getXPosition() {
		return this.getFloat("earth.position.x", 0f);
	}

	@Override
	public float getYPosition() {
		return this.getFloat("earth.position.y", 0f);
	}

	@Override
	public float getZPosition() {
		return this.getFloat("earth.position.z", 0f);
	}

	@Override
	public int getSize() {
		return this.getInt("earth.size", 1);
	}

	@Override
	public Color getColor() {
		final float r = this.getFloat("earth.color.r", 1f);
		final float g = this.getFloat("earth.color.g", 1f);
		final float b = this.getFloat("earth.color.b", 1f);
		return new Color(r,g,b);

	}
}
