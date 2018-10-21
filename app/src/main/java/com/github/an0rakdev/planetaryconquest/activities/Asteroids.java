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

/**
 * This activity is created for displaying a set of asteroids to
 * destroy when moving the head.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class Asteroids extends GvrActivity {
	AsteroidsRenderer renderer;

	/**
	 * Send the pause order to the renderer.
	 */
	@Override
	public void onPause() {
		this.renderer.pause();
		super.onPause();
	}

	/**
	 * Send the resume order to the renderer.
	 */
	@Override
	public void onResume() {
		this.renderer.resume();
		super.onResume();
	}

	/**
	 * Create a Google VR renderer dedicated to this demo and
	 * set it to this Google VR view.
	 */
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
