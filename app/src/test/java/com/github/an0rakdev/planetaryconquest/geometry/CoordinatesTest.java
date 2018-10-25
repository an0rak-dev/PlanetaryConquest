package com.github.an0rakdev.planetaryconquest.geometry;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class CoordinatesTest {
    private Coordinates cut;

    @Before
    public void setUp() {
        this.cut = new Coordinates();
    }

    @Test
    public void default_ctor_should_set_values_to_origin() {
        // When
        this.cut = new Coordinates();
        // Then
        Assertions.assertThat(this.cut.x).isEqualTo(0f);
        Assertions.assertThat(this.cut.y).isEqualTo(0f);
        Assertions.assertThat(this.cut.z).isEqualTo(0f);
    }

    @Test
    public void ctor_should_set_values_to_specified_location() {
        // Given
        float wantedX = 1f;
        float wantedY = -3f;
        float wantedZ = 8f;
        // When
        this.cut = new Coordinates(wantedX, wantedY, wantedZ);
        // Then
        Assertions.assertThat(this.cut.x).isEqualTo(wantedX);
        Assertions.assertThat(this.cut.y).isEqualTo(wantedY);
        Assertions.assertThat(this.cut.z).isEqualTo(wantedZ);
    }

    @Test
    public void ctor_should_copy_the_origin_values() {
        // Given
        float wantedX = 1f;
        float wantedY = -5f;
        float wantedZ = 7f;
        this.cut.x = wantedX;
        this.cut.y = wantedY;
        this.cut.z = wantedZ;
        // When
        final Coordinates copy = new Coordinates(this.cut);
        // Then
        Assertions.assertThat(copy.x).isEqualTo(this.cut.x);
        Assertions.assertThat(copy.y).isEqualTo(this.cut.y);
        Assertions.assertThat(copy.z).isEqualTo(this.cut.z);
    }

    @Test
    public void equals_should_return_false_on_null() {
        // Given
        Object o1 = null;
        // When
        boolean b = this.cut.equals(o1);
        // Then
        Assertions.assertThat(b).isFalse();
    }

    @Test
    public void equals_should_return_false_on_different_objects() {
        // Given
        Object o1 = "Not a coordinates";
        // When
        boolean b = this.cut.equals(o1);
        // Then
        Assertions.assertThat(b).isFalse();
    }

    @Test
    public void equals_should_return_true_on_same_values() {
        // Given
        float x = 0f;
        float y = 1f;
        float z = 3f;
        Object o1 = new Coordinates(x,y,z);
        this.cut.x = x;
        this.cut.y = y;
        this.cut.z = z;
        // When
        boolean b = this.cut.equals(o1);
        // Then
        Assertions.assertThat(b).isTrue();
    }

    @Test
    public void equals_should_return_false_on_different_x() {
        // Given
        float x = 0f;
        float y = 1f;
        float z = 3f;
        Object o1 = new Coordinates(x,y,z);
        this.cut.x = x-0.5f;
        this.cut.y = y;
        this.cut.z = z;
        // When
        boolean b = this.cut.equals(o1);
        // Then
        Assertions.assertThat(b).isFalse();
    }

    @Test
    public void equals_should_return_false_on_different_y() {
        // Given
        float x = 0f;
        float y = 1f;
        float z = 3f;
        Object o1 = new Coordinates(x,y,z);
        this.cut.x = x;
        this.cut.y = y-0.5f;
        this.cut.z = z;
        // When
        boolean b = this.cut.equals(o1);
        // Then
        Assertions.assertThat(b).isFalse();
    }

    @Test
    public void equals_should_return_false_on_different_z() {
        // Given
        float x = 0f;
        float y = 1f;
        float z = 3f;
        Object o1 = new Coordinates(x,y,z);
        this.cut.x = x;
        this.cut.y = y;
        this.cut.z = z-0.5f;
        // When
        boolean b = this.cut.equals(o1);
        // Then
        Assertions.assertThat(b).isFalse();
    }

    @Test
    public void hashCode_should_return_same_hash_on_same_values() {
        // Given
        float wantedX = 1f;
        float wantedY = 3f;
        float wantedZ = 5f;
        Coordinates other = new Coordinates(wantedX, wantedY, wantedZ);
        this.cut.x = wantedX;
        this.cut.y = wantedY;
        this.cut.z = wantedZ;
        // When
        final int h1 = this.cut.hashCode();
        final int h2 = other.hashCode();
        // Then
        Assertions.assertThat(h1).isEqualTo(h2);
    }


    @Test
    public void hashCode_should_return_different_hash_on_different_values() {
        // Given
        float wantedX = 1f;
        float wantedY = 3f;
        float wantedZ = 5f;
        Coordinates other = new Coordinates(wantedX, wantedY, wantedZ);
        // When
        final int h1 = this.cut.hashCode();
        final int h2 = other.hashCode();
        // Then
        Assertions.assertThat(h1).isNotEqualTo(h2);
    }
}
