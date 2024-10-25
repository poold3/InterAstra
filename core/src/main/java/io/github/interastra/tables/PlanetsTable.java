package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.interastra.models.Planet;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;

public class PlanetsTable extends Table {
    public static final float MAX_BUTTON_SIZE = 50f;
    public static final float BUTTON_PAD = 1f;

    private final GameScreen screen;
    private final Skin skin;
    private final float buttonSize;

    public PlanetsTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;
        this.buttonSize = Math.min(((this.screen.stageViewport.getWorldWidth() / 2f) - (9f * 2f * BUTTON_PAD)) / 9f, MAX_BUTTON_SIZE);

        this.setFillParent(true);
        this.bottom();

        for (Planet planet : this.screen.planets) {
            this.add(this.getPlanetContainer(planet)).maxWidth(this.buttonSize).maxHeight(this.buttonSize).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
        }
    }

    public Container<ImageButton> getPlanetContainer(Planet planet) {
        TextureRegion planetTextureRegion = this.screen.planetsTextureAtlas.findRegion("planet", planet.index);
        Drawable planetDrawable = new TextureRegionDrawable(planetTextureRegion);

        ImageButton.ImageButtonStyle planetButtonStyle = new ImageButton.ImageButtonStyle();
        planetButtonStyle.imageUp = planetDrawable;
        planetButtonStyle.imageDown = planetDrawable;
        ImageButton planetImageButton = new ImageButton(planetButtonStyle);
        planetImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.entityBeingFollowed = planet;
                if (planet.moon != null) {
                    screen.camera.targetZoom = screen.camera.getZoomForSize((planet.moon.orbitalRadius * 2f) + planet.moon.getWidth());
                } else {
                    screen.camera.targetZoom = screen.camera.getZoomForSize(planet.getWidth());
                }

            }
        });

        Container<ImageButton> planetContainer = new Container<>(planetImageButton);
        planetContainer.width(this.buttonSize);
        planetContainer.height(this.buttonSize);
        planetContainer.setBackground(this.skin.getDrawable("panel_square"));

        TooltipManager tooltipManager = new TooltipManager();
        tooltipManager.instant();
        tooltipManager.initialTime = 0f;
        tooltipManager.subsequentTime = 0f;

        TextTooltip.TextTooltipStyle planetNameStyle = new TextTooltip.TextTooltipStyle(
            this.skin.get(TextTooltip.TextTooltipStyle.class)
        );
        planetNameStyle.label.font = this.skin.getFont("Teko-32");
        TextTooltip planetName = new TextTooltip(planet.name, tooltipManager, planetNameStyle);
        planetContainer.addListener(planetName);

        return planetContainer;
    }
}
