package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Random;

public class Moon implements CameraEnabledEntity {
    public static final float MAX_MOON_SIZE = 1f;
    public static final float MIN_MOON_SIZE = .1f;
    public static final float MOON_ORBITAL_SPEED = 0.05f;
    public static final float TWO_PI = (float) (2f * Math.PI);

    public final Planet planet;
    public String name;
    public Sprite moonSprite;
    public float orbitalRadius;
    public float orbitalSpeed;
    public float orbitalPosition;

    public Moon(final TextureAtlas textureAtlas, final Planet planet) {
        Random rand = new Random();
        this.planet = planet;
        this.name = String.format("%s's Moon", this.planet.name);
        this.moonSprite = new Sprite(textureAtlas.findRegion("moon"));
        float size = rand.nextFloat(MAX_MOON_SIZE - MIN_MOON_SIZE) + MIN_MOON_SIZE;
        this.moonSprite.setSize(size, size);
        this.moonSprite.setOrigin(size / 2f, size / 2f);
        this.orbitalRadius = this.planet.planetSprite.getWidth();
        this.orbitalSpeed = MOON_ORBITAL_SPEED;
        this.orbitalPosition = rand.nextFloat(TWO_PI);
    }

    public void move(float deltaTime, float speedMultiplier) {
        this.orbitalPosition += (this.orbitalSpeed * deltaTime * speedMultiplier);
        if (this.orbitalPosition >= TWO_PI) {
            this.orbitalPosition -= TWO_PI;
        }
        float x = this.orbitalRadius * (float) Math.cos(this.orbitalPosition) + this.planet.getX();
        float y = this.orbitalRadius * (float) Math.sin(this.orbitalPosition) + this.planet.getY();
        this.moonSprite.setCenter(x, y);
    }

    @Override
    public float getX() {
        return this.moonSprite.getX() + this.moonSprite.getOriginX();
    }

    @Override
    public float getY() {
        return this.moonSprite.getY() + this.moonSprite.getOriginY();
    }

    @Override
    public float getWidth() {
        return this.moonSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return this.moonSprite.getHeight();
    }
}
