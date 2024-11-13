package io.github.interastra.message.messages;

import io.github.interastra.message.models.RocketMessageModel;

public record RemoveRocketMessage(RocketMessageModel rocket, String planetName) {
}
