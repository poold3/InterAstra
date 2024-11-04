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
