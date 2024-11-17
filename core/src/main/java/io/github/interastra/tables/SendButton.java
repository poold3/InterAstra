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
import io.github.interastra.models.Rocket;
import io.github.interastra.models.RocketInOrbit;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

public class SendButton extends Table {
    private final GameScreen screen;
    private final RocketInOrbit rocket;
    public Drawable sendDrawable;
    public ImageButton sendRocketButton;
    public ColorLabel rocketCooldownLabel;
    public boolean inCooldown = true;

    public SendButton(final GameScreen screen, final Skin skin, final RocketInOrbit rocket) {
        super();
        this.screen = screen;
        this.rocket = rocket;
        this.sendDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("send"));

        this.sendRocketButton = new ImageButton(this.sendDrawable);
        sendRocketButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Rocket.ROCKET_TIER_FUEL_PRICE[rocket.tier - 1].canAfford(screen.myPlayer) && !screen.noCostMode) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You cannot afford this action.");
                    return;
                }

                screen.notificationTable.setMessage("Select an available planet.");
                screen.planetsTable.rocketToSend = rocket;
            }
        });
        sendRocketButton.addListener(new ColorTextTooltip(Rocket.ROCKET_TIER_FUEL_PRICE[rocket.tier - 1].toString(), new InstantTooltipManager(), skin, Color.BLACK));

        this.rocketCooldownLabel = new ColorLabel("", skin, Color.BLACK);
        this.rocketCooldownLabel.setAlignment(Align.center);
        this.add(this.rocketCooldownLabel).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.rocket.cooldown > 0f) {
            this.rocketCooldownLabel.setText((int) this.rocket.cooldown);
        } else if (this.inCooldown) {
            this.inCooldown = false;
            this.clear();
            this.add(this.sendRocketButton).size(PlanetDashboardTable.ROCKET_LABEL_WIDTH / 6f);
        }
    }
}
