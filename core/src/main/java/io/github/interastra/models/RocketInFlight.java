package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import io.github.interastra.message.models.RocketMessageModel;

public class RocketInFlight extends Rocket {
    public Vector2 destination;

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


    }

    public void move(float deltaTime, float speedMultiplier) {

    }

    public float getDistance(final Vector2 positionOne, final Vector2 positionTwo) {
        return (float) Math.sqrt(Math.pow(positionOne.x - positionTwo.x, 2) + Math.pow(positionOne.y - positionTwo.y, 2));
    }

    public float timeToPosition(final Vector2 position) {
        float distance = (float) Math.sqrt(Math.pow(this.rocketSprite.getX() - position.x, 2) + Math.pow(this.rocketSprite.getY() - position.y, 2));
        return distance / Rocket.ROCKET_TIER_STATS[this.tier - 1].speed;
    }
}
