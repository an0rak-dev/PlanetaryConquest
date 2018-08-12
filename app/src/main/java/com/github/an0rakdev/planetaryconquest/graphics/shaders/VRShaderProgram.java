package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.graphics.models.TriangleBasedModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.perspectives.CameraMatrix;
import com.google.vr.sdk.base.Eye;

public class VRShaderProgram extends MVPShaderProgram {
    private final GenericMatrix mvpMatrix;
    private final CameraMatrix cameraMatrix;

    public VRShaderProgram(Context context) {
        super(context);
        this.mvpMatrix = new Dim4Matrix();
        final Coordinates eye = new Coordinates(0, 0, -2);
        final Coordinates up = new Coordinates(0, 1, 0);
        this.cameraMatrix = new CameraMatrix(4, 4, eye, up);
        this.cameraMatrix.setLookAt(new Coordinates(0f, 0f, 1f));
    }

    @Override
    public void draw(final TriangleBasedModel shape) {
        GLES20.glUseProgram(this.program);
        final int verticesHandle = this.applyVertices(shape);
        final int colorHandle = this.applyColors(shape);
        final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, "vMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
                this.mvpMatrix.getValues(), 0);
        // Draw
        this.render(shape, verticesHandle, colorHandle);
    }

    public void adaptToEye(final Eye eye) {
        this.mvpMatrix.reset();
        final GenericMatrix realView = new Dim4Matrix();
        final GenericMatrix eyeView = new Dim4Matrix(eye.getEyeView());
        final GenericMatrix eyePerspective = new  Dim4Matrix(eye.getPerspective(0.1f, 100f));
        realView.multiply(eyeView, this.cameraMatrix);
        this.mvpMatrix.multiply(eyePerspective, realView);
    }


    public void rotateCamera(final float yAxisAngle) {
        Coordinates position = this.cameraMatrix.getPosition();
        Coordinates oldTarget = this.cameraMatrix.getLookAt();

        float realAngle = yAxisAngle;
        if (yAxisAngle > 180) { realAngle = 360 - yAxisAngle;}
        if (Math.abs(realAngle) <= 45) {
            this.rotateTrigo(realAngle, new Coordinates(position.x, position.y, oldTarget.z));
        } else if (realAngle > 45 && realAngle <= 90) {
            this.rotateTrigo(realAngle - 90, new Coordinates(1, 0, 0));
        } else if (realAngle < -45 && realAngle >= -90) {
            this.rotateTrigo(realAngle + 90, new Coordinates(-1, 0, 0));
        } else if (realAngle > 90 && realAngle <= 180) {
            this.rotateTrigo(realAngle-180, new Coordinates(0,0,-oldTarget.z));
        } else if (realAngle < -90 && realAngle >= -180) {
            this.rotateTrigo(realAngle + 180, new Coordinates(0, 0, -oldTarget.z));
        }
    }

    private void rotateTrigo(final float anglesInDegrees, final Coordinates position) {
        float cos = (float) Math.cos(Math.toRadians(anglesInDegrees));
        Coordinates newTarget = new Coordinates(
                (position.x + cos),
                position.y,
                position.z
        );
        this.cameraMatrix.setLookAt(newTarget);
    }

    public void moveCamera(final float speed) {
        this.cameraMatrix.moveForward(speed);
    }
}
