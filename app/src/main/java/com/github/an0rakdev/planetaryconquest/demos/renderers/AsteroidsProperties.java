package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.util.Log;

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
}
