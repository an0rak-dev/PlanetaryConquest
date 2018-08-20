package com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl;

import android.content.Context;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;
import com.github.an0rakdev.planetaryconquest.math.matrix.Dim4Matrix;
import com.github.an0rakdev.planetaryconquest.math.matrix.GenericMatrix;
import com.google.vr.sdk.base.Eye;

/**
 * This class is a subclass of OpenGLProgram dedicated to VR rendering.
 *
 * @param <T> The Model type supported by this Shader program.
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class VROpenGLProgram<T extends Model> extends OpenGLProgram<T> {
	private final GenericMatrix mvpMatrix;
	private final GenericMatrix translationMatrix;

	/**
	 * Create a new VROpenGLProgram which will draw with the given type.
	 *
	 * @param context   the Android context used to read shaders raw file.
	 * @param cameraPos the initial position of the Camera in this program.
	 * @param type      the type of drawing to used for rendering the model.
	 * @throws ShaderException if the OpenGL program can't be created.
	 * @see DrawType
	 */
	public VROpenGLProgram(final Context context, final Coordinates cameraPos, final DrawType type) {
		super(context, cameraPos, type);
		this.mvpMatrix = new Dim4Matrix();
		this.translationMatrix = new Dim4Matrix();
	}

	/**
	 * Adapt the current View-Projection matrix to the given Eye.
	 *
	 * @param eye the eye to use as Projection.
	 */
	public void adaptToEye(final Eye eye) {
		this.mvpMatrix.reset();
		final GenericMatrix realView = new Dim4Matrix();
		final GenericMatrix eyeView = new Dim4Matrix(eye.getEyeView());
		final GenericMatrix eyePerspective = new  Dim4Matrix(eye.getPerspective(0.1f, 100f));
		realView.multiply(this.translationMatrix, this.getCamera());
		realView.multiply(eyeView, realView);
		this.mvpMatrix.multiply(eyePerspective, realView);
	}

	/**
	 * Return the current View-Projection matrix.
	 *
	 * @return the MVP matrix.
	 */
	protected final GenericMatrix getViewProjection() {
		return this.mvpMatrix;
	}


	public void move(final float xDistance, final float yDistance, final float zDistance) {
		this.translationMatrix.translate(xDistance, yDistance, zDistance);
	}
}
