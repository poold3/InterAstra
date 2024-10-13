package io.github.interastra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.interastra.Main;
import io.github.interastra.rest.responses.JoinGameResponse;
import io.github.interastra.tables.MainMenuTable;
import io.github.interastra.tables.NotificationTable;

public class MainMenuScreen implements Screen {
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
    public NotificationTable notificationTable;
    public MainMenuTable mainMenuTable;
    public JoinGameResponse joinGameResponse;

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
        this.notificationTable = new NotificationTable(this.skin);
        this.mainMenuTable = new MainMenuTable(this, this.skin);
        this.joinGameResponse = null;
    }

    @Override
    public void show() {
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        Image backgroundImage = new Image(this.background);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        this.stage.addActor(this.mainMenuTable);
        this.stage.addActor(this.notificationTable);
    }

    @Override
    public void render(float delta) {
        if (this.exitGame) {
            this.exitTime += delta;
            if (this.exitTime >= 0.75f) {
                Gdx.app.exit();
            }
        } else if (this.joinGameResponse != null) {
            this.game.setScreen(new LobbyScreen(this.game, this.joinGameResponse.gameCode, this.joinGameResponse.names));
            this.dispose();
        }

        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
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
        this.spriteBatch.dispose();
        this.stage.dispose();
    }

    public synchronized void joinGameSuccess(final JoinGameResponse response) {
        this.joinGameResponse = response;
    }
}
