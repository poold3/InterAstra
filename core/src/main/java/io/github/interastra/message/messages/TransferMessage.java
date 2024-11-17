package io.github.interastra.message.messages;

import io.github.interastra.message.models.PriceMessageModel;

public record TransferMessage(
    String from,
    String to,
    PriceMessageModel amount
) {
}
