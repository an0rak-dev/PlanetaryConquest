package com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl;

import android.opengl.GLES20;

/**
 * Exception used for the Shaders used by OpenGL.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
final class ShaderException extends RuntimeException {
    /**
     * Create a new Shader exception with the given message and the OpenGL error code.
     *
     * @param message the message to pass in this exception.
     */
    ShaderException(final String message) {
        super("Shader error : "
                + message
                + " (OpenGL error code : "
                + GLES20.glGetError()
                + ")");
    }
}
