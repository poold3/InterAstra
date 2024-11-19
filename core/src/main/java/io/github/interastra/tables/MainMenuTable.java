package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.message.MessageService;
import io.github.interastra.screens.MainMenuScreen;
import io.github.interastra.rest.RestService;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.ValidationService;

public class MainMenuTable extends Table {
    public static final float BUTTON_WIDTH = 100f;
    public static final float BUTTON_HEIGHT = 40f;
    public static final float TEXTFIELD_WIDTH = 150f;
    public static final float TEXTFIELD_HEIGHT = 30f;

    private final MainMenuScreen screen;
    private final Skin skin;
    private final TextField ipAddressTextField;
    private final TextField usernameTextField;
    private final TextField gameCodeTextField;

    public MainMenuTable(final MainMenuScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.center();

        Label gameTitleLabel = this.getTitleLabel();
        this.add(gameTitleLabel).expandX().center().padTop(20);
        this.row();

        this.ipAddressTextField = this.getMenuTextField("IP Address");
        this.add(this.ipAddressTextField).center().padTop(30f).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        this.row();

        this.usernameTextField = this.getMenuTextField("Enter Name");
        this.add(usernameTextField).center().padTop(10f).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        this.row();

        TextButton newGameButton = this.getNewGameButton();
        this.add(newGameButton).center().padTop(10f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.row();

        this.gameCodeTextField = this.getMenuTextField("Enter Game Code");
        this.add(gameCodeTextField).center().padTop(50f).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        this.row();

        TextButton joinGameButton = this.getJoinGameButton();
        this.add(joinGameButton).center().padTop(10f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.row();

        TextButton exitButton = this.getExitButton();
        this.add(exitButton).center().padTop(50f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        this.row();
    }

    public Label getTitleLabel() {
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = this.skin.getFont("Teko-48");

        return new Label("Inter Astra", titleLabelStyle);
    }

    public String getUsernameText() {
        return usernameTextField.getText().trim();
    }

    public TextField getMenuTextField(String text) {
        TextField menuTextField = new TextField(text, this.skin);
        menuTextField.setAlignment(Align.center);
        menuTextField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                if (focused && menuTextField.getText().equals(text)) {
                    menuTextField.setText("");
                } else if (!focused && menuTextField.getText().isBlank()) {
                    menuTextField.setText(text);
                }
            }
        });
        menuTextField.addListener(new ClickListenerService(null, Cursor.SystemCursor.Ibeam));
        return menuTextField;
    }

    public TextButton getNewGameButton() {
        TextButton newGameButton = new TextButton("New Game", this.skin);
        newGameButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = usernameTextField.getText().trim();
                if (!ValidationService.validateName(name)) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage(ValidationService.NAME_VALIDATION_MESSAGE);
                    return;
                }

                screen.notificationTable.startLoading("Connecting");
                setIPAddress();
                RestService.newGame(screen, name);
            }
        });
        return newGameButton;
    }

    public TextButton getJoinGameButton() {
        TextButton joinGameButton = new TextButton("Join Game", this.skin);
        joinGameButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = usernameTextField.getText().trim();
                if (!ValidationService.validateName(name)) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage(ValidationService.NAME_VALIDATION_MESSAGE);
                    return;
                }

                String gameCode = gameCodeTextField.getText().trim();
                if (!ValidationService.validateGameCode(gameCode)) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage(ValidationService.GAME_CODE_VALIDATION_MESSAGE);
                    return;
                }

                screen.notificationTable.startLoading("Connecting");
                setIPAddress();
                RestService.joinGame(screen, name, gameCode);
            }
        });
        return joinGameButton;
    }

    public TextButton getExitButton() {
        TextButton exitButton = new TextButton("Exit", this.skin);
        exitButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.leaveSound.play();
                screen.notificationTable.setMessage("Goodbye!");
                screen.exitGame = true;
                RestService.shutdownClient();
            }
        });
        return exitButton;
    }

    public void setIPAddress() {
        String ipAddress = this.ipAddressTextField.getText();
        if (ipAddress.isBlank()) {
            return;
        }

        RestService.BASE_URL = String.format("http://%s:8080", ipAddress);
        MessageService.BASE_URL = String.format("ws://%s:8080", ipAddress);
    }
}
