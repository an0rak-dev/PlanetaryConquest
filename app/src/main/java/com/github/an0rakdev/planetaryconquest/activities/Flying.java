/* ****************************************************************************
 * Flying.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.activities;

import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.FlyingRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

/**
 * This activity is created for displaying the fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class Flying extends GvrActivity {
    /**
     * Create a Google VR renderer dedicated to this demo and
     * set it to this Google VR view.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.flying_activity);
        final GvrView gvrView = findViewById(R.id.flying_gvr_view);
        gvrView.setRenderer(new FlyingRenderer(this));
        this.setGvrView(gvrView);
    }
}
