package io.github.interastra.message.models;

import io.github.interastra.models.Price;

public record PriceMessageModel(
    float balance,
    float ironBalance,
    float oilBalance,
    float siliconBalance,
    float lithiumBalance,
    float helium3Balance
) {
    public PriceMessageModel(final Price price) {
        this(
            price.balance,
            price.ironBalance,
            price.oilBalance,
            price.siliconBalance,
            price.lithiumBalance,
            price.helium3Balance
        );
    }
}
