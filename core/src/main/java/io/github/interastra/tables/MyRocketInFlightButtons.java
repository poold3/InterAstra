package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.RocketInFlight;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.tooltips.*;

import java.util.logging.Level;

public class MyRocketInFlightButtons extends Table {
    private final GameScreen screen;
    private final RocketInFlight rocket;
    public Drawable viewDrawable;
    public ImageButton viewRocketButton;
    public ColorLabel rocketArrivalLabel;

    public MyRocketInFlightButtons(final GameScreen screen, final Skin skin, final RocketInFlight rocket) {
        super();
        this.screen = screen;
        this.rocket = rocket;
        this.viewDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("view"));

        this.viewRocketButton = new ImageButton(this.viewDrawable);
        viewRocketButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.entityBeingFollowed = rocket;
                    screen.camera.targetZoom = screen.camera.getZoomForSize(5f);
                    screen.removePlanetDashboardButton();
                    screen.togglePlanetDashboard();
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        viewRocketButton.addListener(new ColorTextTooltip("View Rocket", new InstantTooltipManager(), skin, Color.BLACK));
        this.add(this.viewRocketButton).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f, PlanetDashboardTable.ROCKET_LABEL_HEIGHT / 2f);

        this.rocketArrivalLabel = new ColorLabel("", skin, Color.BLACK);
        this.rocketArrivalLabel.setAlignment(Align.center);
        this.add(this.rocketArrivalLabel).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 3f, PlanetDashboardTable.ROCKET_LABEL_HEIGHT / 2f);
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
            this.rocketArrivalLabel.setText(Math.round(this.rocket.arrivalTimer));
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
