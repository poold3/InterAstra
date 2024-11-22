package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
    private final ColorLabel rocketsInOrbitIndicator;
    private int numRocketsInOrbit = 0;
    private final ColorLabel rocketsInFlightIndicator;
    private int numRocketsInFlight = 0;
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
        indicatorTable.add(this.baseIndicator).width(PLANET_SIZE / 2f).expand().bottom();

        this.rocketsInFlightIndicator = new ColorLabel("0", this.screen.skin, Color.WHITE);
        this.rocketsInFlightIndicator.setAlignment(Align.center);
        indicatorTable.add(this.rocketsInFlightIndicator).width(PLANET_SIZE / 4f).expand().bottom();

        this.rocketsInOrbitIndicator = new ColorLabel("0", this.screen.skin, Color.WHITE);
        this.rocketsInOrbitIndicator.setAlignment(Align.center);
        indicatorTable.add(this.rocketsInOrbitIndicator).width(PLANET_SIZE / 4f).expand().bottom();
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

        if (this.planet.numMyRockets != this.numRocketsInOrbit) {
            this.numRocketsInOrbit = this.planet.numMyRockets;
            this.rocketsInOrbitIndicator.setText(this.numRocketsInOrbit);
        }

        if (this.planet.rocketsInFlight.size() != this.numRocketsInFlight) {
            this.numRocketsInFlight = this.planet.rocketsInFlight.size();
            this.rocketsInFlightIndicator.setText(this.numRocketsInFlight);
        }
    }
}
