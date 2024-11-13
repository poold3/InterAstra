package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Planet;
import io.github.interastra.models.Rocket;
import io.github.interastra.models.RocketInOrbit;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;
import io.github.interastra.tooltips.RocketToolTip;

import java.util.ArrayList;

public class PlanetDashboardTable extends Table {
    public static final float DASHBOARD_BUTTON_WIDTH = 100f;
    public static final float DASHBOARD_BUTTON_HEIGHT = 40f;
    public static final float ROCKET_LABEL_WIDTH = 120f;
    public static final float ROCKET_LABEL_HEIGHT = 70f;

    private final GameScreen screen;
    private final Skin skin;
    public boolean isVisible = false;
    public Planet planet;

    public Label titleLabel;
    public ArrayList<String> bases = new ArrayList<>();
    public ArrayList<RocketInOrbit> rockets = new ArrayList<>();
    public Label basesLabel;
    public Table rocketsTable;
    public Drawable viewDrawable;
    public Drawable sendDrawable;

    public PlanetDashboardTable(final GameScreen screen, final Skin skin) {
        super();
        this.screen = screen;
        this.skin = skin;

        this.viewDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("view"));
        this.sendDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("send"));

        this.setWidth(this.screen.stageViewport.getWorldWidth() / 2f);
        this.setHeight(this.screen.stageViewport.getWorldHeight() - PlanetsTable.MAX_BUTTON_SIZE - (PlanetsTable.BUTTON_PAD) * 2f);
        this.setPosition(
            (this.screen.stageViewport.getWorldWidth() / 2f) - (this.getWidth() / 2f),
            (PlanetsTable.MAX_BUTTON_SIZE + (PlanetsTable.BUTTON_PAD * 2f))
        );
        this.top();
        Drawable background = this.skin.getDrawable("panel_square_screws");
        this.setBackground(background);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        titleLabelStyle.font = this.skin.getFont("Teko-48");

        this.titleLabel = new Label("", titleLabelStyle);
        this.add(titleLabel).expandX().center().pad(5f);
        this.row();

        ScrollPane scrollPane = new ScrollPane(this.getPlanetDetailsTable(), this.skin);
        scrollPane.addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (getStage() != null) {
                    getStage().setScrollFocus(scrollPane);
                }
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (getStage() != null) {
                    getStage().setScrollFocus(null);
                }
            }
        });
        this.add(scrollPane).grow().padTop(10f);
    }

    public Table getPlanetDetailsTable() {
        Table planetDetailsTable = new Table();
        planetDetailsTable.top();

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        titleLabelStyle.font = this.skin.getFont("Teko-32");

        planetDetailsTable.add(new Label("Build Actions", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        planetDetailsTable.row();

        planetDetailsTable.add(this.getBuildTextButtons()).expandX().center();
        planetDetailsTable.row();

        planetDetailsTable.add(new Label("Bases Built", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        planetDetailsTable.row();

        this.basesLabel = new Label("Unknown", this.skin);
        planetDetailsTable.add(basesLabel).colspan(2).expandX().center().pad(5f);
        planetDetailsTable.row();

        planetDetailsTable.add(new Label("Rockets In Orbit", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        planetDetailsTable.row();

        this.rocketsTable = new Table();
        this.rocketsTable.center().top();
        this.rocketsTable.add(new Label("Unknown", this.skin)).expandX().center().pad(5f);

        planetDetailsTable.add(this.rocketsTable).colspan(2).expandX().center();

        return planetDetailsTable;
    }

    public Table getBuildTextButtons() {
        TextButton buildBaseTextButton = new TextButton("Base", this.skin);
        buildBaseTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        buildBaseTextButton.addListener(new ColorTextTooltip(Planet.BASE_PRICE.toString(), new InstantTooltipManager(), this.skin, Color.BLACK));

        TextButton[] rocketButtons = new TextButton[4];
        for (int i = 0; i < rocketButtons.length; ++i) {
            TextButton buildRocketTextButton = new TextButton("Rocket " + Rocket.ROCKET_TIER_STRING[i], this.skin);
            buildRocketTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                }
            });
            buildRocketTextButton.addListener(new RocketToolTip(screen, i + 1));
            rocketButtons[i] = buildRocketTextButton;
        }

        Table buildRocketTextButtonsTable = new Table();
        buildRocketTextButtonsTable.add(buildBaseTextButton).colspan(2).size(DASHBOARD_BUTTON_WIDTH, DASHBOARD_BUTTON_HEIGHT).pad(5f);
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

    public void setPlanet(final Planet planet) {
        this.planet = planet;
    }

    public boolean needToUpdateRockets() {
        if (this.rockets.size() != this.planet.rocketsInOrbit.size()) {
            return true;
        }

        for (int i = 0; i < this.rockets.size(); ++i) {
            if (!this.rockets.get(i).equals(this.planet.rocketsInOrbit.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void addRocketRow(final RocketInOrbit rocket) {
        Table rocketTable = new Table();

        Label rocketLabel = new Label(String.format("Tier %s", Rocket.ROCKET_TIER_STRING[rocket.tier - 1]), this.skin);
        rocketLabel.setAlignment(Align.center);
        rocketTable.add(rocketLabel).colspan(2).height(ROCKET_LABEL_HEIGHT / 2).width(ROCKET_LABEL_WIDTH / 2f);

        ImageButton viewRocketButton = new ImageButton(this.viewDrawable);
        viewRocketButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.entityBeingFollowed = rocket;
                screen.camera.targetZoom = screen.camera.getZoomForSize(5f);
                screen.removePlanetDashboardButton();
                screen.togglePlanetDashboard();
            }
        });
        viewRocketButton.addListener(new ColorTextTooltip("View", new InstantTooltipManager(), this.skin, Color.BLACK));
        rocketTable.add(viewRocketButton).size(ROCKET_LABEL_WIDTH / 4f);

        if (rocket.playerName.equals(this.screen.myPlayer.name)) {
            ImageButton sendRocketButton = new ImageButton(this.sendDrawable);
            sendRocketButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    screen.planetsTable.rocketToSend = rocket;
                }
            });
            sendRocketButton.addListener(new ColorTextTooltip(Rocket.ROCKET_TIER_FUEL_PRICE[rocket.tier - 1].toString(), new InstantTooltipManager(), this.skin, Color.BLACK));
            rocketTable.add(sendRocketButton).size(ROCKET_LABEL_WIDTH / 4f);
        } else {
            rocketTable.add().size(ROCKET_LABEL_WIDTH / 4f);
        }

        rocketTable.row();

        ColorLabel playerLabel = new ColorLabel(rocket.playerName, this.skin, Color.BLACK);
        playerLabel.setAlignment(Align.center);
        playerLabel.setEllipsis(true);
        rocketTable.add(playerLabel).colspan(4).height(ROCKET_LABEL_HEIGHT / 2f).width(ROCKET_LABEL_WIDTH);

        Container<Table> rocketContainer = new Container<>(rocketTable);
        rocketContainer.size(ROCKET_LABEL_WIDTH, ROCKET_LABEL_HEIGHT);
        rocketContainer.setBackground(this.skin.getDrawable("button_square_header_notch_rectangle"));

        this.rocketsTable.add(rocketContainer).pad(5f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!this.titleLabel.getText().equalsIgnoreCase(this.planet.name)) {
            this.titleLabel.setText(this.planet.name);
        }

        if (this.planet.isVisible) {
            if (this.bases.size() != this.planet.bases.size()) {
                this.bases = new ArrayList<>(this.planet.bases);
                this.basesLabel.setText(this.bases.toString());
            }
            if (this.needToUpdateRockets()) {
                this.rockets.clear();
                this.rocketsTable.clear();
                this.rockets = new ArrayList<>(this.planet.rocketsInOrbit);
                for (int i = 0; i < this.rockets.size(); ++i) {
                    this.addRocketRow(this.rockets.get(i));
                    if (i % 2 == 1) {
                        this.rocketsTable.row();
                    }
                }
            }

        } else {
            if (!this.bases.isEmpty()) {
                this.bases.clear();
            }
            this.basesLabel.setText("Unknown");
            if (!this.rockets.isEmpty()) {
                this.rockets.clear();
            }
            this.rocketsTable.clear();
            this.rocketsTable.add(new Label("Unknown", this.skin)).expandX().center().pad(5f);
        }
    }
}
