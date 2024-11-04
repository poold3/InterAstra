package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

public class Rocket implements CameraEnabledEntity{
        public enum ROCKET_STATE {
        ON_GROUND,
        IN_FLIGHT,
        IN_ORBIT_PLANET,
        IN_ORBIT_MOON
    }

    public String id;
    public String playerName;
    public Sprite rocketSprite;

    public Rocket(final String playerName, final Sprite rocketSprite) {
        this.id = UUID.randomUUID().toString();
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != Rocket.class) {
            return false;
        }

        Rocket otherRocket = (Rocket) o;
        return this.id.equals(otherRocket.id);
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }
}
