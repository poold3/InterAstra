package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.message.models.RocketMessageModel;
import io.github.interastra.models.*;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.RocketToolTip;

import java.util.ArrayList;
import java.util.UUID;

public class PlanetDashboardTable extends Dashboard {
    public static final float ROCKET_LABEL_WIDTH = 140f;
    public static final float ROCKET_LABEL_HEIGHT = 70f;

    public Planet planet;

    public ArrayList<String> bases = new ArrayList<>();
    public ArrayList<RocketInOrbit> rocketsInOrbit = new ArrayList<>();
    public ArrayList<RocketInFlight> rocketsInFlight = new ArrayList<>();
    public Label basesLabel;
    public Table rocketsInOrbitTable;
    public Table rocketsInFlightTable;
    public Drawable viewDrawable;
    public PlanetCooldownLabel buildBaseButton;
    private float updateTimer = 0f;

    public PlanetDashboardTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        this.viewDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("view"));

        this.buildBaseButton = new PlanetCooldownLabel(this.screen, this, this.skin);

        this.setPlanetDetails();
    }

    public void setPlanetDetails() {
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        titleLabelStyle.font = this.skin.getFont("Teko-32");

        this.contentTable.add(new Label("Build Actions", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        this.contentTable.row();

        this.contentTable.add(this.getBuildTextButtons()).expandX().center();
        this.contentTable.row();

        this.contentTable.add(new Label("Bases Built", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        this.contentTable.row();

        this.basesLabel = new Label("", this.skin);
        this.contentTable.add(basesLabel).colspan(2).expandX().center().pad(5f);
        this.contentTable.row();

        this.contentTable.add(new Label("Rockets In Orbit", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        this.contentTable.row();

        this.rocketsInOrbitTable = new Table();
        this.rocketsInOrbitTable.center().top();

        this.contentTable.add(this.rocketsInOrbitTable).colspan(2).expandX().center();
        this.contentTable.row();

        this.contentTable.add(new Label("Rockets In Flight", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        this.contentTable.row();

        this.rocketsInFlightTable = new Table();
        this.rocketsInFlightTable.center().top();

        this.contentTable.add(this.rocketsInFlightTable).colspan(2).expandX().center();
    }

    public Table getBuildTextButtons() {
        TextButton[] rocketButtons = new TextButton[4];
        for (int i = 0; i < rocketButtons.length; ++i) {
            TextButton buildRocketTextButton = new TextButton("Rocket " + Rocket.ROCKET_TIER_STRING[i], this.skin);
            int finalI = i;
            buildRocketTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
                @Override
                public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    if (cursor != null && screen.planetsTable.rocketToSend == null) {
                        float range = Rocket.ROCKET_TIER_STATS[finalI].range;
                        if (planet.moon != null) {
                            range += Moon.RANGE_INCREASE;
                        }
                        screen.planetsTable.setRangeFinder(planet, range);
                    }
                }

                @Override
                public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    if (pointer == -1 && cursor != null && screen.planetsTable.rocketToSend == null) {
                        screen.planetsTable.resetRangeFinder();
                    }
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!planet.hasMyBase) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("You must have a base to build a rocket here.");
                        return;
                    } else if (planet.baseCooldown > 0f) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("Cooldown in effect.");
                        return;
                    } else if (planet.rocketsInFlight.size() + planet.numMyRockets >= planet.baseLimit) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("Too many rockets at this planet.");
                        return;
                    }

                    if (!Rocket.ROCKET_TIER_PRICE[finalI].canAfford(screen.myPlayer) && !screen.noCostMode) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("You cannot afford this action.");
                        return;
                    }

                    screen.buildSound.play(0.05f);
                    RocketInFlight newRocket = new RocketInFlight(
                        screen,
                        new RocketMessageModel(UUID.randomUUID().toString(), screen.myPlayer.name, finalI + 1),
                        planet
                    );
                    planet.rocketsInFlight.add(newRocket);
                    Rocket.ROCKET_TIER_PRICE[finalI].purchase(screen.myPlayer);
                    planet.baseCooldown = Planet.BASE_COOLDOWN;
                }
            });
            buildRocketTextButton.addListener(new RocketToolTip(screen, i + 1));
            rocketButtons[i] = buildRocketTextButton;
        }

        Table buildRocketTextButtonsTable = new Table();
        buildRocketTextButtonsTable.add(this.buildBaseButton).colspan(2).pad(5f);
        buildRocketTextButtonsTable.row();

        for (int i = 0; i < 2; ++i) {
            buildRocketTextButtonsTable.add(rocketButtons[i]).size(DASHBOARD_BUTTON_WIDTH, DASHBOARD_BUTTON_HEIGHT).pad(5f);
        }
        buildRocketTextButtonsTable.row();
        for (int i = 2; i < 4; ++i) {
            buildRocketTextButtonsTable.add(rocketButtons[i]).size(DASHBOARD_BUTTON_WIDTH, DASHBOARD_BUTTON_HEIGHT).pad(5f);
        }
        return buildRocketTextButtonsTable;
    }

    public void addRocketRow(final Rocket rocket) {
        Table rocketTable = new Table();

        Label rocketLabel = new Label(String.format("Tier %s", Rocket.ROCKET_TIER_STRING[rocket.tier - 1]), this.skin);
        rocketLabel.setAlignment(Align.center);
        rocketTable.add(rocketLabel).colspan(3).height(ROCKET_LABEL_HEIGHT / 2).width(ROCKET_LABEL_WIDTH / 2f);

        if (rocket.getClass() == RocketInOrbit.class) {
            if (rocket.playerName.equals(this.screen.myPlayer.name)) {
                rocketTable.add(new MyRocketInOrbitButtons(this.screen, this.skin, (RocketInOrbit) rocket));
            } else {
                rocketTable.add().size(ROCKET_LABEL_WIDTH / 2f, ROCKET_LABEL_HEIGHT / 2f);
            }
        } else {
            rocketTable.add(new MyRocketInFlightButtons(this.screen, this.skin, (RocketInFlight) rocket));
        }

        rocketTable.row();

        ColorLabel playerLabel = new ColorLabel(rocket.playerName, this.skin, Color.BLACK);
        playerLabel.setAlignment(Align.center);
        playerLabel.setEllipsis(true);
        rocketTable.add(playerLabel).colspan(6).height(ROCKET_LABEL_HEIGHT / 2f).width(ROCKET_LABEL_WIDTH);

        Container<Table> rocketContainer = new Container<>(rocketTable);
        rocketContainer.size(ROCKET_LABEL_WIDTH, ROCKET_LABEL_HEIGHT);
        rocketContainer.setBackground(this.skin.getDrawable("button_square_header_notch_rectangle"));

        if (rocket.getClass() == RocketInOrbit.class) {
            this.rocketsInOrbitTable.add(rocketContainer).pad(5f);
        } else {
            this.rocketsInFlightTable.add(rocketContainer).pad(5f);
        }
    }

    public void setPlanet(final Planet planet) {
        this.planet = planet;
        this.rocketsInOrbit.clear();
        this.rocketsInOrbitTable.clear();
        this.bases.clear();
        this.basesLabel.setText("[]");
        this.updateTimer = 1f;
    }

    public boolean needToUpdateRocketsInOrbit() {
        if (this.rocketsInOrbit.size() != this.planet.rocketsInOrbit.size()) {
            return true;
        }

        for (int i = 0; i < this.rocketsInOrbit.size(); ++i) {
            if (!this.rocketsInOrbit.get(i).equals(this.planet.rocketsInOrbit.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean needToUpdateRocketsInFlight() {
        if (this.rocketsInFlight.size() != this.planet.rocketsInFlight.size()) {
            return true;
        }

        for (int i = 0; i < this.rocketsInFlight.size(); ++i) {
            if (!this.rocketsInFlight.get(i).equals(this.planet.rocketsInFlight.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.updateTimer += delta;
        if (this.updateTimer < 1f) {
            return;
        }

        this.updateTimer -= 1f;

        if (!this.titleLabel.getText().equalsIgnoreCase(this.planet.name)) {
            this.titleLabel.setText(this.planet.name);
        }

        if (this.planet.isVisible) {
            if (this.bases.size() != this.planet.bases.size() || this.basesLabel.textEquals("Unknown")) {
                this.bases = new ArrayList<>(this.planet.bases);
                this.basesLabel.setText(this.bases.toString());
            }
            if (this.needToUpdateRocketsInOrbit()) {
                this.rocketsInOrbit.clear();
                this.rocketsInOrbitTable.clear();
                this.rocketsInOrbit = new ArrayList<>(this.planet.rocketsInOrbit);
                for (int i = 0; i < this.rocketsInOrbit.size(); ++i) {
                    this.addRocketRow(this.rocketsInOrbit.get(i));
                    if (i % 2 == 1) {
                        this.rocketsInOrbitTable.row();
                    }
                }
            }
        } else {
            if (!this.bases.isEmpty()) {
                this.bases.clear();
            }
            this.basesLabel.setText("Unknown");
            if (!this.rocketsInOrbit.isEmpty()) {
                this.rocketsInOrbit.clear();
            }
            this.rocketsInOrbitTable.clear();
            this.rocketsInOrbitTable.add(new Label("Unknown", this.skin)).expandX().center().pad(5f);
        }

        if (this.needToUpdateRocketsInFlight()) {
            this.rocketsInFlight.clear();
            this.rocketsInFlightTable.clear();
            this.rocketsInFlight = new ArrayList<>(this.planet.rocketsInFlight);
            for (int i = 0; i < this.rocketsInFlight.size(); ++i) {
                if (this.rocketsInFlight.get(i).arrived) {
                    continue;
                }
                this.addRocketRow(this.rocketsInFlight.get(i));
                if (i % 2 == 1) {
                    this.rocketsInFlightTable.row();
                }
            }
        }
    }
}
