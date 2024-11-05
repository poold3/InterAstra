package io.github.interastra.tooltips;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.models.Planet;
import io.github.interastra.models.PlanetResource;
import io.github.interastra.screens.GameScreen;

public class PlanetToolTip extends Tooltip<Table> {
    public static final float FIRST_ROW_CELL_WIDTH = 30f;
    public static final float FIRST_ROW_IMAGE_SIZE = 20f;

    public Drawable rocketDrawable;
    public Drawable baseDrawable;
    public Drawable moonDrawable;

    public PlanetToolTip(final GameScreen screen, final Planet planet, final Drawable rocketDrawable, final Drawable baseDrawable, final Drawable moonDrawable) {
        super(new Table(), getInstantManager());

        this.rocketDrawable = rocketDrawable;
        this.baseDrawable = baseDrawable;
        this.moonDrawable = moonDrawable;

        Container<Table> toolTipContainer = this.getContainer();
        toolTipContainer.setActor(this.getPlanetTable(screen, planet));
        toolTipContainer.setBackground(screen.skin.getDrawable("panel_rectangle"));
    }

    public Table getPlanetTable(final GameScreen screen, Planet planet) {
        Table planetTable = new Table();

        Label.LabelStyle planetNameStyle = new Label.LabelStyle(
            screen.skin.get(Label.LabelStyle.class)
        );
        planetNameStyle.font = screen.skin.getFont("Teko-32");
        Label planetNameLabel = new Label(planet.name, planetNameStyle);
        planetTable.add(planetNameLabel).colspan(4).expandX().center().pad(2f);

        planetTable.row();

        for (PlanetResource planetResource : planet.resources) {
            Label planetResourceLabel = new Label(String.format("%s: %f", planetResource.getPlanetResourceName(), planetResource.rate), screen.skin);
            planetTable.add(planetResourceLabel).colspan(4).expandX().center().pad(2f);

            planetTable.row();
        }

        Label limitLabel = new Label(String.format("%d", planet.baseLimit), screen.skin);
        limitLabel.setAlignment(Align.center);
        Container<Label> limitContainer = new Container<>(limitLabel);
        limitContainer.size(FIRST_ROW_IMAGE_SIZE);
        planetTable.add(limitContainer).minWidth(FIRST_ROW_CELL_WIDTH).maxWidth(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);

        if (planet.hasMyRocket()) {
            Container<Image> rocketContainer = new Container<>(new Image(this.rocketDrawable));
            rocketContainer.size(FIRST_ROW_IMAGE_SIZE);
            planetTable.add(rocketContainer).minWidth(FIRST_ROW_CELL_WIDTH).maxWidth(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);
        }

        if (planet.hasMyBase()) {
            Container<Image> baseContainer = new Container<>(new Image(this.baseDrawable));
            baseContainer.size(FIRST_ROW_IMAGE_SIZE);
            planetTable.add(baseContainer).minWidth(FIRST_ROW_CELL_WIDTH).maxWidth(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);
        }

        if (planet.moon != null) {
            Container<Image> moonContainer = new Container<>(new Image(this.moonDrawable));
            moonContainer.size(FIRST_ROW_IMAGE_SIZE);
            planetTable.add(moonContainer).minWidth(FIRST_ROW_CELL_WIDTH).maxWidth(FIRST_ROW_CELL_WIDTH).expandX().center().pad(2f);
        }

        return planetTable;
    }

    public static TooltipManager getInstantManager() {
        TooltipManager tooltipManager = new TooltipManager();
        tooltipManager.instant();
        tooltipManager.initialTime = 0f;
        tooltipManager.subsequentTime = 0f;
        return tooltipManager;
    }
}
