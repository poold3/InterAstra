package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import io.github.interastra.message.messages.AddRocketMessage;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.InterAstraLog;

import java.util.logging.Level;

public class RocketInFlight extends Rocket {
    public GameScreen screen;
    public Vector2 destination = new Vector2();
    public float dx;
    public float dy;
    public float arrivalTimer;
    public Planet destinationPlanet;
    public Sprite propulsionSprite;
    public boolean arrived = false;

    /**
     * Use this constructor for building new rockets.
     * @param screen
     * @param rocketMessageModel
     * @param destinationPlanet
     */
    public RocketInFlight(final GameScreen screen,
                          final RocketMessageModel rocketMessageModel,
                          final Planet destinationPlanet
    ) {
        super(screen.spaceCraftTextureAtlas, rocketMessageModel);
        this.screen = screen;
        this.orbitingPlanet = null;
        this.destinationPlanet = destinationPlanet;
        this.arrival();
    }

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
        this.destinationPlanet = destinationPlanet;

        this.propulsionSprite = new Sprite(screen.spaceCraftTextureAtlas.findRegion("spaceEffects"));
        this.propulsionSprite.setSize(ROCKET_PROPULSION_WIDTH, ROCKET_PROPULSION_HEIGHT);
        this.propulsionSprite.setOrigin(ROCKET_PROPULSION_WIDTH / 2f, ROCKET_PROPULSION_HEIGHT / 2f);
        this.propulsionSprite.setCenter(originPlanet.getX(), originPlanet.getY());

        Vector2 targetPosition = new Vector2();
        Vector2 newTargetPosition = new Vector2(destinationPlanet.getX(), destinationPlanet.getY());
        do {
            targetPosition.x = newTargetPosition.x;
            targetPosition.y = newTargetPosition.y;
            float timeToTarget = this.timeToPosition(targetPosition);
            newTargetPosition = destinationPlanet.getPositionInTime(this.screen.gameViewport.getWorldWidth(), this.screen.gameViewport.getWorldHeight(), timeToTarget, false);
        } while (this.getDistance(targetPosition, newTargetPosition) > destinationPlanet.getWidth() / 2f);

        this.destination = newTargetPosition;
        float rotationRadians = (float) Math.atan((newTargetPosition.y - originPlanet.getY()) / (newTargetPosition.x - originPlanet.getX()));
        if (newTargetPosition.x - originPlanet.getX() <= 0f) {
            rotationRadians += (float) Math.PI;
        }

        this.rocketSprite.setRotation((float) (rotationRadians * 180f / Math.PI) - 90f);
        this.propulsionSprite.setRotation(this.rocketSprite.getRotation());

        // Move propulsion to bottom of rocket.
        float propulsionSpriteTranslateDistance = (this.rocketSprite.getHeight() / 2.5f) + (this.propulsionSprite.getHeight() / 2f);
        float propulsionSpriteTranslateX = (float) (propulsionSpriteTranslateDistance * Math.cos(rotationRadians + Math.PI));
        float propulsionSpriteTranslateY = (float) (propulsionSpriteTranslateDistance * Math.sin(rotationRadians + Math.PI));
        this.propulsionSprite.translate(propulsionSpriteTranslateX, propulsionSpriteTranslateY);

        this.dx = (float) (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * Math.cos(rotationRadians));
        this.dy = (float) (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed * Math.sin(rotationRadians));

        this.arrivalTimer = this.timeToPosition(this.destination);
    }

    public void move(final float deltaTime) {
        try {
            if (!this.arrived) {
                float currentDx = this.dx * deltaTime;
                float currentDy = this.dy * deltaTime;
                this.rocketSprite.translate(currentDx, currentDy);
                this.propulsionSprite.translate(currentDx, currentDy);
                this.arrivalTimer -= (deltaTime);
                if (this.arrivalTimer <= 0f) {
                    screen.cooldownSound.play(0.3f);
                    this.arrival();
                }
            } else {
                this.arrivalTimer += (deltaTime);
                if (this.arrivalTimer > 5f) {
                    this.arrival();
                }
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public float getDistance(final Vector2 positionOne, final Vector2 positionTwo) {
        return (float) Math.sqrt(Math.pow(positionOne.x - positionTwo.x, 2) + Math.pow(positionOne.y - positionTwo.y, 2));
    }

    public float timeToPosition(final Vector2 position) {
        float distance = (float) Math.sqrt(Math.pow(this.rocketSprite.getX() - position.x, 2) + Math.pow(this.rocketSprite.getY() - position.y, 2));
        return distance / (Rocket.ROCKET_TIER_STATS[this.tier - 1].speed);
    }

    public void arrival() {
        try {
            this.arrived = true;
            this.arrivalTimer = 0f;
            this.screen.addRocket(new AddRocketMessage(new RocketMessageModel(this), this.destinationPlanet.name));
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
