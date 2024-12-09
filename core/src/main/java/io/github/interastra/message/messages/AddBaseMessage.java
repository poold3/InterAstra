package io.github.interastra.message.messages;

import io.github.interastra.message.models.RocketMessageModel;

public record AddBaseMessage(RocketMessageModel rocket, String planetName, int currentNumBases) {
}
