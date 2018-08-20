package com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl;

import android.opengl.Matrix;

import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;

/**
 * The Camera matrix used for the OpenGL program on a 3dimension system.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class CameraMatrix extends Dim4Matrix {
    private final Coordinates up;
    private Coordinates position;
    private Coordinates lookAt;

	/**
	 * Create a new Camera at the given position which look to (0,0,0).
	 *
	 * @param position the position of the wanted camera.
	 */
	public CameraMatrix(final Coordinates position) {
		super();
        this.position = position;
        this.up = new Coordinates(0f, 1f, 0f);
        this.lookAt = new Coordinates();
        this.recalculate();
	}

	/**
	 * Set the direction looked at by this camera.
	 *
	 * @param target the target point looked by this camera.
	 */
	public void setLookAt(final Coordinates target) {
		this.lookAt = target;
		this.recalculate();
	}

	/**
	 * Move the camera on the depth axis with the given speed.
	 *
	 * @param speed the speed used to move the camera.
	 */
	public void move(final float xDistance, final float yDistance, final float zDistance) {
        this.position.x += xDistance;
        this.position.y += yDistance;
        this.position.z += zDistance;
		this.recalculate();
	}

	private void recalculate() {
		final Coordinates target = new Coordinates(
				this.position.x + this.lookAt.x,
				this.position.y + this.lookAt.y,
				this.position.z + this.lookAt.z
		);
        Matrix.setLookAtM(this.values, 0,
                this.position.x, this.position.y, this.position.z,
				target.x, target.y, target.z,
                this.up.x, this.up.y, this.up.z);
    }
}
