package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.github.interastra.models.Price;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

public class BuySellResourcesTable extends Dashboard {

    private final EnterResourcesTable enterResourcesTable;

    public BuySellResourcesTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        this.titleLabel.setText("Transfer Resources");

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.enterResourcesTable = new EnterResourcesTable(this.skin, false);

        this.contentTable.add(this.enterResourcesTable).colspan(2);
        this.contentTable.row();

        this.contentTable.add(new Label("Buy or Sell", headerLabelStyle)).colspan(2).pad(5f);
        this.contentTable.row();

        TextButton buyTextButton = new TextButton("Buy", this.skin);
        buyTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Price buyAmount = enterResourcesTable.getPrice();
                if (buyAmount == null) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Invalid resource value(s).");
                    return;
                }


            }
        });
        buyTextButton.addListener(new ColorTextTooltip("", new InstantTooltipManager(), this.skin, Color.BLACK));
        this.contentTable.add(buyTextButton).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).padTop(10f);

        TextButton sellTextButton = new TextButton("Sell", this.skin);
        sellTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Price sellAmount = enterResourcesTable.getPrice();
                if (sellAmount == null) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Invalid resource value(s).");
                    return;
                }
            }
        });
        sellTextButton.addListener(new ColorTextTooltip("", new InstantTooltipManager(), this.skin, Color.BLACK));
        this.contentTable.add(sellTextButton).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).padTop(10f);
    }
}
