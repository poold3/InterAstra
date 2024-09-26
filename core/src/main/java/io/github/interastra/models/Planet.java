package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.screens.GameScreen;

import java.util.Random;

public class Planet implements CameraEnabledEntity {
    public static final int NUM_PLANETS_AVAILABLE = 11;
    public static final float MAX_PLANET_SIZE = 10f;
    public static final float MIN_PLANET_SIZE = 5f;
    public static final float MAX_ORBITAL_SPEED = 0.00005f;
    public static final float MAX_ORBITAL_RADIUS = (GameScreen.MIN_WORLD_SIZE / 2f) - MAX_PLANET_SIZE;
    public static final float MIN_ORBITAL_RADIUS = 25f;
    public static final float TWO_PI = (float) (2f * Math.PI);

    public int index;
    public Sprite planetSprite;
    public float orbitalRadius;
    public float orbitalSpeed;
    public float orbitalPosition;

    public Planet(final TextureAtlas textureAtlas) {
        Random rand = new Random();
        this.index = rand.nextInt(NUM_PLANETS_AVAILABLE);
        this.planetSprite = new Sprite(textureAtlas.findRegion("planet", this.index));
        float size = rand.nextFloat(MAX_PLANET_SIZE - MIN_PLANET_SIZE) + MIN_PLANET_SIZE;
        this.planetSprite.setSize(size, size);
        this.planetSprite.setOrigin(size / 2f, size / 2f);
        this.orbitalRadius = rand.nextFloat(MAX_ORBITAL_RADIUS - MIN_ORBITAL_RADIUS) + MIN_ORBITAL_RADIUS;
        this.orbitalSpeed =  (1f - (this.orbitalRadius / MAX_ORBITAL_RADIUS)) * MAX_ORBITAL_SPEED;
        this.orbitalPosition = rand.nextFloat(TWO_PI);
    }

    public void move(float width, float height, float speedMultiplier) {
        this.orbitalPosition += (this.orbitalSpeed * speedMultiplier);
        if (this.orbitalPosition >= TWO_PI) {
            this.orbitalPosition -= TWO_PI;
        }
        float x = this.orbitalRadius * (float) Math.cos(this.orbitalPosition) + (width / 2f);
        float y = this.orbitalRadius * (float) Math.sin(this.orbitalPosition) + (height / 2f);
        this.planetSprite.setCenter(x, y);
    }

    @Override
    public float getX() {
        return this.planetSprite.getX() + this.planetSprite.getOriginX();
    }

    @Override
    public float getY() {
        return this.planetSprite.getY() + this.planetSprite.getOriginY();
    }

    @Override
    public float getWidth() {
        return this.planetSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return this.planetSprite.getHeight();
    }
}
