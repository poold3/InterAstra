package io.github.interastra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.interastra.Main;

public class LoadingScreen implements Screen {
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final float PROGRESS_BAR_WIDTH = 100;
    public static final float PROGRESS_BAR_HEIGHT = 25;
    public static final float WAIT_LOADING_TIME = 0.75f;

    public final Main game;
    public FitViewport viewport;
    public SpriteBatch spriteBatch;
    public BitmapFont bitmapFont;
    public ShapeRenderer shapeRenderer;

    public float loadingTime;

    public LoadingScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Prepare your screen here.
        Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);

        this.viewport = new FitViewport(WIDTH, HEIGHT);
        this.spriteBatch = new SpriteBatch();
        this.bitmapFont = new BitmapFont();
        this.bitmapFont.setColor(Color.WHITE);
        this.shapeRenderer = new ShapeRenderer();

        this.loadingTime = 0f;
    }

    @Override
    public void render(float delta) {
        if (this.game.assetManager.update(16)) {
            this.loadingTime += delta;
            if (this.loadingTime >= WAIT_LOADING_TIME) {
                this.game.setScreen(new GameScreen(this.game));
                this.dispose();
                return;
            }
        }

        ScreenUtils.clear(Color.BLACK);
        this.viewport.apply();
        this.spriteBatch.setProjectionMatrix(this.viewport.getCamera().combined);
        this.shapeRenderer.setProjectionMatrix(this.viewport.getCamera().combined);

        float startX = (WIDTH / 2f) - (PROGRESS_BAR_WIDTH / 2f);
        float startY = 100f;

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(Color.WHITE);
        this.shapeRenderer.rect(startX, startY, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        this.shapeRenderer.end();

        float progress = this.game.assetManager.getProgress() * (PROGRESS_BAR_WIDTH - 3f);

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(Color.WHITE);
        this.shapeRenderer.rect(startX + 1f, startY + 2f, progress, PROGRESS_BAR_HEIGHT - 3f);
        this.shapeRenderer.end();

        GlyphLayout layout = new GlyphLayout(this.bitmapFont, "Inter Astra");

        this.spriteBatch.begin();
        this.bitmapFont.draw(this.spriteBatch, layout, (this.viewport.getWorldWidth() - layout.width) / 2, 150f);
        this.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        this.viewport.update(width, height, true);
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
        this.spriteBatch.dispose();
        this.bitmapFont.dispose();
        this.shapeRenderer.dispose();
    }
}
