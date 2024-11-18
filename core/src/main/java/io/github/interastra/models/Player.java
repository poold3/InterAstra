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
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.IRON, 100f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.OIL, 100f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.SILICON, 0f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.LITHIUM, 0f);
        this.resourceBalances.put(PlanetResource.PLANET_RESOURCE.HELIUM3, 0f);
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
