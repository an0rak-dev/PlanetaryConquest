package com.github.an0rakdev.planetaryconquest.graphics.models.points;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;

/**
 * The base class for all the Points based models.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class Points extends Model {
	/** The uniform background used by the points. */
    protected Color color;

	/**
	 * Create a points model with a default background of Black.
	 */
	Points() {
        this.color = Color.BLACK;
    }

	/**
	 * Convert the current model's background to an array of background components.
	 *
	 * @return the background components (rgba) of the current model's background.
	 */
	public float[] color() {
        final float result[] = new float[4];
        result[0] = this.color.r;
        result[1] = this.color.g;
        result[2] = this.color.b;
        result[3] = this.color.a;
        return result;
    }
}
