package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameUpdateMessage;
import io.github.interastra.message.models.GameUpdatePlanetMessageModel;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.models.Planet;
import io.github.interastra.models.RocketInOrbit;
import io.github.interastra.screens.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameUpdate implements StompFrameHandler {
    public GameScreen gameScreen;

    public GameUpdate(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @NotNull
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameUpdateMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GameUpdateMessage message = (GameUpdateMessage) payload;
        for (int i = 0; i < message.planets().size(); ++i) {
            GameUpdatePlanetMessageModel messagePlanet = message.planets().get(i);
            Planet planet = gameScreen.planets.get(i);
            if (planet.bases.size() != messagePlanet.bases().size()) {
                planet.bases = new CopyOnWriteArrayList<>(messagePlanet.bases());
                planet.setHasMyBase();
            }

            AtomicBoolean recalculateMyRockets = new AtomicBoolean(false);
            planet.rocketsInOrbit.removeIf(rocket -> {
                if (!messagePlanet.rocketsInOrbit().contains(rocket)) {
                    recalculateMyRockets.set(true);
                    return true;
                } else {
                    return false;
                }
            });
            for (RocketMessageModel rocket : messagePlanet.rocketsInOrbit()) {
                if (!planet.rocketsInOrbit.contains(rocket)) {
                    gameScreen.rocketsInFlight.remove(rocket);
                    planet.rocketsInOrbit.add(new RocketInOrbit(gameScreen.spaceCraftTextureAtlas, rocket, planet, gameScreen.cooldownSound, RocketInOrbit.ROCKET_TIER_STATS[rocket.tier() - 1].cooldown));
                    recalculateMyRockets.set(true);
                    gameScreen.planetDashboardTable.updateRocketsInFlight();
                }
            }
            if (recalculateMyRockets.get()) {
                planet.setNumMyRockets();
            }
        }
    }
}
