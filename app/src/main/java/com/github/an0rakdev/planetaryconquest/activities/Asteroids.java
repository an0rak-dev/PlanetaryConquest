/* ****************************************************************************
 * Asteroids.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.activities;

import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.AsteroidsRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class Asteroids extends GvrActivity {
	private AsteroidsRenderer renderer;

	@Override
	public void onPause() {
		this.renderer.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		this.renderer.resume();
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.asteroids_activity);
		final GvrView gvrView = findViewById(R.id.asteroids_gvr_view);
		this.renderer = new AsteroidsRenderer(this);
		gvrView.setRenderer(this.renderer);
		this.setGvrView(gvrView);
	}
}
