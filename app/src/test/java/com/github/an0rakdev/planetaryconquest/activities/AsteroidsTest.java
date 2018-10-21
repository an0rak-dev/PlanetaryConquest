package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.AsteroidsRenderer;
import com.github.an0rakdev.planetaryconquest.renderers.FlyingRenderer;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AsteroidsTest {
    private Asteroids cut;

    @Before
    public void setUp() {
        this.cut = Mockito.spy(new Asteroids());
    }

    @Test
    public void onCreate_should_set_the_gvrview() {
        // Given
        final GvrView fakeGvrView = Mockito.mock(GvrView.class);
        Mockito.doReturn(fakeGvrView).when(this.cut).findViewById(R.id.asteroids_gvr_view);
        ArgumentCaptor<GvrView.StereoRenderer> rendererUsed = ArgumentCaptor.forClass(GvrView.StereoRenderer.class);
        // When
        this.cut.onCreate(Mockito.any(Bundle.class));
        // Then
        Mockito.verify(fakeGvrView).setRenderer(rendererUsed.capture());
        Assert.assertTrue(AsteroidsRenderer.class.isAssignableFrom(rendererUsed.getValue().getClass()));
        Assert.assertEquals(fakeGvrView, this.cut.getGvrView());
    }

    @Test
    public void onPause_should_pause_the_renderer() {
        // Given
        AsteroidsRenderer mockRenderer = Mockito.mock(AsteroidsRenderer.class);
        this.cut.renderer = mockRenderer;
        // When
        this.cut.onPause();
        // Then
        Mockito.verify(mockRenderer).pause();
    }

    @Test
    public void onResume_should_resume_the_renderer() {
        // Given
        AsteroidsRenderer mockRenderer = Mockito.mock(AsteroidsRenderer.class);
        this.cut.renderer = mockRenderer;
        // When
        this.cut.onResume();
        // Then
        Mockito.verify(mockRenderer).resume();
    }
}
