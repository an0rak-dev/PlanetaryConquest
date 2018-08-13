package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.content.Context;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.CameraMatrix;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * The Base class of the all the Shader programs.
 *
 * @param <T> the type of model supported by this program.
*  @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class Program<T extends Model> {
	private final Context context;
	private final CameraMatrix camera;

	/**
	 * Creates a new program with the given Android Context and camera position.
	 *
	 * @param context the Android context used to read shaders raw file.
	 * @param cameraPosition the initial position of the Camera in this program.
	 */
	public Program(final Context context, final Coordinates cameraPosition) {
		this.context = context;
		this.camera = new CameraMatrix(cameraPosition);
	}

	/**
	 * Sends the drawing instructions to the GPU for the given shape.
	 *
	 * @param shape the shape to draw.
	 */
	public void draw(final T shape) {
		this.activate();
		this.render(shape);
	}

	/**
	 * @return the Camera used for this Program.
	 */
	public final CameraMatrix getCamera() {
		return this.camera;
	}

	/**
	 * Read the content of the given file descriptor in the Android resources.
	 *
	 * @param fd the file descriptor (like <code>R.raw.something</code>).
	 * @return the content of the file.
	 */
	protected final String readContentOf(final int fd) {
		final InputStream inputStream = this.context.getResources().openRawResource(fd);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder contentSb = new StringBuilder();
		final String lineSep = System.getProperty("line.separator");
		try {
			String line = reader.readLine();
			while (null != line) {
				contentSb.append(line).append(lineSep);
				line = reader.readLine();
			}
			reader.close();
		} catch (final IOException ex) {
			Log.e("ShaderIO", "Unable to read the content of " + fd);
		}
		return contentSb.toString();
	}

	/**
	 * Add the given Shader raw file to the current program.
	 *
	 * @param shaderFd the file descriptor of the shader raw file (like
	 *                 <code>R.raw.some_shader</code>).
	 * @param type the type of the shader added.
	 * @see ShadersType
	 */
	protected abstract void compile(final int shaderFd, final ShadersType type);

	/**
	 * Set the current program has the OpenGL program to use for the next OpenGL operations.
	 *
	 * Note that once this method is called, you can't add more shaders to the inherited program.
	 */
	protected abstract void activate();

	/**
	 * Send the vertices and fragments to the GPU for rendering the given shape.
	 *
	 * @param shape the shape to render
	 */
	protected abstract void render(final T shape);

	/**
	 * Attach the given buffer to the given attribute in the current program.
	 *
	 * @param attribName the shader's attribute which will holds the values.
	 * @param values the values to attach
	 * @param unitSize the size of one data in the buffer (for exemple a Color is on 4 floats
	 *                 so its unitSize is 4).
	 * @param stride the stride bytes size between two different datas.
	 */
	protected abstract void attachBuffer(final String attribName, final Buffer values,
										 final int unitSize, final int stride);

	/**
	 * Attach the given float array as a Matrix for the given attribute in the current program.
	 *
	 * @param attribName the shader's attribute which will holds the values.
	 * @param values the matrix values to attach
	 * @param matrixSize the dimension of the matrix
	 */
	protected abstract void attachMatrix(final String attribName, final float[] values,
										 final int matrixSize);

	/**
	 * Attach the given floats as independent values for the given attribute in the current program.
	 *
	 * @param attribName the shader's attribute which will holds the values.
	 * @param values the values to attach
	 */
	protected abstract void attachValues(final String attribName, final float... values);
}
