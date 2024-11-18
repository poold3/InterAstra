package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.interastra.message.messages.RemoveRocketMessage;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.models.*;
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
    public Drawable blockDrawable;
    public RocketInOrbit rocketToSend = null;
    public ImageButton[] planetButtons;

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
        this.blockDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("block"));

        this.planetButtons = new ImageButton[this.screen.planets.size()];
        for (int i = 0; i < this.screen.planets.size(); ++i) {
            ImageButton planetImageButton = this.getPlanetImageButton(this.screen.planets.get(i));
            this.planetButtons[i] = planetImageButton;
            this.add(this.getContainer(planetImageButton, this.screen.planets.get(i))).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
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
        Drawable planetDrawable = new TextureRegionDrawable(this.screen.planetsTextureAtlas.findRegion("planet", planet.index));

        ImageButton.ImageButtonStyle planetButtonStyle = new ImageButton.ImageButtonStyle();
        planetButtonStyle.imageUp = planetDrawable;
        planetButtonStyle.imageDown = planetDrawable;
        planetButtonStyle.imageDisabled = this.blockDrawable;
        ImageButton planetImageButton = new ImageButton(planetButtonStyle);
        planetImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (planetImageButton.isDisabled()) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Out of range.");
                    return;
                }

                if (rocketToSend != null) {
                    // Make sure rocket is not already in flight
                    for (RocketInFlight rocket : screen.rocketsInFlight) {
                        if (rocket.id.equals(rocketToSend.id)) {
                            return;
                        }
                    }

                    // Check rocket limit
                    if (screen.getNumRocketsInFlightToPlanet(planet) + planet.numMyRockets >= planet.baseLimit) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("Too many rockets at this planet.");
                        return;
                    }

                    // Purchase flight
                    Rocket.ROCKET_TIER_FUEL_PRICE[rocketToSend.tier - 1].purchase(screen.myPlayer);

                    // Remove rocket
                    screen.removeRocket(new RemoveRocketMessage(new RocketMessageModel(rocketToSend), rocketToSend.orbitingPlanet.name));

                    // Add flight rocket
                    RocketInFlight rocketInFlight = new RocketInFlight(
                        screen,
                        new RocketMessageModel(rocketToSend),
                        rocketToSend.orbitingPlanet,
                        planet
                    );

                    screen.rocketsInFlight.add(rocketInFlight);

                    rocketToSend = null;
                    resetPlanetImageButtons();
                    return;
                }

                trackPlanet(planet);
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
                screen.notificationTable.clearNotification();
                // Are we in send rocket mode?
                if (rocketToSend != null) {
                    rocketToSend = null;
                    resetPlanetImageButtons();
                }
                // Are we viewing the trade table?
                else if (screen.transferTable.isVisible) {
                    screen.toggleTransferTable();
                }
                // Are we viewing the buy/sell table?
                else if (screen.buySellTable.isVisible) {
                    screen.toggleBuySellTable();
                }
                // Are we viewing a planet dashboard?
                else if (screen.planetDashboardTable.isVisible) {
                    screen.togglePlanetDashboard();
                }
                // Are we viewing a rocket in orbit?
                else if (screen.entityBeingFollowed != null && screen.entityBeingFollowed.getClass() == RocketInOrbit.class && ((RocketInOrbit) screen.entityBeingFollowed).inOrbit()) {
                    trackPlanet(((RocketInOrbit) screen.entityBeingFollowed).orbitingPlanet);
                }
                // Are we viewing a rocket in flight?
                else if (screen.entityBeingFollowed != null && screen.entityBeingFollowed.getClass() == RocketInFlight.class && (!((RocketInFlight) screen.entityBeingFollowed).arrived)) {
                    trackPlanet(((RocketInFlight) screen.entityBeingFollowed).destinationPlanet);
                }
                // Reset camera. Make sure planet dashboard button is removed.
                else {
                    screen.removePlanetDashboardButton();
                    screen.entityBeingFollowed = null;
                    screen.camera.reset();
                }
            }
        });

        return undoImageButton;
    }

    public void trackPlanet(final Planet planet) {
        if (screen.transferTable.isVisible) {
            screen.toggleTransferTable();
        } else if (screen.buySellTable.isVisible) {
            screen.toggleBuySellTable();
        }
        screen.entityBeingFollowed = planet;
        screen.camera.targetZoom = screen.camera.getZoomForSize(planet.getWidth() * 3f);
        if (!screen.planetDashboardButtonTable.isVisible) {
            screen.addPlanetDashboardButton();
        }
        if (screen.planetDashboardTable.isVisible) {
            screen.planetDashboardTable.setPlanet(planet);
        }
    }

    public void resetPlanetImageButtons() {
        for (ImageButton planetImageButton : this.planetButtons) {
            planetImageButton.setDisabled(false);
        }
    }

    public float distanceBetweenPlanets(final Planet planetOne, final Planet planetTwo) {
        return (float) Math.sqrt(Math.pow(planetOne.getX() - planetTwo.getX(), 2) + Math.pow(planetOne.getY() - planetTwo.getY(), 2));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.rocketToSend == null) {
            return;
        }

        float range = Rocket.ROCKET_TIER_STATS[this.rocketToSend.tier - 1].range;
        if (this.rocketToSend.orbitingPlanet.moon != null) {
            range += Moon.RANGE_INCREASE;
        }

        for (int i = 0; i < this.planetButtons.length; ++i) {
            ImageButton planetButton = this.planetButtons[i];
            planetButton.setDisabled(
                this.distanceBetweenPlanets(this.rocketToSend.orbitingPlanet, this.screen.planets.get(i)) > range
                    || this.rocketToSend.orbitingPlanet.equals(this.screen.planets.get(i))
            );
        }
    }
}
