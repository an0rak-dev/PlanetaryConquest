package com.github.an0rakdev.planetaryconquest.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.R;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

public class PanoramicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panoramic_actitvity);
        final VrPanoramaView panoBack = findViewById(R.id.pano_view);

        try {
            final InputStream is = getAssets().open("background.jpg");
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            panoBack.loadImageFromBitmap(BitmapFactory.decodeStream(is), options);
            is.close();
        } catch (final IOException ex) {
            Log.e("FlyingActivity", "Error while loading the background.", ex);
        }
    }
}
