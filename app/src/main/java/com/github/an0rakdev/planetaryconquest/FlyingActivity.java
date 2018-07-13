package com.github.an0rakdev.planetaryconquest;

import android.app.Activity;
import android.os.Bundle;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class FlyingActivity extends GvrActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flying_activity);

        GvrView gvrView = findViewById(R.id.flying_gvr_view);

        // Display the view which says the user to put his phone in the Cardboard.
        gvrView.setTransitionViewEnabled(true);

        setGvrView(gvrView);
    }
}
