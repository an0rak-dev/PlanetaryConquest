package com.github.an0rakdev.planetaryconquest.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.github.an0rakdev.planetaryconquest.R;
import com.github.an0rakdev.planetaryconquest.renderers.FlyingRenderer;
import com.google.vr.sdk.base.GvrView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class FlyingTest {
    private Flying cut;

    @Before
    public void setUp() {
        this.cut = Mockito.spy(new Flying());
    }

    @Test
    public void onCreateTest_should_set_the_gvrview() {
        // Given
        final GvrView fakeGvrView = Mockito.mock(GvrView.class);
        Mockito.doReturn(fakeGvrView).when(this.cut).findViewById(R.id.flying_gvr_view);
        ArgumentCaptor<GvrView.StereoRenderer> rendererUsed = ArgumentCaptor.forClass(GvrView.StereoRenderer.class);
        // When
        this.cut.onCreate(Mockito.any(Bundle.class));
        // Then
        Mockito.verify(fakeGvrView).setRenderer(rendererUsed.capture());
        Assert.assertTrue(FlyingRenderer.class.isAssignableFrom(rendererUsed.getValue().getClass()));
        Assert.assertEquals(fakeGvrView, this.cut.getGvrView());
    }
}
