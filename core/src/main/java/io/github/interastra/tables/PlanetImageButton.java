package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Planet;
import io.github.interastra.screens.GameScreen;

public class PlanetImageButton extends Stack {
    public static float PLANET_SIZE = 70f;
    public static float BASE_SIZE = 23;

    private final GameScreen screen;
    private final Planet planet;
    private final ImageButton planetImageButton;
    private final Container<Image> baseIndicator;
    private boolean hasMyBase = false;
    private final ColorLabel rocketIndicator;
    private int numMyRockets = 0;
    private float updateTimer = 0f;

    public PlanetImageButton(final GameScreen screen, final Planet planet) {
        super();
        this.screen = screen;
        this.planet = planet;

        Drawable planetDrawable = new TextureRegionDrawable(this.screen.planetsTextureAtlas.findRegion("planet", planet.index));
        Drawable blockDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("block"));

        ImageButton.ImageButtonStyle planetButtonStyle = new ImageButton.ImageButtonStyle();
        planetButtonStyle.imageUp = planetDrawable;
        planetButtonStyle.imageDisabled = blockDrawable;
        this.planetImageButton = new ImageButton(planetButtonStyle);
        Container<ImageButton> planetImageButtonContainer = new Container<>(this.planetImageButton);
        planetImageButtonContainer.size(PLANET_SIZE);
        this.add(planetImageButtonContainer);

        Table indicatorTable = new Table();
        this.baseIndicator = new Container<>();
        this.baseIndicator.size(BASE_SIZE);
        indicatorTable.add(this.baseIndicator).expand().center().bottom();

        this.rocketIndicator = new ColorLabel("0", this.screen.skin, Color.WHITE);
        indicatorTable.add(this.rocketIndicator).expand().center().bottom();
        this.add(indicatorTable);
    }

    public boolean isDisabled() {
        return this.planetImageButton.isDisabled();
    }

    public void setDisabled(boolean disabled) {
        this.planetImageButton.setDisabled(disabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.updateTimer += delta;
        if (this.updateTimer < 1f) {
            return;
        }

        this.updateTimer -= 1f;
        if (!this.hasMyBase && this.planet.hasMyBase) {
            Image baseImage = new Image(new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("base")));
            this.baseIndicator.setActor(baseImage);
            this.hasMyBase = true;
        }

        if (this.planet.numMyRockets != this.numMyRockets) {
            this.numMyRockets = this.planet.numMyRockets;
            this.rocketIndicator.setText(this.numMyRockets);
        }
    }
}
