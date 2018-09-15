package com.github.an0rakdev.planetaryconquest.activities;

import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.AliensRenderer;
import com.github.an0rakdev.planetaryconquest.renderers.AsteroidsRenderer;
import com.github.an0rakdev.planetaryconquest.renderers.SpaceRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class Aliens extends GvrActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.asteroids_activity);
        final GvrView gvrView = findViewById(R.id.asteroids_gvr_view);
        SpaceRenderer renderer = new AliensRenderer(this);
        gvrView.setRenderer(renderer);
        gvrView.setTransitionViewEnabled(true);
        this.setGvrView(gvrView);
    }

}
