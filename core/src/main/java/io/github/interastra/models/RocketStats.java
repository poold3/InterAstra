package io.github.interastra.models;

public class RocketStats {
    public float range;
    public float speed;

    public RocketStats(final float range, final float speed) {
        this.range = range;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return String.format("Stats: range: %.0f, speed: %.3f", this.range, this.speed);
    }
}
