package io.github.interastra.message.models;

import io.github.interastra.models.Rocket;

public record RocketMessageModel(String id, String playerName) {
    public RocketMessageModel(Rocket rocket) {
        this(
            rocket.id,
            rocket.playerName
        );
    }
}
