package io.github.interastra.models;

public class Player {
    public String name;
    public String messageSessionId;
    public float balance;
    public boolean ready;

    public Player(String name) {
        this.name = name;
        this.messageSessionId = null;
        this.balance = 0f;
        this.ready = false;
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
