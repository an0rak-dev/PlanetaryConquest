package com.github.an0rakdev.planetaryconquest.graphics.models.dim2;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
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
    protected List<Color> getColorsComponents() {
        final List<Color> result = new ArrayList<>();
        final Color first = new Color(0.538f, 0.124f, 0.822f);
        final Color second = new Color(0.235f, 0.767f, 0.018f);
        result.add(first);
        result.add(first);
        result.add(first);
        result.add(second);
        result.add(second);
        result.add(second);
        return result;
    }
}
