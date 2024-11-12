package io.github.interastra.message.models;

import io.github.interastra.models.Rocket;

public record RocketMessageModel(String id, String playerName, int tier) {
    public RocketMessageModel(Rocket rocket) {
        this(
            rocket.id,
            rocket.playerName,
            rocket.tier
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() == RocketMessageModel.class) {
            RocketMessageModel otherRocket = (RocketMessageModel) o;
            return this.id.equals(otherRocket.id());
        } else if (o.getClass() == Rocket.class) {
            Rocket otherRocket = (Rocket) o;
            return this.id.equals(otherRocket.id);
        }
        return false;
    }
}
