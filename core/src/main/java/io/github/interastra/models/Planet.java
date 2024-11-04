package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.message.models.PlanetMessageModel;
import io.github.interastra.message.models.PlanetResourceMessageModel;

import java.util.ArrayList;

public class Planet implements CameraEnabledEntity, Comparable<Planet> {
    public static final float TWO_PI = (float) (2f * Math.PI);

    public int index;
    public String name;
    public Sprite planetSprite;
    public float orbitalRadius;
    public float orbitalSpeed;
    public float orbitalPosition;
    public Moon moon = null;
    public int baseLimit;
    public ArrayList<PlanetResource> resources = new ArrayList<>();

    public Planet(final TextureAtlas textureAtlas, PlanetMessageModel planetMessageModel) {
        this.index = planetMessageModel.index();
        this.name = planetMessageModel.name();
        this.planetSprite = new Sprite(textureAtlas.findRegion("planet", this.index));
        float size = planetMessageModel.size();
        this.baseLimit = planetMessageModel.baseLimit();
        this.planetSprite.setSize(size, size);
        this.planetSprite.setOrigin(size / 2f, size / 2f);
        this.orbitalRadius = planetMessageModel.orbitalRadius();
        this.orbitalSpeed =  planetMessageModel.orbitalSpeed();
        this.orbitalPosition = planetMessageModel.startingOrbitalPosition();

        if (planetMessageModel.moon() != null) {
            this.moon = new Moon(textureAtlas, this, planetMessageModel.moon());
        }

        for (PlanetResourceMessageModel planetResourceMessageModel : planetMessageModel.resources()) {
            this.resources.add(new PlanetResource(planetResourceMessageModel));
        }
    }

    public void move(float width, float height, float deltaTime, float speedMultiplier) {
        this.orbitalPosition += (this.orbitalSpeed * deltaTime * speedMultiplier);
        if (this.orbitalPosition >= TWO_PI) {
            this.orbitalPosition -= TWO_PI;
        }
        float x = this.orbitalRadius * (float) Math.cos(this.orbitalPosition) + (width / 2f);
        float y = this.orbitalRadius * (float) Math.sin(this.orbitalPosition) + (height / 2f);
        this.planetSprite.setCenter(x, y);

        // Move moon as well
        if (this.moon != null) {
            this.moon.move(deltaTime, speedMultiplier);
        }
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

    @Override
    public int compareTo(Planet o) {
        return Float.compare(this.orbitalRadius, o.orbitalRadius);
    }
}
