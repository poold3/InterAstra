package io.github.interastra.models;

import io.github.interastra.message.models.PlanetResourceMessageModel;

import java.util.concurrent.ConcurrentHashMap;

public class PlanetResource {
    public enum PLANET_RESOURCE {
        IRON,
        OIL,
        SILICON,
        LITHIUM,
        HELIUM3
    }
    public static float[] PLANET_RESOURCE_BUY_RATE = {1f, 1.5f, 2f, 3f, 6f};
    public static float[] PLANET_RESOURCE_SELL_RATE = {0.5f, 0.75f, 1f, 1.5f, 3f};
    public static float[] PLANET_RESOURCE_CAPS = {1000f, 750f, 500f, 500f, 250f};

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

    public static String getBuyRateString() {
        return String.format(
            "Iron: $%.2f, Oil: $%.2f, Silicon: $%.2f, Lithium: $%.2f, Helium3: $%.2f",
            PLANET_RESOURCE_BUY_RATE[PLANET_RESOURCE.IRON.ordinal()],
            PLANET_RESOURCE_BUY_RATE[PLANET_RESOURCE.OIL.ordinal()],
            PLANET_RESOURCE_BUY_RATE[PLANET_RESOURCE.SILICON.ordinal()],
            PLANET_RESOURCE_BUY_RATE[PLANET_RESOURCE.LITHIUM.ordinal()],
            PLANET_RESOURCE_BUY_RATE[PLANET_RESOURCE.HELIUM3.ordinal()]
        );
    }

    public static String getSellRateString() {
        return String.format(
            "Iron: $%.2f, Oil: $%.2f, Silicon: $%.2f, Lithium: $%.2f, Helium3: $%.2f",
            PLANET_RESOURCE_SELL_RATE[PLANET_RESOURCE.IRON.ordinal()],
            PLANET_RESOURCE_SELL_RATE[PLANET_RESOURCE.OIL.ordinal()],
            PLANET_RESOURCE_SELL_RATE[PLANET_RESOURCE.SILICON.ordinal()],
            PLANET_RESOURCE_SELL_RATE[PLANET_RESOURCE.LITHIUM.ordinal()],
            PLANET_RESOURCE_SELL_RATE[PLANET_RESOURCE.HELIUM3.ordinal()]
        );
    }
}
