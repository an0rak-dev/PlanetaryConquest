package com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.Program;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.ShadersType;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The base class of all the OpenGL Shader programs.
 *
 * @param <T> The Model type supported by this Shader program.
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public abstract class OpenGLProgram<T extends Model> extends Program<T> {
    private final DrawType type;
    private final int program;
    private final List<Integer> shaders;
    private final List<Integer> handlersEnabled;
    private boolean linked;

	/**
	 * Create a new OpenGLProgram which will draw with the given type.
	 *
	 * @param context the Android context used to read shaders raw file.
	 * @param cameraPos the initial position of the Camera in this program.
	 * @param type the type of drawing to used for rendering the model.
	 * @throws ShaderException if the OpenGL program can't be created.
	 * @see DrawType
	 */
	OpenGLProgram(final Context context, final Coordinates cameraPos, final DrawType type) {
        super(context, cameraPos);
        this.shaders = new ArrayList<>();
        this.type = type;
        this.linked = false;
        this.handlersEnabled = new ArrayList<>();
        this.program = GLES20.glCreateProgram();
        if (0 == this.program) {
            throw new ShaderException("Can't create a new Shader OpenGLProgram");
        }
    }

    @Override
    protected final void compile(final int shaderFd, final ShadersType type) {
    	if (this.linked) { return; }
        final String source = this.readContentOf(shaderFd);
        if (source.isEmpty()) {
            return;
        }
		final int shader = this.newShaderOf(type);
		if (0 == shader) {
            throw new ShaderException("Can't create the shader of type " + type.name());
        }
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int compileStatus[] = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (GLES20.GL_FALSE == compileStatus[0]) {
            final String msg = "Can't compile the shader of type "
                    + type.name() + " - infos : " + GLES20.glGetShaderInfoLog(shader);
            GLES20.glDeleteShader(shader);
            throw new ShaderException(msg);
        }
        this.shaders.add(shader);
    }

	@Override
    protected final void activate() {
    	if (!this.linked) {
			for (final Integer shader : this.shaders) {
				GLES20.glAttachShader(this.program, shader);
			}
			GLES20.glLinkProgram(this.program);
			final int linkStatus[] = new int[1];
			GLES20.glGetProgramiv(this.program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (GLES20.GL_FALSE == linkStatus[0]) {
				throw new ShaderException("Unable to link the program.");
			}
			// Link ok, cleaning it up.
			for (final Integer shader : this.shaders) {
				GLES20.glDetachShader(this.program, shader);
				GLES20.glDeleteShader(shader);
			}
    		this.linked = true;
		}
		GLES20.glUseProgram(this.program);
	}

	@Override
	public void draw(final T shape) {
		super.draw(shape);
		GLES20.glDrawArrays(this.type.getNativeValue(), 0, shape.size());
		for (final Integer handler : this.handlersEnabled) {
			GLES20.glDisableVertexAttribArray(handler);
		}
		this.handlersEnabled.clear();
	}

	@Override
	protected final void attachBuffer(final String attribName, final Buffer values,
								final int unitSize, final int stride) {
		final int handle = GLES20.glGetAttribLocation(this.program, attribName);
		GLES20.glEnableVertexAttribArray(handle);
		GLES20.glVertexAttribPointer(handle, unitSize, GLES20.GL_FLOAT,
				false, stride, values);
		this.handlersEnabled.add(handle);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Note : this method works only on matrixSize values between [2, 4].
	 */
	@Override
	protected final void attachMatrix(final String attribName, final float[] values,
									  final int matrixSize) {
		final int mvpMatrixHandle = GLES20.glGetUniformLocation(this.program, attribName);
		if (2 == matrixSize) {
			GLES20.glUniformMatrix2fv(mvpMatrixHandle, 1, false, values, 0);
		} else if (3 == matrixSize) {
			GLES20.glUniformMatrix3fv(mvpMatrixHandle, 1, false, values, 0);
		} else if (4 == matrixSize) {
			GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, values, 0);
		} else {
			Log.w("OpenGLProgram", "Can't attach matrix with size " + matrixSize);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Note : This implementations only works on 1, 2, 3 or 4 values only.
	 */
	@Override
	protected final void attachValues(final String attribName, final float... values) {
		final int handle = GLES20.glGetUniformLocation(this.program, attribName);
		if (1 == values.length) {
			GLES20.glUniform1f(handle, values[0]);
		} else if (2 == values.length) {
			GLES20.glUniform2fv(handle,1, values, 0);
		} else if (3 == values.length) {
			GLES20.glUniform3fv(handle,1, values, 0);
		} else if (4 == values.length) {
			GLES20.glUniform4fv(handle,1, values, 0);
		} else {
			Log.w("OpenGLProgram", "Too much values to attach : " + values.length);
		}
	}

	@Override
	protected final void setLineWidth(final float width) {
		GLES20.glLineWidth(width);
	}

	private int newShaderOf(final ShadersType type) {
		int shader;
		switch (type) {
			case VERTEX :
				shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
				break;
			case FRAGMENT:
				shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
				break;
			default:
				shader = 0;
				break;
		}
		return shader;
	}
}
