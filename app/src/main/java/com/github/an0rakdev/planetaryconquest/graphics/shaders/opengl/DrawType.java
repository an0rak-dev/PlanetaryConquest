package com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl;

import android.opengl.GLES20;

/**
 * The basic drawing types for OpenGL.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public enum DrawType {
	/**
	 * Draw lines between vertices.
	 */
	LINES(GLES20.GL_LINES),
	/**
	 * Draw only vertices as single points.
	 */
	POINTS(GLES20.GL_POINTS),
	/**
	 * Draw triangles between consecutive group of 3 vertices.
	 */
	TRIANGLES(GLES20.GL_TRIANGLES);

	private final int nativeValue;

	/**
	 * @return the native value of the current type.
	 */
	public int getNativeValue() {
		return this.nativeValue;
	}

	DrawType(final int nativeValue) {
		this.nativeValue = nativeValue;
	}
}
