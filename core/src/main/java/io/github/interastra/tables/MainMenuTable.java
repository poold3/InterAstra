package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.Main;
import io.github.interastra.message.MessageService;
import io.github.interastra.screens.MainMenuScreen;
import io.github.interastra.rest.RestService;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.services.ValidationService;

import java.util.logging.Level;

public class MainMenuTable extends Table {
    public static final float BUTTON_WIDTH = 150f;
    public static final float BUTTON_HEIGHT = 60f;
    public static final float TEXTFIELD_WIDTH = 180f;
    public static final float TEXTFIELD_HEIGHT = 40f;

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
        if (!Main.IP_ADDRESS.equals("localhost")) {
            this.ipAddressTextField.setText(Main.IP_ADDRESS);
        }
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
                try {
                    super.keyboardFocusChanged(event, actor, focused);
                    if (focused && menuTextField.getText().equals(text)) {
                        menuTextField.setText("");
                    } else if (!focused && menuTextField.getText().isBlank()) {
                        menuTextField.setText(text);
                    }
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
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
                try {
                    String name = usernameTextField.getText().trim();
                    if (!ValidationService.validateName(name)) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage(ValidationService.NAME_VALIDATION_MESSAGE);
                        return;
                    }

                    screen.notificationTable.startLoading("Connecting");
                    setIPAddress();
                    RestService.newGame(screen, name);
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        return newGameButton;
    }

    public TextButton getJoinGameButton() {
        TextButton joinGameButton = new TextButton("Join Game", this.skin);
        joinGameButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
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
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        return joinGameButton;
    }

    public TextButton getExitButton() {
        TextButton exitButton = new TextButton("Exit", this.skin);
        exitButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    screen.leaveSound.play();
                    screen.notificationTable.setMessage("Goodbye!");
                    screen.exitGame = true;
                    RestService.shutdownClient();
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        return exitButton;
    }

    public void setIPAddress() {
        try {
            String ipAddress = this.ipAddressTextField.getText();
            if (ipAddress.isBlank() || ipAddress.equals("IP Address")) {
                return;
            }

            Main.IP_ADDRESS = ipAddress;
            RestService.BASE_URL = String.format("http://%s:8090", ipAddress);
            MessageService.BASE_URL = String.format("ws://%s:8090", ipAddress);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
