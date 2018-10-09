/* ****************************************************************************
 * FrameCounter.java
 *
 * Copyright © 2018 by Sylvain Nieuwlandt
 * Released under the MIT License (which can be found in the LICENSE.md file)
 *****************************************************************************/
package com.github.an0rakdev.planetaryconquest;

import android.os.SystemClock;
import android.util.Log;

/**
 * A Frame counter which logs the frame count given a regular clock.
 *
 * @author Sylvain Nieuwlandt
 * @version 1.0
 */
public class FrameCounter {
    private long lastTimer;
    private final long threshold;
    private int frameCount;

    /**
     * Create a new Frame counter for the counting the number of frame per the given threshold.
     *
     * @param thrshld the threshold used as a single "unit" in milliseconds.
     */
    public FrameCounter(final long thrshld) {
        this.lastTimer = 0L;
        this.frameCount = 0;
        this.threshold = thrshld;
    }

    /**
     * Increase the number of frames in the current lap of one.
     */
    public void increase() {
        this.frameCount++;
    }

    /**
     * If the threshold of this counter is reached, then log in Info the Frame count and reset it.
     */
    public void log() {
        final long now = SystemClock.currentThreadTimeMillis();
        if ((now - this.lastTimer) >= this.threshold) {
            Log.i("FrameCounter", this.frameCount + "frames in "
                    + (float)this.threshold / 1000f + "s.");
            this.lastTimer = now;
            this.frameCount = 0;
        }
    }
}
