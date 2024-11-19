package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.Planet;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

public class BuildBaseButton extends Container<Actor> {
    private final GameScreen screen;
    private final PlanetDashboardTable planetDashboardTable;
    public TextButton buildBaseTextButton;
    public ColorLabel baseCooldownLabel;

    public BuildBaseButton(final GameScreen screen, final PlanetDashboardTable planetDashboardTable, final Skin skin) {
        super();
        this.size(PlanetDashboardTable.DASHBOARD_BUTTON_WIDTH, PlanetDashboardTable.DASHBOARD_BUTTON_HEIGHT);
        this.pad(5f);
        this.screen = screen;
        this.planetDashboardTable = planetDashboardTable;

        this.buildBaseTextButton = new TextButton("Base", skin);
        buildBaseTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (planetDashboardTable.planet.numMyRockets <= 0) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You must have a rocket in orbit to build a base here.");
                    return;
                } else if (planetDashboardTable.planet.hasMyBase) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You already have a base here.");
                    return;
                } else if (planetDashboardTable.planet.bases.size() >= planetDashboardTable.planet.baseLimit) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("No more bases can be built here.");
                    return;
                }

                if (!Planet.BASE_PRICE.canAfford(screen.myPlayer) && !screen.noCostMode) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You cannot afford this action.");
                    return;
                }

                screen.buildSound.play(0.01f);
                RestService.addBase(screen, planetDashboardTable.planet.name, screen.myPlayer.name, screen.myPlayer.bases);
                planetDashboardTable.planet.baseCooldown = Planet.BASE_COOLDOWN;
            }
        });
        buildBaseTextButton.addListener(new ColorTextTooltip("Price: " + Planet.BASE_PRICE.toString(), new InstantTooltipManager(), skin, Color.BLACK));

        this.baseCooldownLabel = new ColorLabel("", skin, Color.WHITE);
        this.baseCooldownLabel.setAlignment(Align.center);

        this.setActor(this.baseCooldownLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.planetDashboardTable.planet.baseCooldown > 0f) {
            if (this.getActor().equals(this.buildBaseTextButton)) {
                this.setActor(this.baseCooldownLabel);
            }
            this.baseCooldownLabel.setText(String.format("%.0f", this.planetDashboardTable.planet.baseCooldown));
        } else if (!this.planetDashboardTable.planet.hasMyBase && this.getActor().equals(this.baseCooldownLabel)) {
            this.setActor(this.buildBaseTextButton);
        } else if (this.planetDashboardTable.planet.hasMyBase) {
            if (this.getActor().equals(this.buildBaseTextButton)) {
                this.setActor(this.baseCooldownLabel);
            }
            this.baseCooldownLabel.setText("");
        }
    }
}
