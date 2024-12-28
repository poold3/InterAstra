package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

import java.util.logging.Level;

public class PlanetDashboardButtonTable extends Table {
    public final float DASHBOARD_BUTTON_SIZE = 60f;

    private final GameScreen screen;
    public boolean isVisible = false;

    public PlanetDashboardButtonTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;

        this.setFillParent(true);
        this.top();

        Drawable dashboardDrawable = new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("dashboard"));

        ImageButton.ImageButtonStyle dashboardButtonStyle = new ImageButton.ImageButtonStyle();
        dashboardButtonStyle.imageUp = dashboardDrawable;
        dashboardButtonStyle.imageDown = dashboardDrawable;
        ImageButton dashboardImageButton = new ImageButton(dashboardButtonStyle);

        dashboardImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.togglePlanetDashboard();
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });



        Container<ImageButton> buttonContainer = new Container<>(dashboardImageButton);
        buttonContainer.width(DASHBOARD_BUTTON_SIZE);
        buttonContainer.height(DASHBOARD_BUTTON_SIZE);
        buttonContainer.setBackground(skin.getDrawable("panel_square"));

        Tooltip<Container<Label>> dashboardTooltip = new ColorTextTooltip("Planet Dashboard (d)", new InstantTooltipManager(), skin, Color.BLACK);
        buttonContainer.addListener(dashboardTooltip);

        this.add(buttonContainer).maxWidth(DASHBOARD_BUTTON_SIZE).maxHeight(DASHBOARD_BUTTON_SIZE).expandX().right().top().pad(5f);
    }
}
