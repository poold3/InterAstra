package io.github.interastra.models;

import io.github.interastra.message.models.PlayerMessageModel;

public class Player {
    public String name;
    public float balance;

    public Player(PlayerMessageModel playerMessageModel) {
        this.name = playerMessageModel.name();
        this.balance = playerMessageModel.balance();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != Player.class) {
            return false;
        }

        Player otherPlayer = (Player) o;
        return this.name.equals(otherPlayer.name);
    }
}
