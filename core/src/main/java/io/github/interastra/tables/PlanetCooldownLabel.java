package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.screens.GameScreen;

public class PlanetCooldownLabel extends Container<Actor> {
    private final PlanetDashboardTable planetDashboardTable;
    public ColorLabel baseCooldownLabel;

    public PlanetCooldownLabel(final GameScreen screen, final PlanetDashboardTable planetDashboardTable, final Skin skin) {
        super();
        this.size(PlanetDashboardTable.DASHBOARD_BUTTON_WIDTH, PlanetDashboardTable.DASHBOARD_BUTTON_HEIGHT);
        this.pad(5f);
        this.planetDashboardTable = planetDashboardTable;

        this.baseCooldownLabel = new ColorLabel("", skin, Color.WHITE);
        this.baseCooldownLabel.setAlignment(Align.center);

        this.setActor(this.baseCooldownLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.planetDashboardTable.planet.baseCooldown > 0f) {
            this.baseCooldownLabel.setText(String.format("%.0f", this.planetDashboardTable.planet.baseCooldown));
        } else {
            this.baseCooldownLabel.setText("");
        }
    }
}
