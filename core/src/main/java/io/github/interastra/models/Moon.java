package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.message.models.MoonMessageModel;


public class Moon implements CameraEnabledEntity {
    public static final float TWO_PI = (float) (2f * Math.PI);
    public static final float RANGE_INCREASE = 100f;

    public final Planet planet;
    public String name;
    public Sprite moonSprite;
    public float orbitalRadius;
    public float orbitalSpeed;
    public float orbitalPosition;

    public Moon(final TextureAtlas textureAtlas, final Planet planet, MoonMessageModel moonMessageModel) {
        this.planet = planet;
        this.name = String.format("%s's Moon", this.planet.name);
        this.moonSprite = new Sprite(textureAtlas.findRegion("moon"));
        float size = moonMessageModel.size();
        this.moonSprite.setSize(size, size);
        this.moonSprite.setOrigin(size / 2f, size / 2f);
        this.orbitalRadius = moonMessageModel.orbitalRadius();
        this.orbitalSpeed = moonMessageModel.orbitalSpeed();
        this.orbitalPosition = moonMessageModel.startingOrbitalPosition();
    }

    public void move(float deltaTime) {
        this.orbitalPosition += (this.orbitalSpeed * deltaTime);
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
