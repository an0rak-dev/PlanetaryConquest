/* ****************************************************************************
 * SpaceRenderer.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.FrameCounter;
import com.github.an0rakdev.planetaryconquest.OpenGLProgram;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.StarsCloud;
import com.google.vr.sdk.base.Eye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class SpaceRenderer {
    private static final int TARGETED_FPS = 90;
    private static final int STARS_COUNT = 320;
    // Stars informations
    private static StarsCloud stars = null;
    private final float[] starColor;
    // Utilities
    private final Context context;
    private final FrameCounter frameCounter;
    private final long timeBetweenFrames;
    // Rendering informations.
    private final float[] camera;
    private final float[] view;
    private final float[] mvp;
    private OpenGLProgram starsProgram;

    SpaceRenderer(final Context context) {
        this.context = context;
        this.timeBetweenFrames = (1000 / TARGETED_FPS) - 1L;
        this.frameCounter = new FrameCounter(1000);
        this.camera = new float[16];
        this.view = new float[16];
        this.mvp = new float[16];
        this.starColor = OpenGLUtils.toOpenGLColor(255, 255, 255);
        if (null == stars) {
            stars = new StarsCloud(STARS_COUNT, -5, 5, -3, 3, -3, 3);
        }
    }

    final float[] getCamera() {
        return this.camera;
    }

    /**
     * Has to be called in the OpenGL rendering calls (onSurfaceCreated, onDrawFrame, onSurfaceChanged)
     */
    final void createStarsProgram() {
        this.starsProgram = new OpenGLProgram(OpenGLProgram.DrawType.POINTS);
        final String vertexSources = readContentOf(R.raw.point_vertex);
        final String fragmentSources = readContentOf(R.raw.simple_fragment);
        this.starsProgram.compile(vertexSources, fragmentSources);
        this.starsProgram.setAttributesNames("vMatrix", "vVertices", "vColor");
        Matrix.setLookAtM(this.camera, 0,
                getCameraPosition().x, getCameraPosition().y, getCameraPosition().z,
                getCameraDirection().x, getCameraDirection().y, getCameraDirection().z,
                0f, 1f, 0f);
    }

    final void countNewFrame() {
        this.frameCounter.increase();
        this.frameCounter.log();
    }


    /**
     * Has to be called in the OpenGL rendering calls (onSurfaceCreated, onDrawFrame, onSurfaceChanged)
     */
    final void drawStars(Eye eye) {
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
        this.starsProgram.activate();
        this.starsProgram.set("vPointSize", 4);
        this.starsProgram.useMVP(this.mvp);
        this.starsProgram.draw(stars, this.starColor);
    }

    final long getTimeBetweenFrames() {
        return this.timeBetweenFrames;
    }

    final String readContentOf(final int fd) {
        final InputStream inputStream = this.context.getResources().openRawResource(fd);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder contentSb = new StringBuilder();
        final String lineSep = System.getProperty("line.separator");
        try {
            String line = reader.readLine();
            while (null != line) {
                contentSb.append(line).append(lineSep);
                line = reader.readLine();
            }
            reader.close();
        } catch (final IOException ex) {
            Log.e("SpaceRenderer", "Unable to read the content of " + fd);
        }
        return contentSb.toString();
    }

    private Coordinates getCameraPosition() {
        return new Coordinates(0,0,0);
    }

    private Coordinates getCameraDirection() {
        return new Coordinates(0,0,1);
    }
}
