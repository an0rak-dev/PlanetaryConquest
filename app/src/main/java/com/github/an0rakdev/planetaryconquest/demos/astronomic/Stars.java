package com.github.an0rakdev.planetaryconquest.demos.astronomic;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.models.points.PointCloud;

import java.io.IOException;
import java.util.Properties;

public final class Stars {
	private PointCloud model;

	public Stars(final Context context) {
		final Properties properties = new Properties();
		try {
			properties.load(context.getAssets().open("stars.properties"));
		} catch (final IOException ex) {
			Log.w("Stars", "Unable to load the stars configuration.");
		}

		this.model = new PointCloud(
			Integer.valueOf(properties.getProperty("stars.count.per.face", "10")),
			Integer.valueOf(properties.getProperty("stars.max.x.pos", "1")),
			Integer.valueOf(properties.getProperty("stars.max.y.pos", "1")),
			Integer.valueOf(properties.getProperty("stars.front.z.pos", "1")),
			Integer.valueOf(properties.getProperty("stars.back.z.pos", "-1"))
		);
	}

	public PointCloud model() {
		return this.model;
	}
}
