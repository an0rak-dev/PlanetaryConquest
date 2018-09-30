package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.github.an0rakdev.planetaryconquest.OpenGLProgram;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.SphericalBody;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * This renderer manages the first demo of the application which
 * make a VR fly over the Earth to the Moon.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class FlyingRenderer extends SpaceRenderer {
    private SphericalBody moon;
    private SphericalBody earth;
    private float distanceElapsed;
    private final float cameraSpeed;
    private float cameraZPos;

    private OpenGLProgram openGlProgram;
    private final float[] camera;
    private final float[] view;
    private final float[] mvp;

    /**
     * Create a new Flying Renderer with the given Android Context.
     *
     * @param context the current Android context.
     */
    public FlyingRenderer(final Context context) {
        super(context, null);

        this.cameraZPos = getCameraPosition().z;
        this.distanceElapsed = 25; // meters.
        this.cameraSpeed = 5; // m/s

        this.moon = new SphericalBody(new Coordinates(2.5f, 3.5f, 20), 1);
        this.moon.background(138, 135, 130);
        this.earth = new SphericalBody(new Coordinates(0, -8, 10), 5);
        this.earth.background(32, 119, 238);

        this.camera = new float[16];
        this.view = new float[16];
        this.mvp = new float[16];
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        super.onSurfaceCreated(config);
        this.openGlProgram = new OpenGLProgram(OpenGLProgram.DrawType.TRIANGLES);
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        this.openGlProgram.compile(vertexSources, fragmentSources);
        this.openGlProgram.setAttributesNames("vMatrix", "vVertices", "vColors");
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        long time = SystemClock.uptimeMillis() % this.getTimeBetweenFrames();
        final float currentDistance = (this.cameraSpeed / 1000f) * time;
        final float cameraDirZ = getCameraDirection().z;
        if (distanceElapsed > 0f) {
            this.cameraZPos += currentDistance;
            distanceElapsed -= currentDistance;
        }
        Matrix.setLookAtM(this.camera, 0,
                getCameraPosition().x, getCameraPosition().y, this.cameraZPos,
                getCameraDirection().x, getCameraDirection().y, this.cameraZPos + cameraDirZ,
                0, 1, 0);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);

        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);
        this.openGlProgram.activate();
        this.openGlProgram.useMVP(this.mvp);
        this.openGlProgram.draw(this.moon.getShape());
        this.openGlProgram.draw(this.earth.getShape());
    }
}
