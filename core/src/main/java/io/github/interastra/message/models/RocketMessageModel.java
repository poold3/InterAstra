package io.github.interastra.message.models;

import io.github.interastra.models.Rocket;
import io.github.interastra.models.RocketInFlight;
import io.github.interastra.models.RocketInOrbit;

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
        } else if (o.getClass() == RocketInOrbit.class) {
            RocketInOrbit otherRocket = (RocketInOrbit) o;
            return this.id.equals(otherRocket.id);
        } else if (o.getClass() == RocketInFlight.class) {
            RocketInFlight otherRocket = (RocketInFlight) o;
            return this.id.equals(otherRocket.id);
        }
        return false;
    }
}
