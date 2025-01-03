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
import io.github.interastra.message.StompHandlers.GameEnd;
import io.github.interastra.message.StompHandlers.GameUpdate;
import io.github.interastra.message.StompHandlers.Transfer;
import io.github.interastra.message.messages.*;
import io.github.interastra.message.models.PlanetMessageModel;
import io.github.interastra.message.models.PlayerMessageModel;
import io.github.interastra.models.*;
import io.github.interastra.services.CameraOperatorService;
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.stages.GameStage;
import io.github.interastra.tables.*;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.logging.Level;

public class GameScreen implements Screen {
    public static final float MIN_WORLD_SIZE = 1000f;

    public Main game;
    public LobbyScreen lobbyScreen;
    public CameraOperatorService camera;
    public ExtendViewport gameViewport;
    public CameraEnabledEntity entityBeingFollowed = null;

    public ScreenViewport stageViewport;
    public GameStage stage;
    public TextureAtlas iconsTextureAtlas;
    public TextureAtlas spaceCraftTextureAtlas;
    public Skin skin;
    public Sound buttonSound;
    public Sound leaveSound;
    public Sound cooldownSound;
    public Sound badSound;
    public Sound moneySound;
    public Sound buildSound;
    public Sound valueSound;
    public NotificationTable notificationTable;
    public CoordinatesTable coordinatesTable;
    public PlanetsTable planetsTable;
    public OptionsTable optionsTable;
    public ResourcesTable resourcesTable;
    public PlanetDashboardButtonTable planetDashboardButtonTable;
    public PlanetDashboardTable planetDashboardTable;
    public TransferTable transferTable;
    public BuySellResourcesTable buySellTable;
    public InfoTable infoTable;
    public TextureAtlas planetsTextureAtlas;

    public int basesToWin;
    public Star sol;
    public ArrayList<Planet> planets;
    public ArrayList<PlayerMessageModel> players;
    public Player myPlayer;
    public boolean leaveGame = false;
    public ArrayList<StompSession.Subscription> gameSubscriptions;
    public boolean endGame = false;
    public float resourceUpdateTimer = 0f;
    public boolean noCostMode = false;

