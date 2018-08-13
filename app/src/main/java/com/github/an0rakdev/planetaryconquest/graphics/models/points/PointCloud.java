package com.github.an0rakdev.planetaryconquest.graphics.models.points;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.FloatBuffer;

/**
 * PointCloud (points cloud) model for the space demo.
 *
 * @author Sylvain Nieuwlandt
 */
public final class PointCloud extends Points {
    private FloatBuffer vertices;
    private int size;

	/**
	 * Create the white points cloud of size count in the given bounds.
	 *
	 * Note that the <code>frontZ</code> and <code>backZ</code> params
	 * are used as absolute Z coordinates, the stars will be generated on
	 * <code>frontZ</code> or <code>backZ</code> coordinates, not in the
	 * middle of it. This also means that each depth (<code>frontZ</code>
	 * and <code>backZ</code>) will have <code>count</code> stars.
	 *
	 * @param count the number of stars per depth to include in this model.
	 * @param maxX the x axis bounds (created between [-maxX; maxX]).
	 * @param maxY the y axis bounds (created between [-maxY; maxY])
	 * @param frontZ the front bound in the 3D space.
	 * @param backZ the back bound in the 3D space.
	 */
    public PointCloud(final int count, final float maxX, final float maxY,
					  final float frontZ, final float backZ) {
        super();
        this.size = count;
        this.color = Color.WHITE;
		this.generate(maxX, maxY, frontZ, backZ);
    }

	@Override
    public FloatBuffer bufferize() {
        this.vertices.position(0);
        return this.vertices;
    }

    @Override
    public int size() {
        return this.size;
    }

	private void generate(final float maxX, final float maxY, final float frontZ, final float backZ) {
    	this.vertices = this.createFloatBuffer(this.size * 2 * Coordinates.DIMENSION
				* FLOAT_BYTE_SIZE);
		final float dx = 2 * maxX;
		final float dy = 2 * maxY;
		for (int i = 0; i < this.size; i++) {
			float x = -maxX + ((float) Math.random() * dx);
			float y = -maxY + ((float) Math.random() * dy);
			this.vertices.put(x);
			this.vertices.put(y);
			this.vertices.put(frontZ);
			x = -maxX + ((float) Math.random() * dx);
			y = -maxY + ((float) Math.random() * dy);
			this.vertices.put(x);
			this.vertices.put(y);
			this.vertices.put(backZ);
		}
	}
}
