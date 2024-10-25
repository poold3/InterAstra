package io.github.interastra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.interastra.Main;
import io.github.interastra.message.MessageService;
import io.github.interastra.message.StompHandlers.GameStart;
import io.github.interastra.message.StompHandlers.LobbyUpdate;
import io.github.interastra.message.messages.GameStartMessage;
import io.github.interastra.message.models.LobbyPlayerMessageModel;
import io.github.interastra.services.LockService;
import io.github.interastra.tables.LobbyTable;
import io.github.interastra.tables.NotificationTable;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;

public class LobbyScreen implements Screen {
    public Main game;
    public String gameCode;
    public StompSession messageSession;
    public MessageService messageService;
    public ArrayList<StompSession.Subscription> lobbySubscriptions;
    public String myName;
    public LockService<ArrayList<LobbyPlayerMessageModel>> players;

    public ScreenViewport viewport;
    public Stage stage;
    public TextureAtlas iconsTextureAtlas;
    public SpriteBatch spriteBatch;
    public Skin skin;
    public Texture background;
    public Sound buttonSound;
    public Sound leaveSound;
    public NotificationTable notificationTable;
    public LobbyTable lobbyTable;

    public boolean leaveLobby = false;
    public float leaveTime = 0f;
    public GameStartMessage gameData;

    public LobbyScreen(final Main game, final String gameCode, final String myName) {
        this.game = game;
        this.gameCode = gameCode;
        this.myName = myName;
        this.messageService = new MessageService(this);
        this.lobbySubscriptions = new ArrayList<>();
        this.players = new LockService<>(new ArrayList<>(), true);

        this.viewport = new ScreenViewport();
        this.stage = new Stage(this.viewport);
        Gdx.input.setInputProcessor(this.stage);
        this.iconsTextureAtlas = this.game.assetManager.get("icons/icons.atlas", TextureAtlas.class);
        this.skin = this.game.assetManager.get("spaceskin/spaceskin.json", Skin.class);
        this.background = this.game.assetManager.get("background.png", Texture.class);
        this.spriteBatch = new SpriteBatch();
        this.buttonSound = this.game.assetManager.get("audio/button.mp3", Sound.class);
        this.leaveSound = this.game.assetManager.get("audio/leave.mp3", Sound.class);
        this.notificationTable = new NotificationTable(this.skin);
        this.lobbyTable = new LobbyTable(this, this.skin);
    }

    @Override
    public void show() {
        this.stage.clear();
        Image backgroundImage = new Image(this.background);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        this.stage.addActor(this.notificationTable);
        this.stage.addActor(this.lobbyTable);
    }

    @Override
    public void render(float delta) {
        if (this.leaveLobby) {
            this.leaveTime += delta;
            if (this.leaveTime >= 0.75f) {
                this.unsubscribeToLobbyTopics();
                this.messageSession.disconnect();
                this.game.setScreen(new MainMenuScreen(this.game));
                this.dispose();
            }
        } else if (this.gameData != null) {
            this.game.setScreen(new GameScreen(this.game, this, this.gameData));
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

        // Add lobby update subscription
        this.lobbySubscriptions.add(
            this.messageSession.subscribe(
                String.format("/topic/lobby-update/%s", this.gameCode),
                new LobbyUpdate(this)
            )
        );

        // Add game start subscription
        this.lobbySubscriptions.add(
            this.messageSession.subscribe(
                String.format("/topic/game-start/%s", this.gameCode),
                new GameStart(this)
            )
        );
    }

    public boolean getReadyStatus(final String name) {
        boolean ready = false;
        this.players.lock();
        for (LobbyPlayerMessageModel player : this.players.getData()) {
            if (player.name().equals(name)) {
                ready = player.ready();
                break;
            }
        }
        this.players.unlock();
        return ready;
    }
}
