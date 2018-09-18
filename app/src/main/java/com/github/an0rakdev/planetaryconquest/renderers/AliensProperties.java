package com.github.an0rakdev.planetaryconquest.renderers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

public class AliensProperties extends SpaceProperties {
    /**
     * Load the file "assets/aliens.properties" and return a new AliensProperties config.
     *
     * @param context the current Android's context.
     */
    AliensProperties(final Context context) {
        super();
        try {
            this.load(context.getAssets().open("aliens.properties"));
        } catch (final IOException ex) {
            Log.w("AliensProperties", "Unable to load the asteroids configuration.");
        }
    }

    public float getMarsXPos() {
        return Float.valueOf(this.getProperty("aliens.mars.position.x", "0"));
    }

    public float getMarsYPos() {
        return Float.valueOf(this.getProperty("aliens.mars.position.y", "0"));
    }

    public float getMarsZPos() {
        return Float.valueOf(this.getProperty("aliens.mars.position.z", "0"));
    }

    public float getMarsRadius() {
        return Float.valueOf(this.getProperty("aliens.mars.radius", "1"));
    }

    public float getMarsRed() {
        return Float.valueOf(this.getProperty("aliens.mars.color.r", "0"));
    }

    public float getMarsGreen() {
        return Float.valueOf(this.getProperty("aliens.mars.color.g", "0"));
    }

    public float getMarsBlue() {
        return Float.valueOf(this.getProperty("aliens.mars.color.b", "0"));
    }

    public float getMovementSpeed() {
        return Float.valueOf(this.getProperty("aliens.movement.speed.mps", "2"));
    }

    public long getDistanceToTravel() {
        return Long.valueOf(this.getProperty("aliens.movement.distance.to.travel", "10"));
    }

    public long getTimeBetweenShips() {
        return Long.valueOf(this.getProperty("aliens.ships.time.between", "10"));
    }

    public long getNumberOfShips() {
        return Long.valueOf(this.getProperty("aliens.ships.count", "1"));
    }

    public float getMinXShipPosition() {
        return Long.valueOf(this.getProperty("aliens.ships.min.position.x", "0"));
    }

    public float getMaxXShipPosition() {
        return Long.valueOf(this.getProperty("aliens.ships.max.position.x", "1"));
    }

    public float getMinYShipPosition() {
        return Long.valueOf(this.getProperty("aliens.ships.min.position.y", "0"));
    }

    public float getMaxYShipPosition() {
        return Long.valueOf(this.getProperty("aliens.ships.max.position.y", "1"));
    }

    public float getMinZShipPosition() {
        return Long.valueOf(this.getProperty("aliens.ships.min.position.z", "0"));
    }

    public float getMaxZShipPosition() {
        return Long.valueOf(this.getProperty("aliens.ships.max.position.z", "1"));
    }

    public float getLaserSpeed() {
        return Float.valueOf(this.getProperty("aliens.ships.laser.speed", "10"));
    }

    public long getLaserCooldown() {
        return Long.valueOf(this.getProperty("aliens.ships.laser.cooldown", "100"));
    }

    public float getShipSize() {
        return Float.valueOf(this.getProperty("aliens.ships.size", "1"));
    }
}
