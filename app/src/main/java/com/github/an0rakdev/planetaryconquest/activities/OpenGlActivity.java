package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.views.OpenGlSurfaceView;

public class OpenGlActivity extends Activity {
     @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new OpenGlSurfaceView(this));
    }
}