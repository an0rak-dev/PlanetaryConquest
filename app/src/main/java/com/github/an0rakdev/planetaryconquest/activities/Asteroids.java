package com.github.an0rakdev.planetaryconquest.activities;

import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.demos.renderers.AsteroidsRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class Asteroids extends GvrActivity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.asteroids_activity);
		final GvrView gvrView = findViewById(R.id.asteroids_gvr_view);
		gvrView.setRenderer(new AsteroidsRenderer(this));
		gvrView.setTransitionViewEnabled(true);
		this.setGvrView(gvrView);
	}
}
