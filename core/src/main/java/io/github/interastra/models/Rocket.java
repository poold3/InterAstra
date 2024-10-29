package io.github.interastra.models;

import java.util.UUID;

public class Rocket {
    public String id;
    public String playerName;

    public Rocket(final String playerName) {
        this.id = UUID.randomUUID().toString();
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != Rocket.class) {
            return false;
        }

        Rocket otherRocket = (Rocket) o;
        return this.id.equals(otherRocket.id);
    }
}
