package io.github.interastra.tooltips;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Rocket;
import io.github.interastra.screens.GameScreen;

public class RocketToolTip extends Tooltip<Table> {
    public RocketToolTip(final GameScreen screen, final int rocketTier) {
        super(new Table(), new InstantTooltipManager());

        Container<Table> toolTipContainer = this.getContainer();
        toolTipContainer.setActor(this.getRocketTable(screen, rocketTier));
        toolTipContainer.setBackground(screen.skin.getDrawable("panel_glass"));
    }

    public Table getRocketTable(final GameScreen screen, final int rocketTier) {
        Table rocketTable = new Table();

        Drawable rocketDrawable = new TextureRegionDrawable(screen.spaceCraftTextureAtlas.findRegion("spaceRockets", rocketTier));
        rocketTable.add(new Image(rocketDrawable)).size(Rocket.ROCKET_TIER_WIDTH[rocketTier - 1] * 200f, Rocket.ROCKET_TIER_HEIGHT[rocketTier - 1] * 200f).padTop(5f);
        rocketTable.row();

        Container<Label> priceContainer = new Container<>(new ColorLabel(Rocket.ROCKET_TIER_PRICE[rocketTier - 1].toString(), screen.skin, Color.BLACK));
        priceContainer.pad(10f);
        rocketTable.add(priceContainer).expandX().center();
        rocketTable.row();

        Container<Label> statsContainer = new Container<>(new ColorLabel(Rocket.ROCKET_TIER_STATS[rocketTier - 1].toString(), screen.skin, Color.BLACK));
        statsContainer.pad(10f);
        rocketTable.add(statsContainer).expandX().center();

        return rocketTable;
    }
}
