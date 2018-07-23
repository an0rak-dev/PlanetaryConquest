package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.google.vr.sdk.base.Eye;

public class VRShaderProgram extends ShaderProgram {
    private float[] view;
    private float[] perspective;

    public VRShaderProgram(Context context) {
        super(context);
        this.view = new float[16];
        Matrix.setIdentityM(this.view, 0);
        this.perspective = new float[16];
        Matrix.setIdentityM(this.perspective, 0);
        this.addShader(R.raw.mvp_vertex, GLES20.GL_VERTEX_SHADER);
        this.addShader(R.raw.simple_fragment, GLES20.GL_FRAGMENT_SHADER);
        this.prepare();
    }

    @Override
    public void draw(final Model shape) {
        GLES20.glUseProgram(this.program);
        float mvpMatrix[] = new float[16];
        Matrix.setIdentityM(mvpMatrix, 0);

        
        // TODO mvp = this.view * shape.vertices
        // TODO full = perspective * mvp
        // TODO draw (based on MVPShaderProgram)
    }

    public void adaptToEye(final Eye eye) {
        this.view = eye.getEyeView();
        this.perspective = eye.getPerspective(0.1f, 100f);
    }
}
