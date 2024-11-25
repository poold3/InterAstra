package io.github.interastra.models;

import io.github.interastra.message.models.PriceMessageModel;
import io.github.interastra.screens.GameScreen;

public class Price {
    public float balance;
    public float ironBalance;
    public float oilBalance;
    public float aluminumBalance;
    public float copperBalance;
    public float stoneBalance;

    public Price() {
        this.balance = 0f;
        this.ironBalance = 0f;
        this.oilBalance = 0f;
        this.aluminumBalance = 0f;
        this.copperBalance = 0f;
        this.stoneBalance = 0f;
    }

    public Price(final PriceMessageModel priceMessageModel) {
        this.balance = priceMessageModel.balance();
        this.ironBalance = priceMessageModel.ironBalance();
        this.oilBalance = priceMessageModel.oilBalance();
        this.aluminumBalance = priceMessageModel.aluminumBalance();
        this.copperBalance = priceMessageModel.copperBalance();
        this.stoneBalance = priceMessageModel.stoneBalance();
    }

    public Price(final float balance, final float ironBalance, final float oilBalance, final float aluminumBalance, final float copperBalance, final float stoneBalance) {
        this.balance = balance;
        this.ironBalance = ironBalance;
        this.oilBalance = oilBalance;
        this.aluminumBalance = aluminumBalance;
        this.copperBalance = copperBalance;
        this.stoneBalance = stoneBalance;
    }

    public boolean canAfford(final Player player) {
        return (
            player.balance >= this.balance
                && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.IRON) >= this.ironBalance
                && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.OIL) >= this.oilBalance
                && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.ALUMINUM) >= this.aluminumBalance
                && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.COPPER) >= this.copperBalance
                && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.STONE) >= this.stoneBalance
        );
    }

    public void purchase (final Player player) {
        if (!this.canAfford(player)) {
            return;
        }

        if (this.balance > 0f) {
            player.balance -= this.balance;
        }
        if (this.ironBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.IRON, (k, currentBalance) -> currentBalance - this.ironBalance);
        }
        if (this.oilBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.OIL, (k, currentBalance) -> currentBalance - this.oilBalance);
        }
        if (this.aluminumBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.ALUMINUM, (k, currentBalance) -> currentBalance - this.aluminumBalance);
        }
        if (this.copperBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.COPPER, (k, currentBalance) -> currentBalance - this.copperBalance);
        }
        if (this.stoneBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.STONE, (k, currentBalance) -> currentBalance - this.stoneBalance);
        }
    }

    public void sell (final Player player) {
        if (this.balance > 0f) {
            player.balance += this.balance;
        }
        if (this.ironBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.IRON, (k, currentBalance) -> currentBalance + this.ironBalance);
        }
        if (this.oilBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.OIL, (k, currentBalance) -> currentBalance + this.oilBalance);
        }
        if (this.aluminumBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.ALUMINUM, (k, currentBalance) -> currentBalance + this.aluminumBalance);
        }
        if (this.copperBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.COPPER, (k, currentBalance) -> currentBalance + this.copperBalance);
        }
        if (this.stoneBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.STONE, (k, currentBalance) -> currentBalance + this.stoneBalance);
        }
    }

    @Override
    public String toString() {
        boolean first = true;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.balance > 0f) {
            first = false;
            stringBuilder.append(String.format("$%.2f", this.balance));
        }
        if (this.ironBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;
            stringBuilder.append(String.format("%.0f iron", this.ironBalance));
        }
        if (this.oilBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;
            stringBuilder.append(String.format("%.0f oil", this.oilBalance));
        }
        if (this.aluminumBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;
            stringBuilder.append(String.format("%.0f aluminum", this.aluminumBalance));
        }
        if (this.copperBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;
            stringBuilder.append(String.format("%.0f copper", this.copperBalance));
        }
        if (this.stoneBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%.0f stone", this.stoneBalance));
        }
        return stringBuilder.toString();
    }

    public float getBuyAmount(final GameScreen screen) {
        float buyAmount = 0f;
        buyAmount += (this.ironBalance * screen.buySellTable.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.IRON.ordinal()]);
        buyAmount += (this.oilBalance * screen.buySellTable.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.OIL.ordinal()]);
        buyAmount += (this.aluminumBalance * screen.buySellTable.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.ALUMINUM.ordinal()]);
        buyAmount += (this.copperBalance * screen.buySellTable.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.COPPER.ordinal()]);
        buyAmount += (this.stoneBalance * screen.buySellTable.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.STONE.ordinal()]);
        return buyAmount;
    }

    public float getSellAmount(final GameScreen screen) {
        float sellAmount = 0f;
        sellAmount += (this.ironBalance * screen.buySellTable.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.IRON.ordinal()]);
        sellAmount += (this.oilBalance * screen.buySellTable.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.OIL.ordinal()]);
        sellAmount += (this.aluminumBalance * screen.buySellTable.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.ALUMINUM.ordinal()]);
        sellAmount += (this.copperBalance * screen.buySellTable.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.COPPER.ordinal()]);
        sellAmount += (this.stoneBalance * screen.buySellTable.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.STONE.ordinal()]);
        return sellAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != Price.class) {
            return false;
        }

        Price otherPrice = (Price) o;
        return (
            otherPrice.balance == this.balance
                && otherPrice.ironBalance == this.ironBalance
                && otherPrice.oilBalance == this.oilBalance
                && otherPrice.aluminumBalance == this.aluminumBalance
                && otherPrice.copperBalance == this.copperBalance
                && otherPrice.stoneBalance == this.stoneBalance
        );
    }
}
