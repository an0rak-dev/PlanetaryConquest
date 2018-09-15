package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

public class AliensProperties extends SpaceProperties {
    /**
     * Load the file "assets/aliens.properties" and return a new AliensProperties config.
     *
     * @param context the current Android's context.
     */
    AliensProperties(final Context context) {
        super();
        try {
            this.load(context.getAssets().open("aliens.properties"));
        } catch (final IOException ex) {
            Log.w("AliensProperties", "Unable to load the asteroids configuration.");
        }
    }

}
