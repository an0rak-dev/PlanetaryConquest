package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.models.MonoColorModel;
import com.github.an0rakdev.planetaryconquest.graphics.models.MultiColorModel;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

public final class Square extends MultiColorModel {
    private final Coordinates center;
    private final float size;

    public Square(final Coordinates center, final float size) {
        this.center = center;
        this.size = size;
    }

    @Override
    protected void calculateCoordonates(final List<Coordinates> coordsToFill) {
        final float half = this.size /2;

        final Coordinates tL = new Coordinates(this.center.x - half, this.center.y + half, this.center.z);
        final Coordinates bR = new Coordinates(this.center.x + half, this.center.y - half, this.center.z);

        coordsToFill.add(tL);
        coordsToFill.add(new Coordinates(this.center.x + half, this.center.y + half, this.center.z));
        coordsToFill.add(bR);
        coordsToFill.add(tL);
        coordsToFill.add(bR);
        coordsToFill.add(new Coordinates(this.center.x - half, this.center.y - half, this.center.z));
    }

    @Override
    protected List<Float> getColorsComponents() {
        final List<Float> result = new ArrayList<>();
        result.add(0.538f);
        result.add(0.124f);
        result.add(0.822f);
        result.add(1f);
        result.add(0.235f);
        result.add(0.767f);
        result.add(0.018f);
        result.add(1f);
        return result;
    }
}
