package io.github.interastra.models;

import io.github.interastra.message.models.PlanetResourceMessageModel;

public class PlanetResource {
    public enum PLANET_RESOURCE {
        IRON,
        OIL,
        ALUMINUM,
        COPPER,
        STONE
    }
    public static float[] PLANET_RESOURCE_BUY_BASE_RATE = {1f, 2f, 1.5f, 1.5f, 2f};
    public static float[] PLANET_RESOURCE_SELL_BASE_RATE = {0.9f, 1.5f, 1f, 1f, 1.5f};
    public static float[] PLANET_RESOURCE_CAPS = {1000f, 500f, 750f, 750f, 500f};
    public static float RESOURCE_VALUATION_TIMER = 720f;
    public static float RESOURCE_SELL_VALUATION_RATE = .85f;
    public static float RESOURCE_BUY_VALUATION_RATE = .90f;

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
        } else if (resource == PLANET_RESOURCE.ALUMINUM) {
            return "Aluminum";
        } else if (resource == PLANET_RESOURCE.COPPER) {
            return "Copper";
        } else if (resource == PLANET_RESOURCE.STONE) {
            return "Stone";
        }
        return "";
    }
}
