package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.message.messages.RemoveRocketMessage;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.models.Planet;
import io.github.interastra.models.Rocket;
import io.github.interastra.models.RocketInOrbit;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.tooltips.*;

import java.util.logging.Level;

public class MyRocketInOrbitButtons extends Table {
    private final GameScreen screen;
    private final RocketInOrbit rocket;
    public Drawable sellDrawable;
    public ImageButton sellRocketButton;
    public Drawable sendDrawable;
    public ImageButton sendRocketButton;
    public Drawable upgradeDrawable;
    public Container<ImageButton> upgradeContainer;
    public ImageButton upgradeButton;
    public ColorLabel rocketCooldownLabel;
    public float updateTimer = 1f;
    public boolean needToUpdate = true;
    public boolean readyToSell = false;
    public boolean readyToUpgrade = false;

    public MyRocketInOrbitButtons(final GameScreen screen, final Skin skin, final RocketInOrbit rocket) {
        super();
        this.screen = screen;
        this.rocket = rocket;
        this.sellDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("money"));
        this.sendDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("send"));
        this.upgradeDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("upgrade"));

        this.sellRocketButton = new ImageButton(this.sellDrawable);
        sellRocketButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (readyToSell) {
                        screen.moneySound.play();
                        screen.myPlayer.balance += Rocket.ROCKET_TIER_STATS[rocket.tier - 1].sellPrice;
                        screen.removeRocket(new RemoveRocketMessage(new RocketMessageModel(rocket), rocket.orbitingPlanet.name));
                    } else {
                        readyToSell = true;
                        screen.notificationTable.setMessage("Press the sell button again to sell this rocket.");
                    }
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        sellRocketButton.addListener(new SellToolTip(screen, rocket));
        this.add(this.sellRocketButton).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);

        this.sendRocketButton = new ImageButton(this.sendDrawable);
        sendRocketButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (!Rocket.ROCKET_TIER_FUEL_PRICE[rocket.tier - 1].canAfford(screen) && !screen.noCostMode) {
                        screen.badSound.play(0.5f);
                        return;
                    }

                    screen.notificationTable.setMessage("Select an available planet.");
                    screen.planetsTable.setRocketToSend(rocket);
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        sendRocketButton.addListener(new SendToolTip(screen, rocket));

        this.upgradeContainer = new Container<>();
        this.upgradeContainer.size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);
        this.upgradeButton = new ImageButton(this.upgradeDrawable);
        upgradeButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (readyToUpgrade) {
                        screen.buildSound.play(0.05f);
                        RestService.addBase(screen, rocket.orbitingPlanet.name, screen.myPlayer.name, screen.myPlayer.bases, rocket.id);
                        rocket.orbitingPlanet.baseCooldown = Planet.BASE_COOLDOWN;
                    } else {
                        if (rocket.orbitingPlanet.hasMyBase) {
                            screen.badSound.play(0.5f);
                            screen.notificationTable.setMessage("You already have a base here.");
                            return;
                        } else if (rocket.orbitingPlanet.bases.size() >= rocket.orbitingPlanet.baseLimit) {
                            screen.badSound.play(0.5f);
                            screen.notificationTable.setMessage("No more bases can be built here.");
                            return;
                        }

                        if (!Planet.BASE_PRICE.canAfford(screen) && !screen.noCostMode) {
                            screen.badSound.play(0.5f);
                            return;
                        }

                        readyToUpgrade = true;
                        screen.notificationTable.setMessage("Press the upgrade button again to upgrade this rocket to a base.");
                    }
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        upgradeButton.addListener(new BaseToolTip(screen));

        this.rocketCooldownLabel = new ColorLabel("", skin, Color.BLACK);
        this.rocketCooldownLabel.setAlignment(Align.center);
        this.add(this.rocketCooldownLabel).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 3f, PlanetDashboardTable.ROCKET_LABEL_HEIGHT / 2f);
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);

            if (this.rocket.cooldown > 0f) {
                this.rocketCooldownLabel.setText((int) this.rocket.cooldown);
                return;
            }

            this.updateTimer += delta;
            if (this.updateTimer < 1f) {
                return;
            }

            this.updateTimer -= 1f;

            if (this.upgradeContainer.getActor() == null && !rocket.orbitingPlanet.hasMyBase && rocket.orbitingPlanet.bases.size() < rocket.orbitingPlanet.baseLimit) {
                this.upgradeContainer.setActor(this.upgradeButton);
            } else if (this.upgradeContainer.getActor() != null && (rocket.orbitingPlanet.hasMyBase || rocket.orbitingPlanet.bases.size() >= rocket.orbitingPlanet.baseLimit)) {
                this.upgradeContainer.clear();
            }

            if (this.needToUpdate) {
                this.needToUpdate = false;
                this.clear();
                this.add(this.sellRocketButton).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);
                this.add(this.sendRocketButton).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);
                this.add(this.upgradeContainer).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
