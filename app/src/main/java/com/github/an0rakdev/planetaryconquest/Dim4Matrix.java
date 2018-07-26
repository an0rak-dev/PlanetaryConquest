package com.github.an0rakdev.planetaryconquest;

public class Dim4Matrix extends GenericMatrix {
    public Dim4Matrix() {
        super(4, 4);
    }

    public Dim4Matrix(final float[] values) {
        super(values);
        if (values.length != 16) {
            throw new IllegalArgumentException("The values must be a 4x4");
        }
    }
}
