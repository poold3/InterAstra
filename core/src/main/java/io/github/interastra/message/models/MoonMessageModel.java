package io.github.interastra.message.models;

public record MoonMessageModel(
    String name,
    float size,
    float orbitalRadius,
    float orbitalSpeed,
    float startingOrbitalPosition
) {
}
