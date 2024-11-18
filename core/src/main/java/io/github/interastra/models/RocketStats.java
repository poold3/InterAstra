package io.github.interastra.models;

public class RocketStats {
    public float range;
    public float speed;
    public float cooldown;
    public float sellPrice;
    public float resourceMultiplier;

    public RocketStats(final float range, final float speed, final float cooldown, final float sellPrice, final float resourceMultiplier) {
        this.range = range;
        this.speed = speed;
        this.cooldown = cooldown;
        this.sellPrice = sellPrice;
        this.resourceMultiplier = resourceMultiplier;
    }

    @Override
    public String toString() {
        return String.format("Range: %.0f u, Speed: %.1f u/s, Cooldown: %.0f s, Multiplier: %.1f, Sell Price: $%.2f", this.range, this.speed, this.cooldown, this.resourceMultiplier, this.sellPrice);
    }
}
