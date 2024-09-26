package io.github.interastra.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.interastra.Main;
import io.github.interastra.models.CameraEnabledEntity;
import io.github.interastra.models.Planet;
import io.github.interastra.models.Star;
import io.github.interastra.tools.CameraOperator;

import java.util.ArrayList;
import java.util.Collections;

public class GameScreen implements Screen, InputProcessor {
    public static final float MIN_WORLD_SIZE = 1000f;
    public static final int NUM_PLANETS = 9;
    public static final float ARROW_KEY_MOVE_SPEED = 25f;

    public Main game;
    public CameraOperator camera;
    public ExtendViewport viewport;
    public CameraEnabledEntity entityBeingFollowed = null;

    public SpriteBatch spriteBatch;
    public TextureAtlas planetsTextureAtlas;

    public Star sol;
    public ArrayList<Planet> planets;

    public float speedMultiplier;

    public GameScreen(final Main game) {
        this.game = game;
        this.speedMultiplier = 1f;

        Gdx.input.setInputProcessor(this);

        this.camera = new CameraOperator();
        this.viewport = new ExtendViewport(MIN_WORLD_SIZE, MIN_WORLD_SIZE, camera);
        this.camera.setViewport(this.viewport);

        this.spriteBatch = new SpriteBatch();

        // Get our textureAtlas
        this.planetsTextureAtlas = this.game.assetManager.get("planets/planets.atlas", TextureAtlas.class);

        // Load planets
        this.loadSolarSystem();
    }

    @Override
    public void show() {
        // Set fullscreen
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);
    }

    @Override
    public void render(float delta) {
        this.input();
        this.logic();
        this.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        this.camera.center();
        this.sol.reposition(this.viewport.getWorldWidth() / 2f, this.viewport.getWorldHeight() / 2f);
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
        // Destroy screen's assets here.
    }

    public void input() {
        // Escape key to exit
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // Follow planet
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            this.entityBeingFollowed = this.planets.get(0);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            this.entityBeingFollowed = this.planets.get(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            this.entityBeingFollowed = this.planets.get(2);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            this.entityBeingFollowed = this.planets.get(3);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            this.entityBeingFollowed = this.planets.get(4);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            this.entityBeingFollowed = this.planets.get(5);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            this.entityBeingFollowed = this.planets.get(6);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            this.entityBeingFollowed = this.planets.get(7);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            this.entityBeingFollowed = this.planets.get(8);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            this.entityBeingFollowed = this.sol;
        }

        this.moveWithArrows();
    }

    public void logic() {
        // Move planets
        for (Planet planet : this.planets) {
            planet.move(this.viewport.getWorldWidth(), this.viewport.getWorldHeight(), speedMultiplier);
        }

        // If following an entity, tell the camera operator to do so.
        if (this.entityBeingFollowed != null) {
            this.camera.followCameraEnabledEntity(this.entityBeingFollowed);
        }

        // Tell the camera operator to move if needed
        this.camera.move();
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        this.viewport.apply();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);

        // Draw the sprites
        this.spriteBatch.begin();

        this.sol.starSprite.draw(this.spriteBatch);

        for (Planet planet : this.planets) {
            planet.planetSprite.draw(this.spriteBatch);
        }

        this.spriteBatch.end();
    }

    public void loadSolarSystem() {
        // Add Sol
        this.sol = new Star(this.planetsTextureAtlas, "Sol");

        // Add Planets
        this.planets = new ArrayList<>();
        for (int i = 0; i < NUM_PLANETS; ++i) {
            this.planets.add(new Planet(this.planetsTextureAtlas, Planet.PLANET_NAMES[i]));
        }
        Collections.sort(this.planets);
    }

    public void moveWithArrows() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.camera.targetPosition.x -= (ARROW_KEY_MOVE_SPEED * this.camera.zoom);
            this.entityBeingFollowed = null;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.camera.targetPosition.x += (ARROW_KEY_MOVE_SPEED * this.camera.zoom);
            this.entityBeingFollowed = null;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.camera.targetPosition.y += (ARROW_KEY_MOVE_SPEED * this.camera.zoom);
            this.entityBeingFollowed = null;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.camera.targetPosition.y -= (ARROW_KEY_MOVE_SPEED * this.camera.zoom);
            this.entityBeingFollowed = null;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        this.camera.targetZoom += amountY * 0.05f;
        this.camera.targetZoom = MathUtils.clamp(this.camera.targetZoom, this.camera.getZoomForSize(Planet.MAX_PLANET_SIZE), 1f);
        return true;
    }
}
