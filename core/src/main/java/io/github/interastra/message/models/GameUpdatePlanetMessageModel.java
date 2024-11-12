package io.github.interastra.message.models;

import java.util.ArrayList;

public record GameUpdatePlanetMessageModel(ArrayList<String> bases, ArrayList<RocketMessageModel> rocketsInOrbit) {
}
