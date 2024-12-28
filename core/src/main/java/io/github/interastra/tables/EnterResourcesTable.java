package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.models.Price;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.stages.GameStage;

import java.util.logging.Level;

public class EnterResourcesTable extends Table {
    public static final float TEXTFIELD_WIDTH = 150f;
    public static final float TEXTFIELD_HEIGHT = 50f;

    private final Skin skin;
    public final TextField balanceTextField;
    public final TextField ironTextField;
    public final TextField oilTextField;
    public final TextField aluminumTextField;
    public final TextField copperTextField;
    public final TextField stoneTextField;
    public final GameStage stage;

    public EnterResourcesTable(final GameStage stage, final Skin skin, boolean includeBalance) {
        super();
        this.stage = stage;
        this.skin = skin;

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.add(new Label("Enter Amounts", headerLabelStyle)).colspan(2).pad(5f);
        this.row();

        Label balanceLabel = this.getAmountLabel("Balance ($)");
        Label ironLabel = this.getAmountLabel("Iron");
        this.add(balanceLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.add(ironLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.row();
        this.balanceTextField = this.getAmountTextField();
        if (!includeBalance) {
            this.balanceTextField.setDisabled(true);
        }
        this.ironTextField = this.getAmountTextField();
        this.add(this.balanceTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.add(this.ironTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.row();

        Label oilLabel = this.getAmountLabel("Oil");
        Label aluminumLabel = this.getAmountLabel("Aluminum");
        this.add(oilLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.add(aluminumLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.row();
        this.oilTextField = this.getAmountTextField();
        this.aluminumTextField = this.getAmountTextField();
        this.add(this.oilTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.add(this.aluminumTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.row();

        Label copperLabel = this.getAmountLabel("Copper");
        Label stoneLabel = this.getAmountLabel("Stone");
        this.add(copperLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.add(stoneLabel).width(TEXTFIELD_WIDTH).padTop(10f);
        this.row();
        this.copperTextField = this.getAmountTextField();
        this.stoneTextField = this.getAmountTextField();
        this.add(this.copperTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.add(this.stoneTextField).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT).pad(5f);
        this.row();
    }

    public void resetUI() {
        try {
            this.balanceTextField.setText("0.0");
            this.stage.unfocus(this.balanceTextField);
            this.ironTextField.setText("0.0");
            this.stage.unfocus(this.ironTextField);
            this.oilTextField.setText("0.0");
            this.stage.unfocus(this.oilTextField);
            this.aluminumTextField.setText("0.0");
            this.stage.unfocus(this.aluminumTextField);
            this.copperTextField.setText("0.0");
            this.stage.unfocus(this.copperTextField);
            this.stoneTextField.setText("0.0");
            this.stage.unfocus(this.stoneTextField);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
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
                try {
                    super.keyboardFocusChanged(event, actor, focused);
                    if (focused && amountTextField.getText().equals("0.0")) {
                        amountTextField.setText("");
                    } else if (!focused && amountTextField.getText().isBlank()) {
                        amountTextField.setText("0.0");
                    }
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        amountTextField.addListener(new ClickListenerService(null, Cursor.SystemCursor.Ibeam));
        return amountTextField;
    }

    /**
     * Gets the Price object from the TextFields. Returns null if there are invalid entries.
     * @return The Price object
     */
    public Price getPrice() {
        // Verify amounts
        float balance;
        float ironBalance;
        float oilBalance;
        float aluminumBalance;
        float copperBalance;
        float stoneBalance;
        try {
            balance = Float.parseFloat(balanceTextField.getText().isEmpty() ? "0" : balanceTextField.getText());
            ironBalance = Float.parseFloat(ironTextField.getText().isEmpty() ? "0" : ironTextField.getText());
            oilBalance = Float.parseFloat(oilTextField.getText().isEmpty() ? "0" : oilTextField.getText());
            aluminumBalance = Float.parseFloat(aluminumTextField.getText().isEmpty() ? "0" : aluminumTextField.getText());
            copperBalance = Float.parseFloat(copperTextField.getText().isEmpty() ? "0" : copperTextField.getText());
            stoneBalance = Float.parseFloat(stoneTextField.getText().isEmpty() ? "0" : stoneTextField.getText());
        } catch (NumberFormatException e) {
            return null;
        }

        // Check for negative amounts
        if (
            balance < 0f
                || ironBalance < 0f
                || oilBalance < 0f
                || aluminumBalance < 0f
                || copperBalance < 0f
                || stoneBalance < 0f
        ) {
            return null;
        }

        return new Price(
            balance,
            ironBalance,
            oilBalance,
            aluminumBalance,
            copperBalance,
            stoneBalance
        );
    }
}
