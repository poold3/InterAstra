package io.github.interastra.models;

import io.github.interastra.message.models.PlayerMessageModel;

import java.util.concurrent.ConcurrentHashMap;

public class Player {
    public static float BALANCE_CAP = 5000f;

    public String name;
    public int bases;
    public float balance;
    public ConcurrentHashMap<PlanetResource.PLANET_RESOURCE, Float> resourceBalances;

    public Player(PlayerMessageModel playerMessageModel) {
        this.name = playerMessageModel.name();
        this.bases = playerMessageModel.bases();
        this.balance = 0f;
        this.resourceBalances = new ConcurrentHashMap<>();
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.IRON, 150f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.OIL, 150f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.ALUMINUM, 0f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.COPPER, 0f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.STONE, 0f);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != Player.class) {
            return false;
        }

        Player otherPlayer = (Player) o;
        return this.name.equals(otherPlayer.name);
    }
}
