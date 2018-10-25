package com.github.an0rakdev.planetaryconquest.geometry;

import org.junit.Before;
import org.junit.Test;

public class LineTest {
    @Test
    public void middle_should_return_the_middle_between_two_points() {
        // Given
        Coordinates a = new Coordinates(-1, -1, -1);
        Coordinates b = new Coordinates(1,1,1);
        Line line = new Line(a,b);
        // When
        Coordinates middle = line.middle();
        // TODO
    }
}
