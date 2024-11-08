package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.models.Planet;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

import java.util.ArrayList;

public class PlanetDashboardTable extends Table {
    public static final float DASHBOARD_BUTTON_WIDTH = 100f;
    public static final float DASHBOARD_BUTTON_HEIGHT = 40f;

    private final GameScreen screen;
    private final Skin skin;
    public boolean isVisible = false;
    public Planet planet;

    public Label titleLabel;
    public ScrollPane scrollPane;
    public TextButton buildBaseTextButton;
    public TextButton buildRocketTextButton;
    public ArrayList<String> bases = new ArrayList<>();
    public Label basesLabel;

    public PlanetDashboardTable(final GameScreen screen, final Skin skin) {
        super();
        this.screen = screen;
        this.skin = skin;

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
        titleLabelStyle.font = this.skin.getFont("Teko-32");

        this.titleLabel = new Label("", titleLabelStyle);
        this.add(titleLabel).expandX().center().pad(5f);
        this.row();

        Table planetDetailsTable = new Table();
        planetDetailsTable.top();

        this.setBuildBaseTextButton();
        planetDetailsTable.add(this.buildBaseTextButton).maxWidth(DASHBOARD_BUTTON_WIDTH).maxHeight(DASHBOARD_BUTTON_HEIGHT).pad(5f);
        this.setBuildRocketTextButton();
        planetDetailsTable.add(this.buildRocketTextButton).maxWidth(DASHBOARD_BUTTON_WIDTH).maxHeight(DASHBOARD_BUTTON_HEIGHT).pad(5f);
        planetDetailsTable.row();

        planetDetailsTable.add(new Label("Bases", titleLabelStyle)).colspan(2).expandX().center().pad(5f);
        planetDetailsTable.row();

        this.basesLabel = new Label("Unknown", this.skin);
        planetDetailsTable.add(basesLabel).colspan(2).expandX().center().pad(5f);

        this.scrollPane = new ScrollPane(planetDetailsTable, this.skin);
        this.scrollPane.addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(scrollPane);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                getStage().setScrollFocus(null);
            }
        });
        this.add(this.scrollPane).grow().pad(10f);
    }

    public void setBuildBaseTextButton() {
        this.buildBaseTextButton = new TextButton("Build Base", this.skin);
        this.buildBaseTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.notificationTable.setMessage("Building a base!");
            }
        });
        this.buildBaseTextButton.addListener(new ColorTextTooltip("Build Base", new InstantTooltipManager(), this.skin, Color.BLACK));
    }

    public void setBuildRocketTextButton() {
        this.buildRocketTextButton = new TextButton("Build Rocket", this.skin);
        this.buildRocketTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.notificationTable.setMessage("Building a rocket!");
            }
        });
        this.buildRocketTextButton.addListener(new ColorTextTooltip("Build a rocket!", new InstantTooltipManager(), this.skin, Color.BLACK));
    }

    public void setPlanet(final Planet planet) {
        this.planet = planet;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!this.titleLabel.getText().equalsIgnoreCase(this.planet.name)) {
            this.titleLabel.setText(this.planet.name);
        }

        if (this.planet.isVisible && this.bases.size() != this.planet.bases.size()) {
            this.bases = new ArrayList<>(this.planet.bases);
            StringBuilder builder = new StringBuilder();
            for (String base : this.bases) {
                builder.append(base).append(" ");
            }
            this.basesLabel.setText(builder);
        } else if (!this.planet.isVisible && !this.bases.isEmpty()) {
            this.bases.clear();
            this.basesLabel.setText("Unknown");
        }
    }
}
