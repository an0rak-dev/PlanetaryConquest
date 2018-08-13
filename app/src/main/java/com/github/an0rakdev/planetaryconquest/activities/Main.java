package com.github.an0rakdev.planetaryconquest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.an0rakdev.planetaryconquest.R;

/**
 * Entry point activity of the application.
 * <p>
 *     Display severals demo to launch during the presentation.
 * </p>
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
    }

    public void startFlyingDemo(final View view) {
        final Intent intent = new Intent(this, Flying.class);
        this.startActivity(intent);
    }
}
