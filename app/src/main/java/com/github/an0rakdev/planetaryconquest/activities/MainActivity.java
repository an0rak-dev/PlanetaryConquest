package com.github.an0rakdev.planetaryconquest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.an0rakdev.planetaryconquest.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startOpenGLDemo(final View view) {
        final Intent intent = new Intent(this, OpenGlActivity.class);
        startActivity(intent);
    }

    public void startFlyingDemo(final View view) {
        final Intent intent = new Intent(this, FlyingActivity.class);
        startActivity(intent);
    }
}
