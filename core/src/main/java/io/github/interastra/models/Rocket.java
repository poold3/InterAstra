package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.message.models.RocketMessageModel;

public abstract class Rocket implements CameraEnabledEntity {
    public enum ORBITAL_DIRECTION {
        COUNTER_CLOCKWISE,
        CLOCKWISE
    }

    public static final float[] ROCKET_TIER_HEIGHT = {0.197f, 0.2f, 0.236f, 0.239f};
    public static final float[] ROCKET_TIER_WIDTH = {0.1f, 0.075f, 0.087f, 0.078f};
    public static final Price[] ROCKET_TIER_PRICE = {
        new Price(250f, 500f, 0f, 0f, 0f, 0f),
        new Price(500f, 500f, 0f, 100f, 0f, 0f),
        new Price(750f, 500f, 0f, 100f, 100f, 0f),
        new Price(1000f, 500f, 0f, 100f, 100f, 0f)
    };
    public static final Price[] ROCKET_TIER_FUEL_PRICE = {
        new Price(0f, 0f, 250f, 0f, 0f, 0f),
        new Price(0f, 0f, 500f, 0f, 0f, 0f),
        new Price(0f, 0f, 0f, 0f, 0f, 125f),
        new Price(0f, 0f, 0f, 0f, 0f, 250f)
    };
    public static final String[] ROCKET_TIER_STRING = {"I", "II", "III", "IV"};
    public static final RocketStats[] ROCKET_TIER_STATS = {
        new RocketStats(250f, 0.01f),
        new RocketStats(350f, 0.02f),
        new RocketStats(450f, 0.03f),
        new RocketStats(550f, 0.04f)
    };
    public static final float ROCKET_ORBITAL_RADIUS = 1f;
    public static final float ROCKET_ORBITAL_SPEED = 0.1f;
    public static final float TWO_PI = (float) (2f * Math.PI);

    public String id;
    public String playerName;
    public int tier;
    public Sprite rocketSprite;
    public Planet orbitingPlanet = null;
    public float orbitalRadius;
    public float orbitalPosition;
    public ORBITAL_DIRECTION orbitalDirection;

    public Rocket(final TextureAtlas textureAtlas, RocketMessageModel rocketMessageModel) {
        this.id = rocketMessageModel.id();
        this.playerName = rocketMessageModel.playerName();
        this.tier = rocketMessageModel.tier();

        this.rocketSprite = new Sprite(textureAtlas.findRegion("spaceRockets", this.tier));
        this.rocketSprite.setSize(ROCKET_TIER_WIDTH[this.tier - 1], ROCKET_TIER_HEIGHT[this.tier - 1]);
        this.rocketSprite.setOrigin(ROCKET_TIER_WIDTH[this.tier - 1] / 2f, ROCKET_TIER_HEIGHT[this.tier - 1] / 2f);
    }

    public boolean inOrbit() {
        return this.orbitingPlanet != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() == RocketMessageModel.class) {
            RocketMessageModel otherRocket = (RocketMessageModel) o;
            return this.id.equals(otherRocket.id());
        } else if (o.getClass() == RocketInOrbit.class) {
            RocketInOrbit otherRocket = (RocketInOrbit) o;
            return this.id.equals(otherRocket.id);
        } else if (o.getClass() == RocketInFlight.class) {
            RocketInFlight otherRocket = (RocketInFlight) o;
            return this.id.equals(otherRocket.id);
        }
        return false;
    }

    @Override
    public float getX() {
        return this.rocketSprite.getX() + this.rocketSprite.getOriginX();
    }

    @Override
    public float getY() {
        return this.rocketSprite.getY() + this.rocketSprite.getOriginY();
    }

    @Override
    public float getWidth() {
        return this.rocketSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return this.rocketSprite.getHeight();
    }
}
