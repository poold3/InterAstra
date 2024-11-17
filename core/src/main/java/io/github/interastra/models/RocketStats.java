package io.github.interastra.models;

public class RocketStats {
    public float range;
    public float speed;
    public float cooldown;
    public float sellPrice;

    public RocketStats(final float range, final float speed, final float cooldown, final float sellPrice) {
        this.range = range;
        this.speed = speed;
        this.cooldown = cooldown;
        this.sellPrice = sellPrice;
    }

    @Override
    public String toString() {
        return String.format("Range: %.0f u, Speed: %.1f u/s, Cooldown: %.0f s, Sell Price: $%.2f", this.range, this.speed, this.cooldown, this.sellPrice);
    }
}
