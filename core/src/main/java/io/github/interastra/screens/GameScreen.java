package io.github.interastra.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.interastra.Main;
import io.github.interastra.message.messages.GameStartMessage;
import io.github.interastra.message.models.PlanetMessageModel;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.models.*;
import io.github.interastra.services.CameraOperatorService;
import io.github.interastra.stages.GameStage;
import io.github.interastra.tables.NotificationTable;
import io.github.interastra.tables.OptionsTable;
import io.github.interastra.tables.PlanetsTable;

import java.util.ArrayList;
import java.util.Collections;

public class GameScreen implements Screen {
    public static final float MIN_WORLD_SIZE = 1000f;
    public static final float ARROW_KEY_MOVE_SPEED = 25f;

    public Main game;
    public LobbyScreen lobbyScreen;
    public CameraOperatorService camera;
    public ExtendViewport gameViewport;
    public CameraEnabledEntity entityBeingFollowed = null;

    public ScreenViewport stageViewport;
    public GameStage stage;
    public TextureAtlas iconsTextureAtlas;
    public Skin skin;
    public Sound buttonSound;
    public Sound leaveSound;
    public Sound goodSound;
    public Sound badSound;
    public NotificationTable notificationTable;
    public PlanetsTable planetsTable;
    public OptionsTable optionsTable;

    public TextureAtlas planetsTextureAtlas;

    public Star sol;
    public ArrayList<Planet> planets;
    public ArrayList<Player> players;
    public ArrayList<Rocket> rockets;
    public float speedMultiplier;
    public boolean leaveGame = false;
    public boolean optionsMenuOpen = false;

    public GameScreen(final Main game, final LobbyScreen lobbyScreen, final GameStartMessage gameData) {
        this.game = game;
        this.lobbyScreen = lobbyScreen;

        this.planetsTextureAtlas = this.game.assetManager.get("planets/planets.atlas", TextureAtlas.class);

        this.loadGameData(gameData);

        this.speedMultiplier = 1f;

        this.camera = new CameraOperatorService();
        this.gameViewport = new ExtendViewport(MIN_WORLD_SIZE, MIN_WORLD_SIZE, camera);
        this.camera.setViewport(this.gameViewport);

        this.stageViewport = new ScreenViewport();
        this.stage = new GameStage(this.stageViewport, this);
        Gdx.input.setInputProcessor(this.stage);
        this.iconsTextureAtlas = this.game.assetManager.get("icons/icons.atlas", TextureAtlas.class);
        this.skin = this.game.assetManager.get("spaceskin/spaceskin.json", Skin.class);
        this.buttonSound = this.game.assetManager.get("audio/button.mp3", Sound.class);
        this.leaveSound = this.game.assetManager.get("audio/leave.mp3", Sound.class);
        this.goodSound = this.game.assetManager.get("audio/good.mp3", Sound.class);
        this.badSound = this.game.assetManager.get("audio/bad.mp3", Sound.class);
        this.notificationTable = new NotificationTable(this.skin);
        this.planetsTable = new PlanetsTable(this, this.skin);
        this.optionsTable = new OptionsTable(this, this.skin);
    }

    @Override
    public void show() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        this.notificationTable.setMessage("Welcome to our Solar System!");
        this.stage.addActor(this.notificationTable);
        this.stage.addActor(this.planetsTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.004f, 0f, 0.03f, 1f));

        this.input();
        this.logic();
        this.draw();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stageViewport.update(width, height, true);
        this.gameViewport.update(width, height, true);
        this.camera.center();
        this.sol.reposition(this.gameViewport.getWorldWidth() / 2f, this.gameViewport.getWorldHeight() / 2f);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }

    public void input() {
        // Escape key to exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.toggleOptionsMenu();
        }

    }

    public void logic() {
        if (this.leaveGame) {
            this.game.setScreen(new MainMenuScreen(this.game));
            this.lobbyScreen.unsubscribeToLobbyTopics();
            this.unsubscribeToGameTopics();
            this.lobbyScreen.messageSession.disconnect();
            this.lobbyScreen.dispose();
            this.dispose();
        }

        // Move planets
        for (Planet planet : this.planets) {
            planet.move(this.gameViewport.getWorldWidth(), this.gameViewport.getWorldHeight(), Gdx.graphics.getDeltaTime(), speedMultiplier);
        }

        // If following an entity, tell the camera operator to do so.
        if (this.entityBeingFollowed != null) {
            this.camera.followCameraEnabledEntity(this.entityBeingFollowed);
        }

        // Tell the camera operator to move if needed
        this.camera.move();
    }

    public void draw() {
        this.gameViewport.apply();
        this.game.spriteBatch.setProjectionMatrix(this.camera.combined);

        // Draw the sprites
        this.game.spriteBatch.begin();

        this.sol.starSprite.draw(this.game.spriteBatch);

        for (Planet planet : this.planets) {
            planet.planetSprite.draw(this.game.spriteBatch);
            if (planet.moon != null) {
                planet.moon.moonSprite.draw(this.game.spriteBatch);
            }
        }

        this.game.spriteBatch.end();
    }

    public void loadGameData(final GameStartMessage gameData) {
        // Add Sol
        this.sol = new Star(this.planetsTextureAtlas, gameData.sol());

        // Add Planets
        this.planets = new ArrayList<>();
        for (PlanetMessageModel planet : gameData.planets()) {
            this.planets.add(new Planet(this.planetsTextureAtlas, planet));
        }
        Collections.sort(this.planets);

        // Add Players
        this.players = new ArrayList<>();
        for (PlayerMessageModel player : gameData.players()) {
            this.players.add(new Player(player));
        }

        // Add Rockets
        this.rockets = new ArrayList<>();
    }

    public void unsubscribeToGameTopics() {

    }

    public void toggleOptionsMenu() {
        optionsMenuOpen = !optionsMenuOpen;
        if (optionsMenuOpen) {
            this.goodSound.play(0.5f);
            this.stage.addActor(this.optionsTable);
        } else {
            this.optionsTable.remove();
        }
    }
}
