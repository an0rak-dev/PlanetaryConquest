package com.github.an0rakdev.planetaryconquest.graphics.models.dim3;

import com.github.an0rakdev.planetaryconquest.graphics.Color;
import com.github.an0rakdev.planetaryconquest.math.Coordinates;

import java.util.ArrayList;
import java.util.List;

class Triangle {
    private List<Coordinates> coords;
    private Color color;
    Triangle(final Coordinates c1, final Coordinates c2, final Coordinates c3) {
        this.coords = new ArrayList<>();
        this.coords.add(c1);
        this.coords.add(c2);
        this.coords.add(c3);
        this.color = Color.random(true);
    }

    public List<Coordinates> getCoordinates() {
        return this.coords;
    }

    public Color getColor() {
        return this.color;
    }

    public List<Triangle> split() {
        final List<Triangle> result = new ArrayList<>();
        final Coordinates top = this.coords.get(0);
        final Coordinates right = this.coords.get(1);
        final Coordinates left = this.coords.get(2);
        final Coordinates middleL = this.findMiddle(top, left);
        final Coordinates middleR = this.findMiddle(top, right);
        final Coordinates middleB = this.findMiddle(left, right);
        result.add(new Triangle(top, middleR, middleL));
        result.add(new Triangle(middleR, right, middleB));
        result.add(new Triangle(middleL, middleR, middleB));
        result.add(new Triangle(middleL, middleB, left));
        return result;
    }

    private Coordinates findMiddle(final Coordinates c1, final Coordinates c2) {
        return new Coordinates(
                this.findMiddlePoint(c1.x, c2.x),
                this.findMiddlePoint(c1.y, c2.y),
                this.findMiddlePoint(c1.z, c2.z));
    }

    private float findMiddlePoint(final float p1, final float p2) {
        if (p1 > p2) {
            return p1 - ((p1 - p2) / 2);
        } else {
            return p2 - ((p2 - p1) / 2);
        }
    }
}
