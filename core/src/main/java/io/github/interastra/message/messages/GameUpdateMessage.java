package io.github.interastra.message.messages;

import io.github.interastra.message.models.GameUpdatePlanetMessageModel;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.message.models.RocketMessageModel;

import java.util.ArrayList;

public record GameUpdateMessage(ArrayList<GameUpdatePlanetMessageModel> planets) {
}
