package io.github.interastra.models;

import io.github.interastra.message.models.PlanetResourceMessageModel;

public class PlanetResource {
    public enum PLANET_RESOURCE {
        IRON,
        OIL,
        SILICON,
        LITHIUM,
        HELIUM3
    }
    public static float[] PLANET_RESOURCE_BUY_RATE = {1f, 1.5f, 2f, 3f, 6f};
    public static float[] PLANET_RESOURCE_SELL_BASE_RATE = {0.5f, 0.75f, 1f, 1.5f, 3f};
    public static float[] PLANET_RESOURCE_CAPS = {1000f, 750f, 500f, 500f, 250f};
    public static float RESOURCE_DEVALUATION_TIMER = 600f;
    public static float RESOURCE_DEVALUATION_RATE = .75f;

    public PLANET_RESOURCE resource;
    public float rate;

    public PlanetResource(PlanetResourceMessageModel planetResourceMessageModel) {
        this.resource = planetResourceMessageModel.resource();
        this.rate = planetResourceMessageModel.rate();
    }

    public String getPlanetResourceName() {
        if (resource == PLANET_RESOURCE.IRON) {
            return "Iron";
        } else if (resource == PLANET_RESOURCE.OIL) {
            return "Oil";
        } else if (resource == PLANET_RESOURCE.SILICON) {
            return "Silicon";
        } else if (resource == PLANET_RESOURCE.LITHIUM) {
            return "Lithium";
        } else if (resource == PLANET_RESOURCE.HELIUM3) {
            return "Helium3";
        }
        return "";
    }
}
