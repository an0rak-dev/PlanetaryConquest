package com.github.an0rakdev.planetaryconquest.graphics.shaders.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.points.Points;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.ShadersType;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.DrawType;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.VROpenGLProgram;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

/**
 * A VR program implementation using OpenGL and Google's Cardboard SDK dedicated
 * to points rendering.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class VRPointProgram extends VROpenGLProgram<Points> {
	private final int pointSize;

	/**
	 * Create a new VRPointProgram with the given Android's context and the given points
	 * size and set the camera's position.
	 *
	 * @param context the Android's context used.
	 * @param cameraPos the initial camera's position.
	 * @param pointSize the size to use when drawing the points.
	 */
    public VRPointProgram(Context context, final Coordinates cameraPos, int pointSize) {
        super(context, cameraPos, DrawType.POINTS);
        this.pointSize = pointSize;
		this.compile(R.raw.point_vertex, ShadersType.VERTEX);
		this.compile(R.raw.simple_fragment, ShadersType.FRAGMENT);
    }

	@Override
	protected void render(final Points shape) {
		// Apply the model's vertices and the background
		this.attachBuffer("vPosition", shape.bufferize(), 3, 3);
		this.attachMatrix("vMatrix", this.getViewProjection().values(), 4);
		this.attachValues("vColor", shape.color());
		this.attachValues("vPointSize", this.pointSize);
	}
}
