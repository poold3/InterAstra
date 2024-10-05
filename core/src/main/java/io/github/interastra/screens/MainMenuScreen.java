package io.github.interastra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.interastra.Main;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.NotificationService;
import io.github.interastra.services.ValidationService;

public class MainMenuScreen implements Screen {
    public static final float BUTTON_WIDTH = 100f;
    public static final float BUTTON_HEIGHT = 40f;
    public static final float TEXTFIELD_WIDTH = 150f;
    public static final float TEXTFIELD_HEIGHT = 30f;

    public Main game;
    public ScreenViewport viewport;
    public Stage stage;
    public SpriteBatch spriteBatch;
    public Skin skin;
    public Texture background;
    public Sound buttonSound;
    public Sound badSound;
    public Sound leaveSound;
    public boolean exitGame = false;
    public float exitTime = 0f;
    public TextField usernameTextField;
    public TextField gameCodeTextField;
    public NotificationService notificationService;

    public MainMenuScreen(final Main game) {
        this.game = game;

        this.viewport = new ScreenViewport();
        this.stage = new Stage(this.viewport);
        Gdx.input.setInputProcessor(this.stage);
        this.skin = this.game.assetManager.get("spaceskin/spaceskin.json", Skin.class);
        this.background = this.game.assetManager.get("background.png", Texture.class);
        this.spriteBatch = new SpriteBatch();
        this.buttonSound = this.game.assetManager.get("audio/button.mp3", Sound.class);
        this.badSound = this.game.assetManager.get("audio/bad.mp3", Sound.class);
        this.leaveSound = this.game.assetManager.get("audio/leave.mp3", Sound.class);
        this.notificationService = new NotificationService(this.skin.getFont("Teko-16"));
    }

    @Override
    public void show() {
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        Image backgroundImage = new Image(this.background);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        this.stage.addActor(table);

        Label gameTitleLabel = this.getTitleLabel();
        table.add(gameTitleLabel).expandX().center().padTop(20);
        table.row();

        this.usernameTextField = this.getMenuTextField("Enter Username");
        table.add(usernameTextField).center().padTop(30f).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        table.row();

        TextButton newGameButton = this.getNewGameButton();
        table.add(newGameButton).center().padTop(10f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        table.row();

        this.gameCodeTextField = this.getMenuTextField("Enter Game Code");
        table.add(gameCodeTextField).center().padTop(50f).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        table.row();

        TextButton joinGameButton = this.getJoinGameButton();
        table.add(joinGameButton).center().padTop(10f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        table.row();

        TextButton exitButton = this.getExitButton();
        table.add(exitButton).center().padTop(50f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        table.row();
    }

    @Override
    public void render(float delta) {
        if (this.exitGame) {
            this.exitTime += delta;
            if (this.exitTime >= 0.5f) {
                Gdx.app.exit();
            }
        }

        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();

        this.viewport.apply();
        this.spriteBatch.setProjectionMatrix(this.viewport.getCamera().combined);
        this.spriteBatch.begin();

        float worldHeight = this.viewport.getWorldHeight();

        this.notificationService.drawMessage(this.spriteBatch, delta, 5f, worldHeight - 5f);
        this.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.background.dispose();
        this.skin.dispose();
        this.spriteBatch.dispose();
        this.stage.dispose();
        this.badSound.dispose();
        this.buttonSound.dispose();
        this.leaveSound.dispose();
        this.notificationService.dispose();
    }

    public Label getTitleLabel() {
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = this.skin.getFont("Teko-48");

        return new Label("Inter Astra", titleLabelStyle);
    }

    public TextField getMenuTextField(String text) {
        TextField usernameTextField = new TextField(text, this.skin);
        usernameTextField.setAlignment(Align.center);
        usernameTextField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                if (focused && usernameTextField.getText().equals(text)) {
                    usernameTextField.setText("");
                } else if (!focused && usernameTextField.getText().isBlank()) {
                    usernameTextField.setText(text);
                }
            }
        });
        return usernameTextField;
    }

    public TextButton getNewGameButton() {
        TextButton newGameButton = new TextButton("New Game", this.skin);
        newGameButton.addListener(new ClickListenerService(this.buttonSound) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = usernameTextField.getText().trim();
                if (!ValidationService.validateName(name)) {
                    badSound.play();
                    notificationService.setMessage(ValidationService.NAME_VALIDATION_MESSAGE);
                    return;
                }
            }
        });
        return newGameButton;
    }

    public TextButton getJoinGameButton() {
        TextButton joinGameButton = new TextButton("Join Game", this.skin);
        joinGameButton.addListener(new ClickListenerService(this.buttonSound) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = usernameTextField.getText().trim();
                if (!ValidationService.validateName(name)) {
                    badSound.play();
                    notificationService.setMessage(ValidationService.NAME_VALIDATION_MESSAGE);
                    return;
                }

                String gameCode = gameCodeTextField.getText().trim();
                if (!ValidationService.validateGameCode(gameCode)) {
                    badSound.play();
                    notificationService.setMessage(ValidationService.GAME_CODE_VALIDATION_MESSAGE);
                    return;
                }
            }
        });
        return joinGameButton;
    }

    public TextButton getExitButton() {
        TextButton exitButton = new TextButton("Exit", this.skin);
        exitButton.addListener(new ClickListenerService(this.buttonSound) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaveSound.play();
                notificationService.setMessage("Goodbye!");
                exitGame = true;
            }
        });
        return exitButton;
    }

}
