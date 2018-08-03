package com.github.an0rakdev.planetaryconquest.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.R;
import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

public class PanoramicActivity extends AppCompatActivity {

    public static final String TAG = "PanoramicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panoramic_activity);
        final VrPanoramaView panoBack = findViewById(R.id.pano_view);
        // Create a panoramic viewer
        this.loadPanoramaView(panoBack);
        // Force the VR mode
        panoBack.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);
    }

    private void loadPanoramaView(VrPanoramaView panoBack) {
        InputStream is = null;
        try {
            is = getAssets().open("home.jpg");
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            panoBack.loadImageFromBitmap(BitmapFactory.decodeStream(is), options);

        } catch (final IOException ex) {
            Log.e(TAG, "Error while loading the background.", ex);
        } finally {
            if (null != is){
                try {
                    is.close();
                } catch (final IOException ex) {
                    Log.e(TAG, "Error while closing the stream to the bg.", ex);
                }
            }
        }
    }
}
