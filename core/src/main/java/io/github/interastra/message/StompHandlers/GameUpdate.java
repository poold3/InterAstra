package io.github.interastra.message.StompHandlers;

import io.github.interastra.message.messages.GameUpdateMessage;
import io.github.interastra.message.models.GameUpdatePlanetMessageModel;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.models.Planet;
import io.github.interastra.models.Rocket;
import io.github.interastra.models.RocketInOrbit;
import io.github.interastra.screens.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
                planet.bases = new ArrayList<>(messagePlanet.bases());
            }
            planet.rocketsInOrbit.removeIf(rocket -> !messagePlanet.rocketsInOrbit().contains(rocket));
            for (RocketMessageModel rocket : messagePlanet.rocketsInOrbit()) {
                if (!planet.rocketsInOrbit.contains(rocket)) {
                    //planet.rocketsInOrbit.add(new RocketInOrbit(gameScreen.spaceCraftTextureAtlas, rocket, planet));
                }
            }
        }
    }
}
