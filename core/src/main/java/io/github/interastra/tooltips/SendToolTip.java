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

public class SendToolTip extends Tooltip<Table> {
    public SendToolTip(final GameScreen screen, final RocketInOrbit rocket) {
        super(new Table(), new InstantTooltipManager());

        Container<Table> toolTipContainer = this.getContainer();
        toolTipContainer.setActor(this.getSendTable(screen, rocket.tier));
        toolTipContainer.setBackground(screen.skin.getDrawable("panel_glass"));
    }

    public Table getSendTable(final GameScreen screen, final int tier) {
        Table sendTable = new Table();

        Container<Label> titleContainer = new Container<>(new ColorLabel("Send This Rocket", screen.skin, Color.BLACK));
        titleContainer.pad(10f);
        sendTable.add(titleContainer).expandX().center();
        sendTable.row();

        Container<Label> priceContainer = new Container<>(new ColorLabel("Price: " + Rocket.ROCKET_TIER_FUEL_PRICE[tier - 1].toString(), screen.skin, Color.BLACK));
        priceContainer.pad(10f);
        sendTable.add(priceContainer).expandX().center();

        return sendTable;
    }
}
