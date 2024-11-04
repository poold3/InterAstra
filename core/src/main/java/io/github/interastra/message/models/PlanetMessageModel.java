package io.github.interastra.message.models;

import java.util.ArrayList;

public record PlanetMessageModel(
    int index,
    String name,
    float size,
    int baseLimit,
    float orbitalRadius,
    float orbitalSpeed,
    float startingOrbitalPosition,
    MoonMessageModel moon,
    ArrayList<PlanetResourceMessageModel> resources
) {
}
