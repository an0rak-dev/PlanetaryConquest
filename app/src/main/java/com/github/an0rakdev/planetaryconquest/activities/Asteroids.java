package com.github.an0rakdev.planetaryconquest.activities;

import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.AsteroidsRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class Asteroids extends GvrActivity {
	private AsteroidsRenderer renderer;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.asteroids_activity);
		final GvrView gvrView = findViewById(R.id.asteroids_gvr_view);
		this.renderer = new AsteroidsRenderer(this);
		gvrView.setRenderer(this.renderer);
		gvrView.setTransitionViewEnabled(true);
		this.setGvrView(gvrView);
	}

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
}
