package com.github.an0rakdev.planetaryconquest.geometry;

public class Line {
    private Coordinates begin;
    private Coordinates end;

    Line(Coordinates a, Coordinates b) {
        this.begin = a;
        this.end = b;
    }

    public Coordinates middle() {
        return new Coordinates(this.middleOf(this.begin.x, this.end.x),
                this.middleOf(this.begin.y, this.end.y),
                this.middleOf(this.begin.z, this.end.z));
    }

    private float middleOf(final float p1, final float p2) {
        final float a = (p1 > p2) ? p1 : p2;
        final float b = (p1 > p2) ? p2 : p1;
        return a - ((a - b) / 2);
    }
}
