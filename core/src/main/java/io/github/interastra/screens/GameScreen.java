package io.github.interastra.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    public static final float WORLD_SIZE = 1000f;
    public static final float CAMERA_SIZE = 50f;

    public Game game;
    public OrthographicCamera camera;
    public FitViewport viewport;

    public GameScreen(final Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Set fullscreen
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_SIZE, WORLD_SIZE, camera);
        this.camera.zoom = getCameraZoomFromScreenUnitSize(CAMERA_SIZE);
    }

    @Override
    public void render(float delta) {
        this.input();
        ScreenUtils.clear(Color.BLACK);
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
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
    }

    /**
     * Gets the camera zoom value for a certain screen size.
     * @param screenSize Provided in world units. If you wanted a screen size of 500 x 500 world units, you would pass
     *                   500f as the parameter value.
     * @return The camera zoom value to get the provided screen size.
     */
    public static float getCameraZoomFromScreenUnitSize(float screenSize) {
        return (1f / WORLD_SIZE) * screenSize;
    }
}
