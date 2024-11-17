package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.github.interastra.message.messages.TransferMessage;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.message.models.PriceMessageModel;
import io.github.interastra.models.Price;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;

public class TransferTable extends Dashboard {

    private final EnterResourcesTable enterResourcesTable;
    private final Label recipientLabel;

    public TransferTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        this.titleLabel.setText("Transfer Resources");

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.enterResourcesTable = new EnterResourcesTable(this.skin, true);

        this.contentTable.setDebug(true);
        this.contentTable.add(this.enterResourcesTable).colspan(2);
        this.contentTable.row();

        this.contentTable.add(new Label("Select a Recipient", headerLabelStyle)).colspan(2).pad(5f);
        this.contentTable.row();

        this.recipientLabel = new Label("", this.skin);
        this.contentTable.add(this.recipientLabel).colspan(2).pad(5f);
        this.contentTable.row();

        int newRowCounter = 0;
        for (int i = 0; i < this.screen.players.size(); ++i) {
            if (this.screen.players.get(i).name().equals(this.screen.myPlayer.name)) {
                continue;
            }
            this.contentTable.add(this.getPlayerButton(this.screen.players.get(i))).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).pad(5f);
            if (newRowCounter % 2 == 1) {
                this.contentTable.row();
            }
            newRowCounter += 1;
        }
        if (this.screen.players.size() % 2 == 0) {
            this.contentTable.add().size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).pad(5f);
        }

        this.contentTable.row();
        TextButton sendTextButton = new TextButton("Send", this.skin);
        sendTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Verify recipient
                if (recipientLabel.textEquals("")) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You must select a recipient.");
                    return;
                }

                Price transferAmount = enterResourcesTable.getPrice();
                if (transferAmount == null) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Invalid resource value(s).");
                    return;
                }

                // Verify player can afford
                if (!transferAmount.canAfford(screen.myPlayer) && !screen.noCostMode) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You cannot afford this action.");
                    return;
                }

                screen.sendTransfer(new TransferMessage(screen.myPlayer.name, recipientLabel.getText().toString(), new PriceMessageModel(transferAmount)));
                resetUI();
            }
        });

        this.contentTable.row();
        this.contentTable.add(sendTextButton).colspan(2).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).padTop(15f);
    }

    public void resetUI() {
        this.enterResourcesTable.resetUI();
        this.recipientLabel.setText("");
    }

    public TextButton getPlayerButton(final PlayerMessageModel player) {
        TextButton playerTextButton = new TextButton(player.name(), this.skin);
        playerTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                recipientLabel.setText(player.name());
            }
        });
        return playerTextButton;
    }
}
