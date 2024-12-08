package io.github.interastra.tooltips;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Planet;
import io.github.interastra.screens.GameScreen;

public class BaseToolTip extends Tooltip<Table> {
    public BaseToolTip(final GameScreen screen) {
        super(new Table(), new InstantTooltipManager());

        Container<Table> toolTipContainer = this.getContainer();
        toolTipContainer.setActor(this.getBaseTable(screen));
        toolTipContainer.setBackground(screen.skin.getDrawable("panel_glass"));
    }

    public Table getBaseTable(final GameScreen screen) {
        Table baseTable = new Table();

        Container<Label> priceContainer = new Container<>(new ColorLabel("Price: " + Planet.BASE_PRICE, screen.skin, Color.BLACK));
        priceContainer.pad(10f);
        baseTable.add(priceContainer).expandX().center();
        baseTable.row();

        Container<Label> statsContainer = new Container<>(new ColorLabel(String.format("Cooldown: %.0f s, Multiplier: %.2f", Planet.BASE_COOLDOWN, Planet.BASE_RESOURCE_MULTIPLIER), screen.skin, Color.BLACK));
        statsContainer.pad(10f);
        baseTable.add(statsContainer).expandX().center();

        return baseTable;
    }
}
