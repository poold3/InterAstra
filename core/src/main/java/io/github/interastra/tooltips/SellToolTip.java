package io.github.interastra.tooltips;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Rocket;
import io.github.interastra.models.RocketInOrbit;
import io.github.interastra.screens.GameScreen;

public class SellToolTip extends Tooltip<Table> {
    public SellToolTip(final GameScreen screen, final RocketInOrbit rocket) {
        super(new Table(), new InstantTooltipManager());

        Container<Table> toolTipContainer = this.getContainer();
        toolTipContainer.setActor(this.getSellTable(screen, rocket.tier));
        toolTipContainer.setBackground(screen.skin.getDrawable("panel_glass"));
    }

    public Table getSellTable(final GameScreen screen, final int tier) {
        Table sellTable = new Table();

        Container<Label> titleContainer = new Container<>(new ColorLabel("Sell This Rocket", screen.skin, Color.BLACK));
        titleContainer.pad(10f);
        sellTable.add(titleContainer).expandX().center();
        sellTable.row();

        Container<Label> sellPriceContainer = new Container<>(new ColorLabel(String.format("Sell Price: $%.2f", Rocket.ROCKET_TIER_STATS[tier - 1].sellPrice), screen.skin, Color.BLACK));
        sellPriceContainer.pad(10f);
        sellTable.add(sellPriceContainer).expandX().center();

        return sellTable;
    }
}
