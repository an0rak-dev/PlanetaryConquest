package com.github.an0rakdev.planetaryconquest.graphics.models;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class StarsModel extends PointBasedModel {
    private List<Coordinates> stars;
    private FloatBuffer vertices;
    public StarsModel(final int starsCount, final float maxX, final float maxY, final float zPos) {
        super();
        this.stars = new ArrayList<Coordinates>();
        this.vertices = null;
        this.color = Color.WHITE;
        final float dx = 2* maxX;
        final float dy = 2* maxY;
        while (this.stars.size() < starsCount) {
            final float x = -maxX + ((float) Math.random() * dx);
            final float y = -maxY + ((float) Math.random() * dy);
            this.stars.add(new Coordinates(x, y, zPos));
        }
    }

    @Override
    public FloatBuffer getVerticesBuffer() {
        if (null == this.vertices) {
            ByteBuffer bb = ByteBuffer.allocateDirect(this.stars.size()
                * Coordinates.DIMENSION * (Float.SIZE) / Byte.SIZE);
            bb.order(ByteOrder.nativeOrder());
            this.vertices = bb.asFloatBuffer();
            for (final Coordinates star : this.stars) {
                this.vertices.put(star.x);
                this.vertices.put(star.y);
                this.vertices.put(star.z);
            }
        }
        this.vertices.position(0);
        return this.vertices;
    }

    @Override
    public int getNbOfElements() {
        return this.stars.size();
    }
}
