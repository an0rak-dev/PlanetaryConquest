/* ****************************************************************************
 * MathUtils.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.adoc file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.geometry.Coordinates;
import com.github.an0rakdev.planetaryconquest.graphics.models.Laser;
import com.github.an0rakdev.planetaryconquest.geometry.Sphere;

public class MathUtils {
    public static final int FLOAT_BYTES = (Float.SIZE / Byte.SIZE);
    public static final int DIMENSION = 3;

    public static float randRange(final float min, final float max) {
        return min + (float) Math.random() * (max - min);
    }

    public static float horizontalAngleOf(Coordinates coord) {
        return MathUtils.horizontalAngleOf(MathUtils.convertPositionToMatrix(coord));
    }

    public static float horizontalAngleOf(float[] coordsModel) {
        return (float) Math.atan2(getX(coordsModel), Math.abs(getZ(coordsModel)));
    }

    public static float verticalAngleOf(Coordinates coord) {
        return MathUtils.verticalAngleOf(MathUtils.convertPositionToMatrix(coord));
    }

    public static float verticalAngleOf(float[] coordsModel) {
        return (float) Math.atan2(getY(coordsModel), Math.abs(getZ(coordsModel)));
    }

    public static float horizontalAngleBetween(final float[] v1, final float[] v2) {
        float deltaX = getX(v1) - getX(v2);
        float deltaY = getY(v1) - getY(v2);
        float deltaZ = getZ(v1) - getZ(v2);
        float distance = (float)Math.sqrt(deltaY * deltaY + deltaZ * deltaZ);

        return (float) Math.atan2(deltaX, distance);
    }

    public static float verticalAngleBetween(float[] v1, float[] v2) {
        float deltaX = getX(v1) - getX(v2);
        float deltaY = getY(v1) - getY(v2);
        float deltaZ = getZ(v1) - getZ(v2);
        float distance = (float)Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        return (float) Math.atan2(deltaY, distance);
    }

    public static float[] convertPositionToMatrix(final Coordinates coord) {
        final float[] model = new float[16];
        Matrix.setIdentityM(model, 0);
        Matrix.translateM(model, 0, coord.x, coord.y, coord.z);
        return model;
    }

    public static boolean collide(Laser laser, Sphere shape) {
        final float[] laserModel = laser.model();
        final float[] shapeModel = shape.model();
        final float left = MathUtils.getX(shapeModel) - shape.getRadius();
        final float right = MathUtils.getX(shapeModel) + shape.getRadius();
        final float up = MathUtils.getY(shapeModel) + shape.getRadius();
        final float down = MathUtils.getY(shapeModel) - shape.getRadius();
        final float forward = MathUtils.getZ(shapeModel) - shape.getRadius();

        return left <= MathUtils.getX(laserModel) && MathUtils.getX(laserModel) <= right
                && down <= MathUtils.getY(laserModel) && MathUtils.getY(laserModel) <= up
                && forward <= MathUtils.getZ(laserModel);
    }

    public static float getX(float[] matrix) {
        return matrix[12];
    }

    public static float getY(float[] matrix) {
        return matrix[13];
    }

    public static float getZ(float[] matrix) {
        return matrix[14];
    }
}
