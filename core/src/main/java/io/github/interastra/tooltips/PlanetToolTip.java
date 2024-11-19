package io.github.interastra.tooltips;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Planet;
import io.github.interastra.models.PlanetResource;
import io.github.interastra.screens.GameScreen;

public class PlanetToolTip extends Tooltip<Table> {
    public static final float FIRST_ROW_CELL_WIDTH = 60f;
    public static final float FIRST_ROW_IMAGE_SIZE = 20f;

    public Drawable moonDrawable;

    public PlanetToolTip(final GameScreen screen, final Planet planet, final Drawable moonDrawable) {
        super(new Table(), new InstantTooltipManager());

        this.moonDrawable = moonDrawable;

        Container<Table> toolTipContainer = this.getContainer();
        toolTipContainer.setActor(this.getPlanetTable(screen, planet));
        toolTipContainer.setBackground(screen.skin.getDrawable("panel_glass"));
        toolTipContainer.pad(10f);
    }

    public Table getPlanetTable(final GameScreen screen, Planet planet) {
        Table planetTable = new Table();

        Label.LabelStyle planetNameStyle = new Label.LabelStyle(
            screen.skin.get(Label.LabelStyle.class)
        );
        planetNameStyle.font = screen.skin.getFont("Teko-32");
        ColorLabel planetNameLabel = new ColorLabel(planet.name, planetNameStyle, Color.BLACK);
        planetTable.add(planetNameLabel).colspan(2).expandX().center().pad(2f);

        planetTable.row();

        for (PlanetResource planetResource : planet.resources) {
            ColorLabel planetResourceLabel = new ColorLabel(String.format("%s: %f", planetResource.getPlanetResourceName(), planetResource.rate), screen.skin, Color.BLACK);
            planetTable.add(planetResourceLabel).colspan(2).expandX().center().pad(2f);

            planetTable.row();
        }

        ColorLabel limitLabel = new ColorLabel(String.format("%d", planet.baseLimit), screen.skin, Color.BLACK);
        limitLabel.setAlignment(Align.center);
        Container<Label> limitContainer = new Container<>(limitLabel);
        limitContainer.size(FIRST_ROW_IMAGE_SIZE);
        planetTable.add(limitContainer).width(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);

        if (planet.moon != null) {
            Container<Image> moonContainer = new Container<>(new Image(this.moonDrawable));
            moonContainer.size(FIRST_ROW_IMAGE_SIZE);
            planetTable.add(moonContainer).width(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);
        } else {
            planetTable.add().width(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);
        }

        return planetTable;
    }
}
