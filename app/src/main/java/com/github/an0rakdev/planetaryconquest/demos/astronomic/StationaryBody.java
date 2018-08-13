package com.github.an0rakdev.planetaryconquest.demos.astronomic;

import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Sphere;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

/**
 * An astronomic, stationary, body.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class StationaryBody {
	private Polyhedron model;

	/**
	 * Create a new stationary body with the given configuration.
	 *
	 * @param config the configuration of the new body.
	 */
	public StationaryBody(final Configuration config) {
		final Coordinates center = new Coordinates(
				config.getXPosition(),
				config.getYPosition(),
				config.getZPosition()
		);
		this.model = new Sphere(center, config.getSize());
		this.model.precision(3);
		this.model.background(config.getColor());
	}

	/**
	 * The 3D model to render this body.
	 *
	 * @return the 3D model.
	 */
	public Polyhedron model() {
		return this.model;
	}
}
