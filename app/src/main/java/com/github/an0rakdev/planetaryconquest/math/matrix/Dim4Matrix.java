package com.github.an0rakdev.planetaryconquest.math.matrix;

public class Dim4Matrix extends GenericMatrix {
    public Dim4Matrix() {
        super(4,4);
    }

    public Dim4Matrix(final float[] values) {
        super(values);
        if (values.length != 16) {
            throw new IllegalArgumentException("The matrix dimension should be 4*4");
        }
    }
}