    public GameScreen(final Main game, final LobbyScreen lobbyScreen, final GameStartMessage gameData) {
        this.game = game;
        this.lobbyScreen = lobbyScreen;

        this.planetsTextureAtlas = this.game.assetManager.get("planets/planets.atlas", TextureAtlas.class);
        this.spaceCraftTextureAtlas = this.game.assetManager.get("spacecraft/spacecraft.atlas", TextureAtlas.class);

        this.buttonSound = this.game.assetManager.get("audio/button.mp3", Sound.class);
        this.leaveSound = this.game.assetManager.get("audio/leave.mp3", Sound.class);
        this.cooldownSound = this.game.assetManager.get("audio/good.mp3", Sound.class);
        this.badSound = this.game.assetManager.get("audio/bad.mp3", Sound.class);
        this.moneySound = this.game.assetManager.get("audio/money.mp3", Sound.class);
        this.buildSound = this.game.assetManager.get("audio/build.mp3", Sound.class);
        this.valueSound = this.game.assetManager.get("audio/devalue.mp3", Sound.class);

        this.loadGameData(gameData);

        this.gameSubscriptions = new ArrayList<>();
        this.subscribeToGameTopics();

        this.camera = new CameraOperatorService();
        this.gameViewport = new ExtendViewport(MIN_WORLD_SIZE, MIN_WORLD_SIZE, camera);
        this.camera.setViewport(this.gameViewport);

        this.stageViewport = new ScreenViewport();
        this.stage = new GameStage(this.stageViewport, this);
        this.iconsTextureAtlas = this.game.assetManager.get("icons/icons.atlas", TextureAtlas.class);
        this.skin = this.game.assetManager.get("spaceskin/spaceskin.json", Skin.class);
        this.notificationTable = new NotificationTable(this.skin, this.stageViewport);
        this.coordinatesTable = new CoordinatesTable(this.camera, this.skin);
        this.planetsTable = new PlanetsTable(this, this.skin);
        this.optionsTable = new OptionsTable(this, this.skin);
        this.resourcesTable = new ResourcesTable(this, this.skin);
        this.planetDashboardButtonTable = new PlanetDashboardButtonTable(this, this.skin);
        this.planetDashboardTable = new PlanetDashboardTable(this, this.skin);
        this.transferTable = new TransferTable(this, this.skin);
        this.buySellTable = new BuySellResourcesTable(this, this.skin);
        this.infoTable = new InfoTable(this, this.skin);

        this.stage.addActor(this.notificationTable);
        this.stage.addActor(this.coordinatesTable);
        this.stage.addActor(this.planetsTable);
        this.stage.addActor(this.resourcesTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        this.notificationTable.setMessage("Welcome to our Solar System!");
    }

    @Override
    public void render(float delta) {
        try {
            ScreenUtils.clear(new Color(0.004f, 0f, 0.03f, 1f));

            this.input();
            this.logic(delta);
            this.draw();

            stage.act(delta);
            stage.draw();
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void resize(int width, int height) {
        try {
            this.stageViewport.update(width, height, true);
            this.gameViewport.update(width, height, true);
            this.camera.center();
            this.sol.reposition(this.gameViewport.getWorldWidth() / 2f, this.gameViewport.getWorldHeight() / 2f);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
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
        try {
            this.stage.dispose();
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void input() {
        // Escape key to exit
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (this.planetsTable.rocketToSend != null) {
                this.planetsTable.rocketToSend = null;
                this.planetsTable.resetRangeFinder();
            } else if (this.planetDashboardTable.isVisible) {
                this.togglePlanetDashboard();
            } else if (this.transferTable.isVisible) {
                this.toggleTransferTable();
            } else if (this.buySellTable.isVisible) {
                this.toggleBuySellTable();
            } else if (this.infoTable.isVisible) {
                this.toggleInfoTable();
            } else {
                this.toggleOptionsMenu();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            this.toggleBuySellTable();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            this.toggleTransferTable();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            this.toggleInfoTable();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            this.togglePlanetDashboard();
        }
    }

    public void logic(float delta) {
        if (this.leaveGame) {
            this.game.setScreen(new MainMenuScreen(this.game));
            this.lobbyScreen.unsubscribeToLobbyTopics();
            this.unsubscribeToGameTopics();
            this.lobbyScreen.messageSession.disconnect();
            this.lobbyScreen.dispose();
            this.dispose();
        } else if (this.endGame) {
            this.lobbyScreen.gameData = null;
            this.game.setScreen(this.lobbyScreen);
            this.unsubscribeToGameTopics();
            this.dispose();
        }

        // Update resource buy/sell values
        this.buySellTable.update(delta);

        // Update resource balances
        this.resourceUpdateTimer += delta;
        while (this.resourceUpdateTimer >= 1f) {
            this.resourceUpdateTimer -= 1f;
            for (Planet planet : this.planets) {
                if (planet.isVisible) {
                    final float resourceMultiplier = (planet.hasMyBase ? Planet.BASE_RESOURCE_MULTIPLIER : 0f) + planet.myResourceMultiplier;
                    for (PlanetResource planetResource : planet.resources) {
                        myPlayer.resourceBalances.computeIfPresent(planetResource.resource, (k, currentBalance) -> currentBalance + (planetResource.rate * resourceMultiplier));
                    }
                }
            }
        }

        // Move planets
        for (Planet planet : this.planets) {
            planet.move(this.gameViewport.getWorldWidth(), this.gameViewport.getWorldHeight(), delta);
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
            if (planet.isVisible) {
                for (RocketInOrbit rocket : planet.rocketsInOrbit) {
                    rocket.rocketSprite.draw(this.game.spriteBatch);
                }
            }
            for (RocketInFlight rocket : planet.rocketsInFlight) {
                if (rocket.arrived) {
                    continue;
                }
                rocket.propulsionSprite.draw(this.game.spriteBatch);
                rocket.rocketSprite.draw(this.game.spriteBatch);
            }
        }
        this.game.spriteBatch.end();
    }

    public void loadGameData(final GameStartMessage gameData) {
        try {
            this.basesToWin = gameData.basesToWin();
            // Add Sol
            this.sol = new Star(this.planetsTextureAtlas, gameData.sol());

            // Add Planets
            this.planets = new ArrayList<>();
            for (PlanetMessageModel planet : gameData.planets()) {
                this.planets.add(new Planet(this.planetsTextureAtlas, planet, this.spaceCraftTextureAtlas, this.lobbyScreen.myName, cooldownSound));
            }

            // Add Players
            this.players = new ArrayList<>(gameData.players());
            for (PlayerMessageModel player : this.players) {
                if (player.name().equals(this.lobbyScreen.myName)) {
                    this.myPlayer = new Player(player);
                }
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void unsubscribeToGameTopics() {
        try {
            for (StompSession.Subscription sub : this.gameSubscriptions) {
                sub.unsubscribe();
            }
            this.gameSubscriptions.clear();
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void subscribeToGameTopics() {
        try {
            this.unsubscribeToGameTopics();

            // Add lobby update subscription
            this.gameSubscriptions.add(
                this.lobbyScreen.messageSession.subscribe(
                    String.format("/topic/game-update/%s", this.lobbyScreen.gameCode),
                    new GameUpdate(this)
                )
            );

            // Add game start subscription
            this.gameSubscriptions.add(
                this.lobbyScreen.messageSession.subscribe(
                    String.format("/topic/game-end/%s", this.lobbyScreen.gameCode),
                    new GameEnd(this)
                )
            );

            // Add transfer subscriptions
            this.gameSubscriptions.add(
                this.lobbyScreen.messageSession.subscribe(
                    String.format("/topic/transfer/%s", this.lobbyScreen.gameCode),
                    new Transfer(this)
                )
            );
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void toggleOptionsMenu() {
        try {
            this.optionsTable.isVisible = !this.optionsTable.isVisible;
            if (this.optionsTable.isVisible) {
                this.stage.addActor(this.optionsTable);
            } else {
                this.optionsTable.remove();
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void addPlanetDashboardButton() {
        this.planetDashboardButtonTable.isVisible = true;
        this.stage.addActor(this.planetDashboardButtonTable);
    }

    public void removePlanetDashboardButton() {
        this.planetDashboardButtonTable.isVisible = false;
        this.planetDashboardButtonTable.remove();
    }

    public void togglePlanetDashboard() {
        try {
            if (this.transferTable.isVisible) {
                this.toggleTransferTable();
            } else if (this.buySellTable.isVisible) {
                this.toggleBuySellTable();
            } else if (this.infoTable.isVisible) {
                this.toggleInfoTable();
            }

            if (!this.planetDashboardTable.isVisible && (this.entityBeingFollowed == null || this.entityBeingFollowed.getClass() != Planet.class)) {
                return;
            }

            this.planetDashboardTable.isVisible = !this.planetDashboardTable.isVisible;
            if (this.planetDashboardTable.isVisible) {
                this.planetDashboardTable.setPlanet((Planet) this.entityBeingFollowed);
                this.stage.addActor(this.planetDashboardTable);
            } else {
                this.planetDashboardTable.remove();
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void toggleTransferTable() {
        try {
            if (this.planetDashboardTable.isVisible) {
                this.togglePlanetDashboard();
            } else if (this.buySellTable.isVisible) {
                this.toggleBuySellTable();
            } else if (this.infoTable.isVisible) {
                this.toggleInfoTable();
            }
            this.transferTable.isVisible = !this.transferTable.isVisible;
            if (this.transferTable.isVisible) {
                this.stage.addActor(this.transferTable);
            } else {
                this.transferTable.remove();
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void toggleBuySellTable() {
        try {
            if (this.planetDashboardTable.isVisible) {
                this.togglePlanetDashboard();
            } else if (this.transferTable.isVisible) {
                this.toggleTransferTable();
            } else if (this.infoTable.isVisible) {
                this.toggleInfoTable();
            }
            this.buySellTable.isVisible = !this.buySellTable.isVisible;
            if (this.buySellTable.isVisible) {
                this.buySellTable.revaluationLabelUpdateTimer = 1f;
                this.stage.addActor(this.buySellTable);
            } else {
                this.buySellTable.remove();
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void toggleInfoTable() {
        try {
            if (this.planetDashboardTable.isVisible) {
                this.togglePlanetDashboard();
            } else if (this.transferTable.isVisible) {
                this.toggleTransferTable();
            } else if (this.buySellTable.isVisible) {
                this.toggleBuySellTable();
            }
            this.infoTable.isVisible = !this.infoTable.isVisible;
            if (this.infoTable.isVisible) {
                this.stage.addActor(this.infoTable);
            } else {
                this.infoTable.remove();
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void addRocket(final AddRocketMessage message) {
        try {
            if (!this.lobbyScreen.messageSession.isConnected()) {
                return;
            }
            String url = String.format("/ia-ws/add-rocket/%s", this.lobbyScreen.gameCode);
            this.lobbyScreen.messageSession.send(url, message);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void removeRocket(final RemoveRocketMessage message) {
        try {
            if (!this.lobbyScreen.messageSession.isConnected()) {
                return;
            }
            String url = String.format("/ia-ws/remove-rocket/%s", this.lobbyScreen.gameCode);
            this.lobbyScreen.messageSession.send(url, message);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void sendTransfer(final TransferMessage message) {
        try {
            if (!this.lobbyScreen.messageSession.isConnected()) {
                return;
            }
            String url = String.format("/ia-ws/transfer/%s", this.lobbyScreen.gameCode);
            this.lobbyScreen.messageSession.send(url, message);
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
