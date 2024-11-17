package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.message.messages.TransferMessage;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.message.models.PriceMessageModel;
import io.github.interastra.models.Price;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;

public class TransferTable extends Dashboard {
    public static final float TEXTFIELD_WIDTH = 150f;
    public static final float TEXTFIELD_HEIGHT = 30f;

    private final TextField balanceTextField;
    private final TextField ironTextField;
    private final TextField oilTextField;
    private final TextField siliconTextField;
    private final TextField lithiumTextField;
    private final TextField helium3TextField;
    private final Label recipientLabel;

    public TransferTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        this.titleLabel.setText("Transfer Resources");

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.contentTable.add(new Label("Enter Amounts", headerLabelStyle)).colspan(2).pad(5f);
        this.contentTable.row();

        Label balanceLabel = this.getAmountLabel("Balance ($)");
        Label ironLabel = this.getAmountLabel("Iron");
        this.contentTable.add(balanceLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.contentTable.add(ironLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.contentTable.row();
        this.balanceTextField = this.getAmountTextField();
        this.ironTextField = this.getAmountTextField();
        this.contentTable.add(this.balanceTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.contentTable.add(this.ironTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.contentTable.row();

        Label oilLabel = this.getAmountLabel("Oil");
        Label siliconLabel = this.getAmountLabel("Silicon");
        this.contentTable.add(oilLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.contentTable.add(siliconLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.contentTable.row();
        this.oilTextField = this.getAmountTextField();
        this.siliconTextField = this.getAmountTextField();
        this.contentTable.add(this.oilTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.contentTable.add(this.siliconTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.contentTable.row();

        Label lithiumLabel = this.getAmountLabel("Lithium");
        Label helium3Label = this.getAmountLabel("Helium3");
        this.contentTable.add(lithiumLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.contentTable.add(helium3Label).width(TEXTFIELD_WIDTH).padTop(10f);
        this.contentTable.row();
        this.lithiumTextField = this.getAmountTextField();
        this.helium3TextField = this.getAmountTextField();
        this.contentTable.add(this.lithiumTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.contentTable.add(this.helium3TextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
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
            this.contentTable.add(this.getPlayerButton(this.screen.players.get(i))).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).pad(5f).center();
            if (newRowCounter % 2 == 1) {
                this.contentTable.row();
            }
            newRowCounter += 1;
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

                // Verify amounts
                float balance;
                float ironBalance;
                float oilBalance;
                float siliconBalance;
                float lithiumBalance;
                float helium3Balance;
                try {
                    balance = Float.parseFloat(balanceTextField.getText().isEmpty() ? "0" : balanceTextField.getText());
                    ironBalance = Float.parseFloat(ironTextField.getText().isEmpty() ? "0" : ironTextField.getText());
                    oilBalance = Float.parseFloat(oilTextField.getText().isEmpty() ? "0" : oilTextField.getText());
                    siliconBalance = Float.parseFloat(siliconTextField.getText().isEmpty() ? "0" : siliconTextField.getText());
                    lithiumBalance = Float.parseFloat(lithiumTextField.getText().isEmpty() ? "0" : lithiumTextField.getText());
                    helium3Balance = Float.parseFloat(helium3TextField.getText().isEmpty() ? "0" : helium3TextField.getText());
                } catch (NumberFormatException e) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Invalid amount value(s).");
                    return;
                }

                // Check for negative amounts
                if (
                    balance < 0f
                    || ironBalance < 0f
                    || oilBalance < 0f
                    || siliconBalance < 0f
                    || lithiumBalance < 0f
                    || helium3Balance < 0f
                ) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("No negative value(s).");
                    return;
                }

                Price transferAmount = new Price(
                    balance,
                    ironBalance,
                    oilBalance,
                    siliconBalance,
                    lithiumBalance,
                    helium3Balance
                );

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
        this.balanceTextField.setText("0.0");
        this.ironTextField.setText("0.0");
        this.oilTextField.setText("0.0");
        this.siliconTextField.setText("0.0");
        this.lithiumTextField.setText("0.0");
        this.helium3TextField.setText("0.0");
        this.recipientLabel.setText("");
    }

    public Label getAmountLabel(final String text) {
        Label textFieldLabel = new Label(text, this.skin);
        textFieldLabel.setAlignment(Align.center);
        return textFieldLabel;
    }

    public TextField getAmountTextField() {
        TextField.TextFieldStyle amountTextFieldStyle = new TextField.TextFieldStyle(this.skin.get(TextField.TextFieldStyle.class));
        amountTextFieldStyle.fontColor = Color.BLACK;
        amountTextFieldStyle.background = this.skin.getDrawable("panel_glass");
        amountTextFieldStyle.focusedBackground = this.skin.getDrawable("panel_glass");
        amountTextFieldStyle.cursor = this.skin.getDrawable("cursor-16-black");

        TextField amountTextField = new TextField("0.0", amountTextFieldStyle);
        amountTextField.setAlignment(Align.center);
        amountTextField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                if (focused && amountTextField.getText().equals("0.0")) {
                    amountTextField.setText("");
                } else if (!focused && amountTextField.getText().isBlank()) {
                    amountTextField.setText("0.0");
                }
            }
        });
        amountTextField.addListener(new ClickListenerService(null, Cursor.SystemCursor.Ibeam));
        return amountTextField;
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
