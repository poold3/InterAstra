package io.github.interastra.message.messages;

import io.github.interastra.message.models.RocketMessageModel;

public record AddRocketMessage(RocketMessageModel rocket, String planetName) {
}
