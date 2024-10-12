package io.github.interastra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.interastra.Main;
import io.github.interastra.services.MessageService;
import io.github.interastra.tables.NotificationTable;

import java.util.ArrayList;

public class LobbyScreen implements Screen {
    public Main game;
    public ScreenViewport viewport;
    public Stage stage;
    public SpriteBatch spriteBatch;
    public Skin skin;
    public Texture background;
    public Sound buttonSound;
    public Sound badSound;
    public Sound leaveSound;
    public NotificationTable notificationTable;
    public MessageService messageService;

    public LobbyScreen(final Main game, final String gameCode, final ArrayList<String> names) {
        this.game = game;
        this.messageService = new MessageService(gameCode);
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
    }

    @Override
    public void show() {
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        Image backgroundImage = new Image(this.background);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        this.stage.addActor(this.notificationTable);
    }

    @Override
    public void render(float delta) {
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
        this.background.dispose();
        this.skin.dispose();
        this.spriteBatch.dispose();
        this.stage.dispose();
        this.badSound.dispose();
        this.buttonSound.dispose();
        this.leaveSound.dispose();
    }
}
