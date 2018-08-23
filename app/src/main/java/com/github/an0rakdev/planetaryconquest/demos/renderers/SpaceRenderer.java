package com.github.an0rakdev.planetaryconquest.demos.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.FrameCounter;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.OpenGLUtils;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public abstract class SpaceRenderer implements GvrView.StereoRenderer {
    protected static final int FLOAT_BYTES = (Float.SIZE / Byte.SIZE);
    protected static final int DIMENSION = 3;
    private static final String TAG = "Renderer";
    private static FloatBuffer starsVertices = null;
    private final float[] starColor;
    private final Context context;
    private final SpaceProperties properties;
    private final FrameCounter frameCounter;
    private int starsShader;
    private final float[] camera;
    private final float[] pos;
    private final float[] view;
    private final float[] model;
    private final float[] modelView;
    private final float[] mvp;
    private final long timeBetweenFrames;

    SpaceRenderer(final Context context, final SpaceProperties properties) {
        this.context = context;
        this.properties = properties;
        this.timeBetweenFrames = (1000 / this.properties.getFps()) - 1L;
        this.frameCounter = new FrameCounter(1000);
        this.camera = new float[16];
        this.view = new float[16];
        this.model = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];
        this.pos = new float[]{0f, 0f, 1f};
        this.starColor = new float[]{1f, 1f, 1f, 1f};
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
        Matrix.setIdentityM(this.model, 0);
        Matrix.translateM(this.model, 0, this.pos[0], this.pos[1], this.pos[2]);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(this.starsShader);

        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);
        Matrix.multiplyMM(this.modelView, 0, this.view, 0, this.model, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.modelView, 0);


        // Pass the vertices to the shader.
        final int verticesHandle = GLES20.glGetAttribLocation(this.starsShader, "vVertices");
        GLES20.glEnableVertexAttribArray(verticesHandle);
        GLES20.glVertexAttribPointer(verticesHandle, DIMENSION, GLES20.GL_FLOAT, false, 3, starsVertices);
        // Pass the MVP to the shader.
        final int mvpHandle = GLES20.glGetUniformLocation(this.starsShader, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, this.mvp, 0);
        // Pass the point size to the shader.
        final int pointSizeHandle = GLES20.glGetUniformLocation(this.starsShader, "vPointSize");
        GLES20.glUniform1f(pointSizeHandle, 4f);
        // Pass the color to the shader.
        final int colorHandle = GLES20.glGetUniformLocation(this.starsShader, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, this.starColor, 0);

        // Draw.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, this.properties.getStarsCount());
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }

    @Override
    public void onFinishFrame(final Viewport viewport) {
        // Do nothing.
    }

    @Override
    public void onSurfaceChanged(final int width, final int height) {
        // Classic one.
    }

    @Override
    public void onRendererShutdown() {
        // Do nothing.
    }

    final long getTimeBetweenFrames() {
        return this.timeBetweenFrames;
    }

    protected final Context getContext() {
        return this.context;
    }

    protected final SpaceProperties getProperties() {
        return this.properties;
    }

    private void initializeStars() {
        final int starsCount = this.properties.getStarsCount();
        starsVertices = this.createFloatBuffer(starsCount * DIMENSION * FLOAT_BYTES);
        final float minX = -this.properties.getStarsXBound();
        final float maxX = this.properties.getStarsXBound();
        final float minY = -this.properties.getStarsYBound();
        final float maxY = this.properties.getStarsYBound();
        final float dx = maxX - minX;
        final float dy = maxY - minY;

        for (int i = 0; i < starsCount / 2; i++) {
            float x = minX + ((float) Math.random() * dx);
            float y = minY + ((float) Math.random() * dy);
            starsVertices.put(x);
            starsVertices.put(y);
            starsVertices.put(3);
            x = minX + ((float) Math.random() * dx);
            y = minY + ((float) Math.random() * dy);
            starsVertices.put(x);
            starsVertices.put(y);
            starsVertices.put(-3);
        }
        starsVertices.position(0);
    }

    private FloatBuffer createFloatBuffer(final int size) {
        final ByteBuffer bb = ByteBuffer.allocateDirect(size);
        bb.order(ByteOrder.nativeOrder());
        return bb.asFloatBuffer();
    }

    private void checkError(final boolean isError, final String errorMessage) {
        if (isError) {
            Log.e(TAG, errorMessage);
            Log.e(TAG, "OpenGL error code : " + GLES20.glGetError());
        }
    }

    private String readContentOf(final int fd) {
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
}
