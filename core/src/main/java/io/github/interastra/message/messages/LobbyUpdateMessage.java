package io.github.interastra.message.messages;

import io.github.interastra.message.models.LobbyPlayerModel;

import java.util.ArrayList;

public record LobbyUpdateMessage(ArrayList<LobbyPlayerModel> players) {
}
