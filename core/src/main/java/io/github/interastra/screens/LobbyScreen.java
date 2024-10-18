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
import io.github.interastra.message.MessageService;
import io.github.interastra.message.StompHandlers.LobbyUpdate;
import io.github.interastra.message.models.LobbyPlayerModel;
import io.github.interastra.models.Player;
import io.github.interastra.tables.LobbyTable;
import io.github.interastra.tables.NotificationTable;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class LobbyScreen implements Screen {
    public Main game;
    public String gameCode;
    public StompSession messageSession;
    public MessageService messageService;
    public ArrayList<StompSession.Subscription> lobbySubscriptions;
    public String myName;
    public ArrayList<LobbyPlayerModel> players;
    public final ReentrantLock playersLock;

    public ScreenViewport viewport;
    public Stage stage;
    public SpriteBatch spriteBatch;
    public Skin skin;
    public Texture background;
    public Sound buttonSound;
    public Sound badSound;
    public Sound leaveSound;
    public NotificationTable notificationTable;
    public LobbyTable lobbyTable;

    public LobbyScreen(final Main game, final String gameCode, final String myName) {
        this.game = game;
        this.gameCode = gameCode;
        this.myName = myName;
        this.messageService = new MessageService(this);
        this.lobbySubscriptions = new ArrayList<>();
        this.playersLock = new ReentrantLock(true);
        this.players = new ArrayList<>();

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
        this.lobbyTable = new LobbyTable(this, this.skin);
    }

    @Override
    public void show() {
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        Image backgroundImage = new Image(this.background);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        this.stage.addActor(this.notificationTable);
        this.stage.addActor(this.lobbyTable);
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
        this.spriteBatch.dispose();
        this.stage.dispose();
    }

    public void setMessageSession(final StompSession messageSession) {
        this.messageSession = messageSession;
    }

    public void unsubscribeToLobbyTopics() {
        for (StompSession.Subscription sub : this.lobbySubscriptions) {
            sub.unsubscribe();
        }
        this.lobbySubscriptions.clear();
    }

    public void subscribeToLobbyTopics() {
        this.unsubscribeToLobbyTopics();
        System.out.println(String.format("/topic/lobby-update/%s", this.gameCode));
        this.lobbySubscriptions.add(
            this.messageSession.subscribe(
                String.format("/topic/lobby-update/%s", this.gameCode),
                new LobbyUpdate(this)
            )
        );
    }
}
