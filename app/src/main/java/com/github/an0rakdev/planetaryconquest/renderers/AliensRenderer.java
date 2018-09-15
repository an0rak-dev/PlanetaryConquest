package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;

public class AliensRenderer extends SpaceRenderer {
    public AliensRenderer(Context context) {
        super(context, new AliensProperties(context));
    }
}
