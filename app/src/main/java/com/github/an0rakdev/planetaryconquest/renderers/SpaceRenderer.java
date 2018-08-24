package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.FrameCounter;
import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.MathUtils;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;

public abstract class SpaceRenderer implements GvrView.StereoRenderer {
    static final int FLOAT_BYTES = (Float.SIZE / Byte.SIZE);
    static final int DIMENSION = 3;
    private static final String TAG = "Renderer";
    // Stars informations
    private static FloatBuffer starsVertices = null;
    private final float[] starColor;
    // Utilities
    private final Context context;
    private final SpaceProperties properties;
    private final FrameCounter frameCounter;
    private final long timeBetweenFrames;
    // Rendering informations.
    private final float[] camera;
    private final float[] view;
    private final float[] mvp;
    private int starsShader;

    SpaceRenderer(final Context context, final SpaceProperties properties) {
        this.context = context;
        this.properties = properties;
        this.timeBetweenFrames = (1000 / this.properties.getFps()) - 1L;
        this.frameCounter = new FrameCounter(1000);
        this.camera = new float[16];
        this.view = new float[16];
        this.mvp = new float[16];
        this.starColor = OpenGLUtils.toOpenGLColor(
                this.properties.getStarsColorR(),
                this.properties.getStarsColorG(),
                this.properties.getStarsColorB());
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        if (null == starsVertices) {
            initializeStars();
        }
        final String vertexSources = readContentOf(R.raw.point_vertex);
        final String fragmentSources = readContentOf(R.raw.simple_fragment);
        this.starsShader = OpenGLUtils.newProgram();
        final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.starsShader);
        final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.starsShader);
        OpenGLUtils.linkProgram(this.starsShader, vertexShader, fragmentShader);
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        this.frameCounter.increase();
        this.frameCounter.log();

        final float cameraPosX = this.properties.getCameraPositionX();
        final float cameraPosY = this.properties.getCameraPositionY();
        final float cameraPosZ = this.properties.getCameraPositionZ();
        final float cameraDirX = this.properties.getCameraDirectionX();
        final float cameraDirY = this.properties.getCameraDirectionY();
        final float cameraDirZ = this.properties.getCameraDirectionZ();
        Matrix.setLookAtM(this.camera, 0,
                cameraPosX, cameraPosY, cameraPosZ,
                cameraDirX, cameraDirY, cameraDirZ,
                0f, 1f, 0f);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.view, 0);

        OpenGLUtils.clear();
        OpenGLUtils.use(this.starsShader);
        OpenGLUtils.setPointSizeOf(this.starsShader, 4f, "vPointSize");
        final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.starsShader, starsVertices, "vVertices");
        OpenGLUtils.bindMVPToProgram(this.starsShader, this.mvp, "vMatrix");
        OpenGLUtils.bindColorToProgram(this.starsShader, this.starColor, "vColor");
        OpenGLUtils.drawPoints(this.properties.getStarsCount(), verticesHandle);
    }

    @Override
    public void onFinishFrame(final Viewport viewport) {
        // Do nothing.
    }

    @Override
    public void onSurfaceChanged(final int width, final int height) {
        // Do nothing.
    }

    @Override
    public void onRendererShutdown() {
        // Do nothing.
    }

    final long getTimeBetweenFrames() {
        return this.timeBetweenFrames;
    }

    final SpaceProperties getProperties() {
        return this.properties;
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
            Log.e(TAG, "Unable to read the content of " + fd);
        }
        return contentSb.toString();
    }

    private void initializeStars() {
        final int starsCount = this.properties.getStarsCount();
        starsVertices = this.createFloatBuffer(starsCount * DIMENSION * FLOAT_BYTES);
        final float minX = -this.properties.getStarsXBound();
        final float maxX = this.properties.getStarsXBound();
        final float minY = -this.properties.getStarsYBound();
        final float maxY = this.properties.getStarsYBound();
        final float front = this.properties.getStarsZBound();
        final float back = -this.properties.getStarsZBound();

        for (int i = 0; i < starsCount / 2; i++) {
            starsVertices.put(MathUtils.randRange(minX, maxX));
            starsVertices.put(MathUtils.randRange(minY, maxY));
            starsVertices.put(front);

            starsVertices.put(MathUtils.randRange(minX, maxX));
            starsVertices.put(MathUtils.randRange(minY, maxY));
            starsVertices.put(back);
        }
        starsVertices.position(0);
    }

    FloatBuffer createFloatBuffer(final int size) {
        final ByteBuffer bb = ByteBuffer.allocateDirect(size);
        bb.order(ByteOrder.nativeOrder());
        return bb.asFloatBuffer();
    }
}
