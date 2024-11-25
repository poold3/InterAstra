package io.github.interastra.message.models;

import io.github.interastra.models.Price;

public record PriceMessageModel(
    float balance,
    float ironBalance,
    float oilBalance,
    float aluminumBalance,
    float copperBalance,
    float stoneBalance
) {
    public PriceMessageModel(final Price price) {
        this(
            price.balance,
            price.ironBalance,
            price.oilBalance,
            price.aluminumBalance,
            price.copperBalance,
            price.stoneBalance
        );
    }
}
