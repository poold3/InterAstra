package io.github.interastra.models;

public class Price {
    public float balance;
    public float ironBalance;
    public float oilBalance;
    public float siliconBalance;
    public float lithiumBalance;
    public float helium3Balance;

    public Price() {
        this.balance = 0f;
        this.ironBalance = 0f;
        this.oilBalance = 0f;
        this.siliconBalance = 0f;
        this.lithiumBalance = 0f;
        this.helium3Balance = 0f;
    }

    public Price(final float balance, final float ironBalance, final float oilBalance, final float siliconBalance, final float lithiumBalance, final float helium3Balance) {
        this.balance = balance;
        this.ironBalance = ironBalance;
        this.oilBalance = oilBalance;
        this.siliconBalance = siliconBalance;
        this.lithiumBalance = lithiumBalance;
        this.helium3Balance = helium3Balance;
    }

    public boolean canAfford(final Player player) {
        return (
            player.balance >= this.balance
            && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.IRON) >= this.ironBalance
            && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.OIL) >= this.oilBalance
            && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.SILICON) >= this.siliconBalance
            && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.LITHIUM) >= this.lithiumBalance
            && player.resourceBalances.get(PlanetResource.PLANET_RESOURCE.HELIUM3) >= this.helium3Balance
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
        if (this.siliconBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.SILICON, (k, currentBalance) -> currentBalance - this.siliconBalance);
        }
        if (this.lithiumBalance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.LITHIUM, (k, currentBalance) -> currentBalance - this.lithiumBalance);
        }
        if (this.helium3Balance > 0f) {
            player.resourceBalances.computeIfPresent(PlanetResource.PLANET_RESOURCE.HELIUM3, (k, currentBalance) -> currentBalance - this.helium3Balance);
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
        if (this.siliconBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;
            stringBuilder.append(String.format("%.0f silicon", this.siliconBalance));
        }
        if (this.lithiumBalance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;
            stringBuilder.append(String.format("%.0f lithium", this.lithiumBalance));
        }
        if (this.helium3Balance > 0f) {
            if (!first) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%.0f helium3", this.helium3Balance));
        }
        return stringBuilder.toString();
    }
}
