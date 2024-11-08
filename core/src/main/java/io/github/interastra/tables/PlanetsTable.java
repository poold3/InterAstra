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
import io.github.interastra.tooltips.PlanetToolTip;

public class PlanetsTable extends Table {
    public static final float MAX_BUTTON_SIZE = 50f;
    public static final float BUTTON_PAD = 1f;

    private final GameScreen screen;
    private final Skin skin;
    private final float buttonSize;
    public Drawable rocketDrawable;
    public Drawable baseDrawable;
    public Drawable moonDrawable;

    public PlanetsTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;
        int numButtons = screen.planets.size() + 1;
        this.buttonSize = Math.min(((this.screen.stageViewport.getWorldWidth() / 2f) - (numButtons * 2f * BUTTON_PAD)) / numButtons, MAX_BUTTON_SIZE);

        this.setFillParent(true);
        this.bottom();

        this.rocketDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("rocket_black"));
        this.baseDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("base_black"));
        this.moonDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("moon_black"));

        for (Planet planet : this.screen.planets) {
            this.add(this.getContainer(this.getPlanetImageButton(planet), planet)).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
        }
        this.add(this.getContainer(this.getUndoImageButton(), null)).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
    }

    public Container<ImageButton> getContainer(final ImageButton button, final Planet planet) {
        Container<ImageButton> buttonContainer = new Container<>(button);
        buttonContainer.width(this.buttonSize);
        buttonContainer.height(this.buttonSize);
        buttonContainer.setBackground(this.skin.getDrawable("panel_square"));

        if (planet != null) {
            buttonContainer.addListener(new PlanetToolTip(this.screen, planet, this.rocketDrawable, this.baseDrawable, this.moonDrawable));
        }

        return buttonContainer;
    }

    public ImageButton getPlanetImageButton(Planet planet) {
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
                if (!screen.planetDashboardButtonTable.isVisible) {
                    screen.addPlanetDashboardButton();
                }
            }
        });

        return planetImageButton;
    }

    public ImageButton getUndoImageButton() {
        TextureRegion undoTextureRegion = this.screen.iconsTextureAtlas.findRegion("undo");
        Drawable undoDrawable = new TextureRegionDrawable(undoTextureRegion);

        ImageButton.ImageButtonStyle undoButtonStyle = new ImageButton.ImageButtonStyle();
        undoButtonStyle.imageUp = undoDrawable;
        undoButtonStyle.imageDown = undoDrawable;
        ImageButton undoImageButton = new ImageButton(undoButtonStyle);
        undoImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (screen.planetDashboardButtonTable.isVisible) {
                    screen.removePlanetDashboardButton();
                }
                screen.entityBeingFollowed = null;
                screen.camera.reset();
            }
        });

        return undoImageButton;
    }
}
