package io.github.interastra.message.messages;

import io.github.interastra.message.models.PlayerMessageModel;

import java.util.ArrayList;

public record GameEndMessage(ArrayList<PlayerMessageModel> players) {
}
