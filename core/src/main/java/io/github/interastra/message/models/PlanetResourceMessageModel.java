package io.github.interastra.message.models;

import io.github.interastra.models.PlanetResource;

public record PlanetResourceMessageModel(PlanetResource.PLANET_RESOURCE resource, float rate) {
}
