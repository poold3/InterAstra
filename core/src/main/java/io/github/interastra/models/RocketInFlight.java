package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import io.github.interastra.message.models.RocketMessageModel;

public class RocketInFlight extends Rocket {
    public Vector2 destination = new Vector2();
    public float dx;
    public float dy;

    public RocketInFlight(
        final TextureAtlas textureAtlas,
        final RocketMessageModel rocketMessageModel,
        final float worldWidth,
        final float worldHeight,
        final float speedMultiplier,
        final Planet originPlanet,
        final Planet destinationPlanet
    ) {
        super(textureAtlas, rocketMessageModel);
        this.orbitingPlanet = null;
        this.rocketSprite.setCenter(originPlanet.getX(), originPlanet.getY());

        Vector2 targetPosition = new Vector2();
        Vector2 newTargetPosition = new Vector2(destinationPlanet.getX(), destinationPlanet.getY());
        do {
            targetPosition.x = newTargetPosition.x;
            targetPosition.y = newTargetPosition.y;
            float timeToTarget = this.timeToPosition(targetPosition);
            newTargetPosition = destinationPlanet.getPositionInTime(worldWidth, worldHeight, timeToTarget, speedMultiplier);
        } while (this.getDistance(targetPosition, newTargetPosition) > 1f);

        this.destination.x = newTargetPosition.x;
        this.destination.y = newTargetPosition.y;
        float rotationRadians = (float) Math.atan((newTargetPosition.y - originPlanet.getY()) / (newTargetPosition.x - originPlanet.getX()));
        if (newTargetPosition.x - originPlanet.getX() <= 0f) {
            rotationRadians += (float) Math.PI;
        }

        this.rocketSprite.setRotation((float) (rotationRadians * 180f / Math.PI));
        this.dx = (float) (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * Math.cos(rotationRadians));
        this.dy = (float) (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * Math.sin(rotationRadians));
    }

    public void move(float deltaTime, float speedMultiplier) {
        this.rocketSprite.setCenter(this.dx * deltaTime * speedMultiplier, this.dy * deltaTime * speedMultiplier);
    }

    public float getDistance(final Vector2 positionOne, final Vector2 positionTwo) {
        return (float) Math.sqrt(Math.pow(positionOne.x - positionTwo.x, 2) + Math.pow(positionOne.y - positionTwo.y, 2));
    }

    public float timeToPosition(final Vector2 position) {
        float distance = (float) Math.sqrt(Math.pow(this.rocketSprite.getX() - position.x, 2) + Math.pow(this.rocketSprite.getY() - position.y, 2));
        return distance / Rocket.ROCKET_TIER_STATS[this.tier - 1].speed;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}