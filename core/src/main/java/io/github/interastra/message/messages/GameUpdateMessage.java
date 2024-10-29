package io.github.interastra.message.messages;

import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.message.models.RocketMessageModel;

import java.util.ArrayList;

public record GameUpdateMessage(long gameTime, ArrayList<PlayerMessageModel> players, ArrayList<RocketMessageModel> rockets) {
}
