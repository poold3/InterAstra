package io.github.interastra.models;

import com.badlogic.gdx.graphics.Texture;

public class Planet {
    public static final int NUM_PLANETS = 11;

    public int index;
    public float size;

    public Planet(int index, float size) {
        this.index = index;
        this.size = size;
    }
}
