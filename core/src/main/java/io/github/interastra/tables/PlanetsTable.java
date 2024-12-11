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
    public static final float MAX_BUTTON_SIZE = 70f;
    public static final float BUTTON_PAD = 1f;

    private final GameScreen screen;
    public Drawable moonDrawable;
    public RocketInOrbit rocketToSend = null;

    public Planet rangeFinderOrigin = null;
    public Float rangeFinderRange = null;
    public PlanetImageButton[] planetButtons;

    public PlanetsTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        int numButtons = screen.planets.size() + 1;
        float buttonSize = Math.min(((this.screen.stageViewport.getWorldWidth() / 2f) - (numButtons * 2f * BUTTON_PAD)) / numButtons, MAX_BUTTON_SIZE);

        this.setFillParent(true);
        this.bottom();

        this.moonDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("moon_black"));

        this.planetButtons = new PlanetImageButton[this.screen.planets.size()];
        for (int i = 0; i < this.screen.planets.size(); ++i) {
            Planet planet = this.screen.planets.get(i);
            PlanetImageButton planetImageButton = new PlanetImageButton(this.screen, planet);
            this.planetButtons[i] = planetImageButton;

            Container<PlanetImageButton> planetImageButtonContainer = new Container<>(planetImageButton);
            planetImageButtonContainer.size(buttonSize);
            planetImageButtonContainer.setBackground(skin.getDrawable("panel_square"));
            planetImageButtonContainer.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (planetImageButton.isDisabled()) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("Out of range.");
                        return;
                    }

                    if (rocketToSend != null) {
                        // Make sure rocket is not already in flight
                        for (RocketInFlight rocket : planet.rocketsInFlight) {
                            if (rocket.equals(rocketToSend)) {
                                return;
                            }
                        }

                        // Check rocket limit
                        if (planet.rocketsInFlight.size() + planet.numMyRockets >= planet.baseLimit) {
                            screen.badSound.play(0.5f);
                            screen.notificationTable.setMessage("Too many rockets at this planet.");
                            return;
                        }

                        // Purchase flight
                        Rocket.ROCKET_TIER_FUEL_PRICE[rocketToSend.tier - 1].purchase(screen);

                        // Remove rocket
                        screen.removeRocket(new RemoveRocketMessage(new RocketMessageModel(rocketToSend), rocketToSend.orbitingPlanet.name));

                        // Add flight rocket
                        RocketInFlight rocketInFlight = new RocketInFlight(
                            screen,
                            new RocketMessageModel(rocketToSend),
                            rocketToSend.orbitingPlanet,
                            planet
                        );

                        planet.rocketsInFlight.add(rocketInFlight);

                        rocketToSend = null;
                        resetRangeFinder();
                        return;
                    }

                    trackPlanet(planet);
                }
            });
            planetImageButtonContainer.addListener(new PlanetToolTip(this.screen, this.screen.planets.get(i), this.moonDrawable));
            this.add(planetImageButtonContainer).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
        }
        Container<ImageButton> undoImageButtonContainer = new Container<>(this.getUndoImageButton());
        undoImageButtonContainer.size(buttonSize);
        undoImageButtonContainer.setBackground(skin.getDrawable("panel_square"));
        this.add(undoImageButtonContainer).pad(0f, BUTTON_PAD, 0f, BUTTON_PAD);
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
                    resetRangeFinder();
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
        screen.entityBeingFollowed = planet;
        screen.camera.targetZoom = screen.camera.getZoomForSize(planet.getWidth() * 3f);
        if (!screen.planetDashboardButtonTable.isVisible) {
            screen.addPlanetDashboardButton();
        }
        if (screen.planetDashboardTable.isVisible) {
            screen.planetDashboardTable.setPlanet(planet);
        }

        if (screen.transferTable.isVisible) {
            screen.toggleTransferTable();
            screen.togglePlanetDashboard();
        } else if (screen.buySellTable.isVisible) {
            screen.toggleBuySellTable();
            screen.togglePlanetDashboard();
        } else if (screen.infoTable.isVisible) {
            screen.toggleInfoTable();
            screen.togglePlanetDashboard();
        }
    }

    public void resetPlanetImageButtons() {
        for (PlanetImageButton planetImageButton : this.planetButtons) {
            planetImageButton.setDisabled(false);
        }
    }

    public float distanceBetweenPlanets(final Planet planetOne, final Planet planetTwo) {
        return (float) Math.sqrt(Math.pow(planetOne.getX() - planetTwo.getX(), 2) + Math.pow(planetOne.getY() - planetTwo.getY(), 2));
    }

    public void setRocketToSend(final RocketInOrbit rocketToSend) {
        this.rocketToSend = rocketToSend;
        float newRange = Rocket.ROCKET_TIER_STATS[this.rocketToSend.tier - 1].range;
        if (this.rocketToSend.orbitingPlanet.moon != null) {
            newRange += Moon.RANGE_INCREASE;
        }
        this.setRangeFinder(this.rocketToSend.orbitingPlanet, newRange);
    }

    public void setRangeFinder(final Planet origin, final float range) {
        this.rangeFinderOrigin = origin;
        this.rangeFinderRange = range;
    }

    public void resetRangeFinder() {
        this.rangeFinderOrigin = null;
        this.rangeFinderRange = null;
        this.resetPlanetImageButtons();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.rangeFinderOrigin == null || this.rangeFinderRange == null) {
            return;
        }

        for (int i = 0; i < this.planetButtons.length; ++i) {
            PlanetImageButton planetButton = this.planetButtons[i];
            planetButton.setDisabled(
                this.distanceBetweenPlanets(this.rangeFinderOrigin, this.screen.planets.get(i)) > this.rangeFinderRange
                    || this.rangeFinderOrigin.equals(this.screen.planets.get(i))
            );
        }
    }
}
