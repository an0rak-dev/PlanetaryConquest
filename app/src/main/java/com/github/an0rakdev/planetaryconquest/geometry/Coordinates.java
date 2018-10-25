/* ****************************************************************************
 * Coordinates.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.adoc file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest.geometry;

/**
 * A 3dimensional coordinates object.
 *
 * @author  Sylvain Nieuwlandt
 * @version 1.0
 */
public final class Coordinates {
	/** The dimension used by those Coordinates. */
    public static final int DIMENSION = 3;
    /** The horizontal axis position. */
    public float x;
	/** The vertical axis position. */
    public float y;
	/** The depth axis position. */
    public float z;

	/**
	 * Create a new Coordinates object at position <code>(0,0,0)</code>.
	 */
	public Coordinates() {
        this(0,0,0);
    }

	/**
	 * Create a new Coordinates object at the given position.
	 *
	 * @param x the horizontal axis position.
	 * @param y the vertical axis position.
	 * @param z the depth axis position.
	 */
    public Coordinates(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

	/**
	 * Copy constructor, create a new Coordinates with the same values as
	 * the origin.
	 *
	 * @param origin the object with the values to copy.
	 */
	public Coordinates(final Coordinates origin) {
    	this.x = origin.x;
    	this.y = origin.y;
    	this.z = origin.z;
	}

    @Override
    public boolean equals(final Object o) {
    	if (!(o instanceof Coordinates)) {
    		return false;
    	}
    	final Coordinates that = (Coordinates)o;
		return this.x == that.x && this.y == that.y && this.z == that.z;
	}

	@Override
	public int hashCode() {
    	final int prime = 3;
    	int result = (int)this.x;
    	result *= prime;
    	result += (int)this.y;
    	result *= prime;
    	result += (int)this.z;
    	return result;
	}
}
