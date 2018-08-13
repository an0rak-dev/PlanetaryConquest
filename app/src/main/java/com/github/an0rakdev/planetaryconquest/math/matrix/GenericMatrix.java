package com.github.an0rakdev.planetaryconquest.math.matrix;

import android.opengl.Matrix;

/**
 * The base class for all the matrix operations.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class GenericMatrix {
	protected final float values[];

	/**
	 * Create a new, identity, matrix which can be on several dimensions.
	 *
	 * @param nbLin the number of lines in the Matrix.
	 * @param nbCol the number of columns in the Matrix.
	 */
	GenericMatrix(final int nbLin, final int nbCol) {
		this.values = new float[nbLin * nbCol];
		this.reset();
	}

	/**
	 * Create a new Matrix which is a copy of the given values (column values are
	 * consecutive).
	 *
	 * @param values the wanted values in the matrix.
	 */
	GenericMatrix(final float[] values) {
		this.values = values;
	}

	/**
	 * Return the current values of this matrix with columns values consecutives.
	 * <p>
	 * For exemple this method called on :
	 * <pre>
	 *     | 0 4 |
	 *     | 1 5 |
	 * </pre>
	 * will return <code>{0, 1, 4, 5}</code>.
	 *
	 * @return the values of this matrix.
	 */
	public final float[] values() {
		return this.values;
	}

	/**
	 * Multiply the two given matrix and set the result in this one.
	 *
	 * @param m1 the left part of the multiplication.
	 * @param m2 the right part of the multiplication.
	 */
	public final void multiply(final GenericMatrix m1, final GenericMatrix m2) {
		Matrix.multiplyMM(this.values, 0, m1.values, 0, m2.values, 0);
	}

	/**
	 * Change the values of this Matrix to make it Identity given its dimension.
	 */
	public void reset() {
		Matrix.setIdentityM(this.values, 0);
	}
}
