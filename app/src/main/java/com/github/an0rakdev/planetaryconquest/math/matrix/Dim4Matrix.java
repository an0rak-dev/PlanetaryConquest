package com.github.an0rakdev.planetaryconquest.math.matrix;

/**
 * A 4 dimension specific matrix.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class Dim4Matrix extends GenericMatrix {
	/**
	 * Create a new, identity, matrix on 4 dimensions.
	 */
	public Dim4Matrix() {
		super(4, 4);
	}

	/**
	 * Create a new 4 dimension matrix with the given values.
	 * <p>
	 * If the given values doesn't correspond to a 4 dimension matrix, then
	 * throws an IllegalArgumentException.
	 *
	 * @param values the values of the new Matrix.
	 * @throws IllegalArgumentException if the size of the array isn't 16.
	 */
	public Dim4Matrix(final float[] values) {
		super(values);
		if (16 != values.length) {
			throw new IllegalArgumentException("The matrix dimension should be 4*4");
		}
	}
}
