package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.OpenGLUtils;
import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.google.vr.sdk.audio.GvrAudioEngine;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import javax.microedition.khronos.egl.EGLConfig;

public class AsteroidsRenderer2 extends SpaceRenderer  {
    private int program;
    private final float[] view;
    private final float[] modelView;
    private final float[] mvp;
    private final float[] camera;
    private Sphere asteroid;

    private final GvrAudioEngine audioEngine;
    private final float[] headQuaternion;
    private final float[] headView;

    public AsteroidsRenderer2(Context context) {
        super(context, new AsteroidsProperties(context));
        this.view = new float[16];
        this.modelView = new float[16];
        this.mvp = new float[16];
        this.camera = new float[16];
        this.audioEngine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
        this.headQuaternion = new float[4];
        this.headView = new float[16];
    }

    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        super.onSurfaceCreated(config);

        this.program = OpenGLUtils.newProgram();
        final String vertexSources = readContentOf(R.raw.mvp_vertex);
        final String fragmentSources = readContentOf(R.raw.multicolor_fragment);
        final int vertexShader = OpenGLUtils.addVertexShaderToProgram(vertexSources, this.program);
        final int fragmentShader = OpenGLUtils.addFragmentShaderToProgram(fragmentSources, this.program);
        OpenGLUtils.linkProgram(this.program, vertexShader, fragmentShader);



        this.asteroid = new Sphere(new Coordinates(4,0,8), 1);
        this.asteroid.precision(1);
        this.asteroid.background(OpenGLUtils.toOpenGLColor(190, 190, 190));
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {
        super.onNewFrame(headTransform);

        final AsteroidsProperties config = (AsteroidsProperties) getProperties();
        final float cameraPosX = config.getCameraPositionX();
        final float cameraPosY = config.getCameraPositionY();
        final float cameraPosZ = config.getCameraPositionZ();
        final float cameraDirX = config.getCameraDirectionX();
        final float cameraDirY = config.getCameraDirectionY();
        final float cameraDirZ = config.getCameraDirectionZ();

        Matrix.setLookAtM(this.camera, 0,
                cameraPosX, cameraPosY, cameraPosZ,
                cameraPosX + cameraDirX, cameraPosY + cameraDirY, cameraPosZ + cameraDirZ,
                0, 1, 0);
        headTransform.getHeadView(this.headView, 0);
    }

    @Override
    public void onDrawEye(final Eye eye) {
        super.onDrawEye(eye);
        Matrix.multiplyMM(this.view, 0, eye.getEyeView(), 0, this.camera, 0);

        final float[] asteroidModel = this.convertPositionToMatrix(this.asteroid.getPosition());

        Matrix.multiplyMM(this.modelView, 0, this.view, 0, asteroidModel, 0);
        Matrix.multiplyMM(this.mvp, 0, eye.getPerspective(0.1f, 100f), 0, this.modelView, 0);

        OpenGLUtils.use(this.program);
        OpenGLUtils.bindMVPToProgram(this.program, this.mvp, "vMatrix");

        if (!this.isLookingAt(this.asteroid, eye)) {
            final int verticesHandle = OpenGLUtils.bindVerticesToProgram(this.program, this.asteroid.bufferize(), "vVertices");
            final int colorsHandle = OpenGLUtils.bindColorToProgram(this.program, this.asteroid.colors(), "vColors");
            OpenGLUtils.drawTriangles(this.asteroid.size(), verticesHandle, colorsHandle);
        }
    }

    public void pause() {
        this.audioEngine.pause();
    }

    public void resume() {
        this.audioEngine.resume();
    }

    private boolean isLookingAt(final Sphere shape, final Eye eye) {
        final float sightAngle = this.sightPitch(eye, shape.getPosition().z);
        final float shapeAngle = this.shapePitch(eye, shape);
        return shapeAngle - 0.3f <= sightAngle && sightAngle <= shapeAngle + 0.3f;
    }


    private float sightPitch(Eye eye, float z) {
        // Works. DO NOT TOUCH ANYMORE YOU IDIOT !
        float[] sightModel = this.convertPositionToMatrix(new Coordinates(0, 0, z));
        float[] sightUntouchedModel = this.convertPositionToMatrix(new Coordinates(0, 0, z));
        float[] sightReal = new float[16];

        Matrix.multiplyMM(sightReal, 0, this.headView, 0, sightModel, 0);
        Matrix.multiplyMM(sightReal, 0, eye.getPerspective(0.1f, 100f), 0, sightReal, 0);
        return this.pitchBetween(sightUntouchedModel, sightReal);
    }

    private float shapePitch(Eye eye, Polyhedron shape) {
        float[] shapeModel = this.convertPositionToMatrix(shape.getPosition());
        float[] shapeReal = new float[16];
        Matrix.multiplyMM(shapeReal, 0, this.headView, 0, shapeModel, 0);
        Matrix.multiplyMM(shapeReal, 0, eye.getPerspective(0.1f, 100f), 0, shapeReal, 0);
        return (float) Math.atan2(shapeReal[12], -shapeReal[14]);
    }

    private float[] convertPositionToMatrix(final Coordinates coord) {
        // Works. DO NOT TOUCH ANYMORE YOU IDIOT !
        final float[] model = new float[16];
        Matrix.setIdentityM(model, 0);
        Matrix.translateM(model, 0, coord.x, coord.y, coord.z);
        return model;
    }

    private float pitchBetween(final float[] v1, final float[] v2) {
        // Works. DO NOT TOUCH ANYMORE YOU IDIOT !
        final float deltaX = v1[12] - v2[12];
        final float deltaZ = v1[14] - v2[14];
        return (float) Math.atan2(deltaX, deltaZ);
    }
}
