package com.github.an0rakdev.planetaryconquest.graphics.shaders.programs;

import android.content.Context;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.polyhedrons.Polyhedron;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.ShadersType;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.DrawType;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.VROpenGLProgram;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

/**
 * A VR program implementation using OpenGL and Google's Cardboard SDK.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public final class VRProgram extends VROpenGLProgram<Polyhedron> {

	/**
	 * Create a new VRProgram with the given Android's context and set the
	 * camera's position.
	 *
	 * @param context the Android's context used.
	 * @param cameraPosition the initial camera's position.
	 */
    public VRProgram(final Context context, final Coordinates cameraPosition) {
        super(context, cameraPosition, DrawType.TRIANGLES);
        this.compile(R.raw.mvp_vertex, ShadersType.VERTEX);
        this.compile(R.raw.multicolor_fragment, ShadersType.FRAGMENT);
    }

    @Override
	protected void render(final Polyhedron shape) {
		this.attachBuffer("vPosition", shape.bufferize(), 3, 3*Model.FLOAT_BYTE_SIZE);
		this.attachBuffer("vColors", shape.colors(), 4, 0);
		this.attachMatrix("uMatrix", this.getViewProjection().values(), 4);
    }
}
