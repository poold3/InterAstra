package io.github.interastra.message.models;

public record PlanetMessageModel(
    int index,
    String name,
    float size,
    float orbitalRadius,
    float orbitalSpeed,
    float startingOrbitalPosition,
    MoonMessageModel moon
) {
}
