package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.interastra.models.Planet;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.CameraOperatorService;
import io.github.interastra.services.ClickListenerService;

public class PlanetsTable extends Table {
    public static final float MAX_BUTTON_SIZE = 50f;
    public static final float BUTTON_PAD = 1f;

    private final GameScreen screen;
    private final Skin skin;
    private final float buttonSize;
    private final Container<ImageButton> arrowContainer;
    private final Drawable arrowDownDrawable;
    private final Drawable arrowUpDrawable;
    private float targetYPosition = 0f;

    public PlanetsTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;
        int numButtons = screen.planets.size() + 1;
        this.buttonSize = Math.min(((this.screen.stageViewport.getWorldWidth() / 2f) - (numButtons * 2f * BUTTON_PAD)) / numButtons, MAX_BUTTON_SIZE);

        this.setFillParent(true);
        this.bottom();

        this.arrowDownDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("arrow_drop_down"));
        this.arrowUpDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("arrow_drop_up"));
        this.arrowContainer = this.getArrowButton();
        this.add(arrowContainer).colspan(numButtons).center().padBottom(BUTTON_PAD * 2f);
        this.row();

        for (Planet planet : this.screen.planets) {
            this.add(this.getContainer(this.getPlanetImageButton(planet), planet.name)).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
        }
        this.add(this.getContainer(this.getUndoImageButton(), null)).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
    }

    public Container<ImageButton> getArrowButton() {
        float height = this.buttonSize / 5f;

        ImageButton.ImageButtonStyle arrowButtonStyle = new ImageButton.ImageButtonStyle();
        arrowButtonStyle.imageUp = this.arrowDownDrawable;
        arrowButtonStyle.imageDown = this.arrowDownDrawable;
        ImageButton arrowImageButton = new ImageButton(arrowButtonStyle);
        arrowImageButton.getImageCell().pad(height * -1f + 2f);
        arrowImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ImageButton.ImageButtonStyle style = arrowContainer.getActor().getStyle();
                if (targetYPosition != 0f) {
                    style.imageUp = arrowDownDrawable;
                    style.imageDown = arrowDownDrawable;
                    targetYPosition = 0f;
                } else {
                    style.imageUp = arrowUpDrawable;
                    style.imageDown = arrowUpDrawable;
                    targetYPosition = buttonSize * -1f;
                }
            }
        });

        Container<ImageButton> arrowContainer = new Container<>(arrowImageButton);
        arrowContainer.width(this.buttonSize);
        arrowContainer.height(this.buttonSize / 5f);
        arrowContainer.setBackground(this.skin.getDrawable("panel_rectangle"));

        return arrowContainer;
    }

    public Container<ImageButton> getContainer(final ImageButton button, final String text) {
        Container<ImageButton> buttonContainer = new Container<>(button);
        buttonContainer.width(this.buttonSize);
        buttonContainer.height(this.buttonSize);
        buttonContainer.setBackground(this.skin.getDrawable("panel_square"));

        if (text != null) {
            TooltipManager tooltipManager = new TooltipManager();
            tooltipManager.instant();
            tooltipManager.initialTime = 0f;
            tooltipManager.subsequentTime = 0f;

            TextTooltip.TextTooltipStyle buttonNameStyle = new TextTooltip.TextTooltipStyle(
                this.skin.get(TextTooltip.TextTooltipStyle.class)
            );
            buttonNameStyle.label.font = this.skin.getFont("Teko-32");
            TextTooltip buttonName = new TextTooltip(text, tooltipManager, buttonNameStyle);
            buttonContainer.addListener(buttonName);
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
                screen.entityBeingFollowed = null;
                screen.camera.reset();
            }
        });

        return undoImageButton;
    }

    @Override
    public void act(float delta) {
        if (Math.abs(this.getY() - this.targetYPosition) > 0.1f) {
            this.setY(CameraOperatorService.floatLerp(this.getY(), this.targetYPosition, 0.3f));
        }
    }
}
