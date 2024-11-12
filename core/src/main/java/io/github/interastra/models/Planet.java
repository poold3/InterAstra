package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import io.github.interastra.message.models.PlanetMessageModel;
import io.github.interastra.message.models.PlanetResourceMessageModel;
import io.github.interastra.message.models.RocketMessageModel;

import java.util.ArrayList;

public class Planet implements CameraEnabledEntity, Comparable<Planet> {
    public static final float TWO_PI = (float) (2f * Math.PI);
    public static final Price BASE_PRICE = new Price(1000f, 1000f, 0f, 250f, 250f, 0f);

    public int index;
    public String name;
    public String myName;
    public boolean isVisible = false;
    public boolean hasMyBase = false;
    public int numMyRockets = 0;
    public Sprite planetSprite;
    public float orbitalRadius;
    public float orbitalSpeed;
    public float orbitalPosition;
    public Moon moon = null;
    public int baseLimit;
    public ArrayList<PlanetResource> resources = new ArrayList<>();
    public ArrayList<String> bases = new ArrayList<>();
    public ArrayList<RocketInOrbit> rocketsInOrbit = new ArrayList<>();

    public Planet(final TextureAtlas planetTextureAtlas,
                  PlanetMessageModel planetMessageModel,
                  final TextureAtlas rocketTextureAtlas,
                  String myName) {
        this.index = planetMessageModel.index();
        this.name = planetMessageModel.name();
        this.myName = myName;
        this.planetSprite = new Sprite(planetTextureAtlas.findRegion("planet", this.index));
        float size = planetMessageModel.size();
        this.baseLimit = planetMessageModel.baseLimit();
        this.planetSprite.setSize(size, size);
        this.planetSprite.setOrigin(size / 2f, size / 2f);
        this.orbitalRadius = planetMessageModel.orbitalRadius();
        this.orbitalSpeed =  planetMessageModel.orbitalSpeed();
        this.orbitalPosition = planetMessageModel.startingOrbitalPosition();

        if (planetMessageModel.moon() != null) {
            this.moon = new Moon(planetTextureAtlas, this, planetMessageModel.moon());
        }

        for (PlanetResourceMessageModel planetResourceMessageModel : planetMessageModel.resources()) {
            this.resources.add(new PlanetResource(planetResourceMessageModel));
        }

        this.bases.addAll(planetMessageModel.bases());

        for (RocketMessageModel rocketMessageModel : planetMessageModel.rocketsInOrbit()) {
            this.rocketsInOrbit.add(new RocketInOrbit(rocketTextureAtlas, rocketMessageModel, this));
        }

        this.setHasMyBase();
        this.setNumMyRockets();
    }

    public void move(float worldWidth, float worldHeight, float deltaTime, float speedMultiplier) {
        Vector2 newPosition = this.getPositionInTime(worldWidth, worldHeight, deltaTime, speedMultiplier);
        this.planetSprite.setCenter(newPosition.x, newPosition.y);

        // Move moon as well
        if (this.moon != null) {
            this.moon.move(deltaTime, speedMultiplier);
        }

        // Move rockets in orbit
        if (this.isVisible) {
            for (RocketInOrbit rocket : this.rocketsInOrbit) {
                rocket.move(deltaTime, speedMultiplier);
            }
        }
    }

    public Vector2 getPositionInTime(float worldWidth, float worldHeight, float deltaTime, float speedMultiplier) {
        Vector2 position = new Vector2();
        this.orbitalPosition += (this.orbitalSpeed * deltaTime * speedMultiplier);
        if (this.orbitalPosition >= TWO_PI) {
            this.orbitalPosition -= TWO_PI;
        }
        position.x = this.orbitalRadius * (float) Math.cos(this.orbitalPosition) + (worldWidth / 2f);
        position.y = this.orbitalRadius * (float) Math.sin(this.orbitalPosition) + (worldHeight / 2f);
        return position;
    }

    public void setHasMyBase() {
        this.hasMyBase = false;
        for (String base : this.bases) {
            if (base.equals(this.myName)) {
                this.hasMyBase = true;
                break;
            }
        }
        this.isVisible = this.hasMyBase || this.numMyRockets > 0;
    }

    public void setNumMyRockets() {
        this.numMyRockets = 0;
        for (Rocket rocket : this.rocketsInOrbit) {
            if (rocket.playerName.equals(this.myName)) {
                this.numMyRockets += 1;
            }
        }
        this.isVisible = this.hasMyBase || this.numMyRockets > 0;
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
