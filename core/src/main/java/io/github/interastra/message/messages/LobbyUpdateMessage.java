package io.github.interastra.message.messages;

import io.github.interastra.message.models.LobbyPlayerMessageModel;

import java.util.ArrayList;

public record LobbyUpdateMessage(ArrayList<LobbyPlayerMessageModel> players) {
}
