package com.github.an0rakdev.planetaryconquest.graphics.shaders;

import android.opengl.GLES20;

class ShaderException extends RuntimeException {
    ShaderException(final String message) {
        super("Shader error : " + message
                + " (OpenGL error code : " + GLES20.glGetError() + ")");
    }
}
