package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Random;

public class Star implements CameraEnabledEntity {
    public static final int NUM_STARS_AVAILABLE = 3;
    public static final float MAX_STAR_SIZE = 25f;
    public static final float MIN_STAR_SIZE = 15f;

    public int index;
    public String name;
    public Sprite starSprite;

    public Star(final TextureAtlas textureAtlas, String name) {
        Random rand = new Random();
        this.index = rand.nextInt(NUM_STARS_AVAILABLE);
        this.name = name;
        this.starSprite = new Sprite(textureAtlas.findRegion("star", this.index));
        float size = rand.nextFloat(MAX_STAR_SIZE - MIN_STAR_SIZE) + MIN_STAR_SIZE;
        this.starSprite.setSize(size, size);
        this.starSprite.setOrigin(size / 2f, size / 2f);
    }

    public void reposition(float x, float y) {
        this.starSprite.setCenter(x, y);
    }

    @Override
    public float getX() {
        return this.starSprite.getX() + this.starSprite.getOriginX();
    }

    @Override
    public float getY() {
        return this.starSprite.getY() + this.starSprite.getOriginY();
    }

    @Override
    public float getWidth() {
        return this.starSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return this.starSprite.getHeight();
    }
}
