package com.github.an0rakdev.planetaryconquest.graphics.shaders.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.graphics.models.Model;
import com.github.an0rakdev.planetaryconquest.graphics.models.lines.Lines;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.ShadersType;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.DrawType;
import com.github.an0rakdev.planetaryconquest.graphics.shaders.opengl.VROpenGLProgram;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

public class VRLineProgram extends VROpenGLProgram<Lines> {

    private final float lineWidth;

    /**
     * Create a new VRLineProgram which will draw with the given type.
     *
     * @param context   the Android context used to read shaders raw file.
     * @param cameraPos the initial position of the Camera in this program.
     * @see DrawType
     */
    public VRLineProgram(Context context, Coordinates cameraPos, final float lineWidth) {
        super(context, cameraPos, DrawType.LINES);

        this.compile(R.raw.mvp_vertex, ShadersType.VERTEX);
        this.compile(R.raw.simple_fragment, ShadersType.FRAGMENT);
        this.lineWidth = lineWidth;
    }

    @Override
    protected void render(Lines shape) {
        this.setLineWidth(shape.width());
        this.attachBuffer("vPosition", shape.bufferize(), 3, 3 * Model.FLOAT_BYTE_SIZE);
        this.attachValues("vColor", shape.color());
        this.attachMatrix("uMatrix", this.getViewProjection().values(), 4);
    }
}
