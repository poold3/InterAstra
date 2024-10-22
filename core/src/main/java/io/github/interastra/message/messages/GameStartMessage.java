package io.github.interastra.message.messages;

import io.github.interastra.message.models.PlanetMessageModel;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.message.models.StarMessageModel;

import java.util.ArrayList;

public record GameStartMessage(
    ArrayList<PlayerMessageModel> players,
    ArrayList<PlanetMessageModel> planets,
    StarMessageModel sol
) {
}
