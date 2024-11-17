package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.message.models.RocketMessageModel;

import java.util.Random;

public class RocketInOrbit extends Rocket {
    public float cooldown;

    public RocketInOrbit(final TextureAtlas textureAtlas, RocketMessageModel rocketMessageModel, final Planet orbitingPlanet, final float cooldown) {
        this(textureAtlas, rocketMessageModel, orbitingPlanet);
        this.cooldown = cooldown;
    }

    public RocketInOrbit(final TextureAtlas textureAtlas, RocketMessageModel rocketMessageModel, final Planet orbitingPlanet) {
        super(textureAtlas, rocketMessageModel);
        this.orbitingPlanet = orbitingPlanet;


        Random rand = new Random();
        this.orbitalRadius = (this.orbitingPlanet.planetSprite.getWidth() / 2f) + rand.nextFloat(ROCKET_ORBITAL_RADIUS);
        this.orbitalPosition = rand.nextFloat(TWO_PI);
        this.orbitalDirection = rand.nextInt(2) == 0 ? ORBITAL_DIRECTION.COUNTER_CLOCKWISE : ORBITAL_DIRECTION.CLOCKWISE;
        this.cooldown = 0f;
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

            if (this.cooldown > 0f) {
                this.cooldown -= (deltaTime * speedMultiplier);
            }
        }
    }
}
