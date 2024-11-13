package io.github.interastra.models;

import com.badlogic.gdx.math.Vector2;
import io.github.interastra.message.messages.AddRocketMessage;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.screens.GameScreen;

import java.util.Timer;
import java.util.TimerTask;

public class RocketInFlight extends Rocket {
    public GameScreen screen;
    public Vector2 destination = new Vector2();
    public float dx;
    public float dy;
    public float destroyTimer;
    public String destinationPlanet;

    public RocketInFlight(
        final GameScreen screen,
        final RocketMessageModel rocketMessageModel,
        final Planet originPlanet,
        final Planet destinationPlanet
    ) {
        super(screen.spaceCraftTextureAtlas, rocketMessageModel);
        this.screen = screen;
        this.orbitingPlanet = null;
        this.rocketSprite.setCenter(originPlanet.getX(), originPlanet.getY());

        Vector2 targetPosition = new Vector2();
        Vector2 newTargetPosition = new Vector2(destinationPlanet.getX(), destinationPlanet.getY());
        do {
            targetPosition.x = newTargetPosition.x;
            targetPosition.y = newTargetPosition.y;
            float timeToTarget = this.timeToPosition(targetPosition, this.screen.speedMultiplier);
            newTargetPosition = destinationPlanet.getPositionInTime(this.screen.gameViewport.getWorldWidth(), this.screen.gameViewport.getWorldHeight(), timeToTarget, this.screen.speedMultiplier, false);
        } while (this.getDistance(targetPosition, newTargetPosition) > destinationPlanet.getWidth() / 2f);

        this.destination = newTargetPosition;
        float rotationRadians = (float) Math.atan((newTargetPosition.y - originPlanet.getY()) / (newTargetPosition.x - originPlanet.getX()));
        if (newTargetPosition.x - originPlanet.getX() <= 0f) {
            rotationRadians += (float) Math.PI;
        }

        this.rocketSprite.setRotation((float) (rotationRadians * 180f / Math.PI) - 90f);
        this.dx = (float) (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * Math.cos(rotationRadians));
        this.dy = (float) (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * Math.sin(rotationRadians));

        this.destinationPlanet = destinationPlanet.name;
        this.destroyTimer = this.timeToPosition(this.destination, this.screen.speedMultiplier);
    }

    public void move(final float deltaTime, final float speedMultiplier) {
        float newX = this.getX() + (this.dx * deltaTime * speedMultiplier);
        float newY = this.getY() + (this.dy * deltaTime * speedMultiplier);
        this.rocketSprite.setCenter(newX, newY);

        this.destroyTimer -= deltaTime;
        if (this.destroyTimer <= 0f) {
            this.destroy();
        }
    }

    public float getDistance(final Vector2 positionOne, final Vector2 positionTwo) {
        return (float) Math.sqrt(Math.pow(positionOne.x - positionTwo.x, 2) + Math.pow(positionOne.y - positionTwo.y, 2));
    }

    public float timeToPosition(final Vector2 position, final float speedMultiplier) {
        float distance = (float) Math.sqrt(Math.pow(this.rocketSprite.getX() - position.x, 2) + Math.pow(this.rocketSprite.getY() - position.y, 2));
        return distance / (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * speedMultiplier);
    }

    public void destroy() {
        this.screen.addRocket(new AddRocketMessage(new RocketMessageModel(this), this.destinationPlanet));
        this.screen.rocketsInFlight.remove(this);
    }
}
