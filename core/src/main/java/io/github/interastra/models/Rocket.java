package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.message.models.RocketMessageModel;

import java.util.Random;

public class Rocket implements CameraEnabledEntity {
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

    public Rocket(final TextureAtlas textureAtlas, final Planet orbitingPlanet, RocketMessageModel rocketMessageModel) {
        this.id = rocketMessageModel.id();
        this.playerName = rocketMessageModel.playerName();
        this.tier = rocketMessageModel.tier();
        this.orbitingPlanet = orbitingPlanet;

        this.rocketSprite = new Sprite(textureAtlas.findRegion("spaceRockets", this.tier));
        this.rocketSprite.setSize(ROCKET_TIER_WIDTH[this.tier - 1], ROCKET_TIER_HEIGHT[this.tier - 1]);
        this.rocketSprite.setOrigin(ROCKET_TIER_WIDTH[this.tier - 1] / 2f, ROCKET_TIER_HEIGHT[this.tier - 1] / 2f);
        Random rand = new Random();
        this.orbitalRadius = (this.orbitingPlanet.planetSprite.getWidth() / 2f) + rand.nextFloat(ROCKET_ORBITAL_RADIUS);
        this.orbitalPosition = rand.nextFloat(TWO_PI);

        this.orbitalDirection = rand.nextInt(2) == 0 ? ORBITAL_DIRECTION.COUNTER_CLOCKWISE : ORBITAL_DIRECTION.CLOCKWISE;
    }

    public void move(float deltaTime, float speedMultiplier) {
        if (this.inOrbit()) {
            if (this.orbitalDirection == ORBITAL_DIRECTION.COUNTER_CLOCKWISE) {
                this.orbitalPosition += (ROCKET_ORBITAL_SPEED * deltaTime * speedMultiplier);
                if (this.orbitalPosition >= TWO_PI) {
                    this.orbitalPosition -= TWO_PI;
                }
                this.rocketSprite.setRotation((float) (this.orbitalPosition / Math.PI * 180f));
            } else {
                this.orbitalPosition -= (ROCKET_ORBITAL_SPEED * deltaTime * speedMultiplier);
                if (this.orbitalPosition < 0f) {
                    this.orbitalPosition += TWO_PI;
                }
                this.rocketSprite.setRotation((float) ((this.orbitalPosition / Math.PI * 180f) + 180f));
            }

            float x = this.orbitalRadius * (float) Math.cos(this.orbitalPosition) + this.orbitingPlanet.getX();
            float y = this.orbitalRadius * (float) Math.sin(this.orbitalPosition) + this.orbitingPlanet.getY();
            this.rocketSprite.setCenter(x, y);
        }
    }

    public boolean inOrbit() {
        return this.orbitingPlanet != null;
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
