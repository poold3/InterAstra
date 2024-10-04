package io.github.interastra.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.interastra.Main;

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

    public MainMenuScreen(final Main game) {
        this.game = game;

        this.viewport = new ScreenViewport();
        this.stage = new Stage(this.viewport);
        Gdx.input.setInputProcessor(this.stage);
        this.skin = this.game.assetManager.get("spaceskin/spaceskin.json", Skin.class);
        this.background = this.game.assetManager.get("background.png", Texture.class);
        this.spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        float worldWidth = this.viewport.getWorldWidth();
        float worldHeight = this.viewport.getWorldHeight();

        Image backgroundImage = new Image(this.background);
        backgroundImage.setFillParent(true);
        this.stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        this.stage.addActor(table);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = this.skin.getFont("Teko-48");

        Label gameTitleLabel = new Label("Inter Astra", titleLabelStyle);
        table.add(gameTitleLabel).expandX().center().padTop(20);
        table.row();

        TextField usernameTextField = new TextField("Username", this.skin);
        usernameTextField.setAlignment(Align.center);
        table.add(usernameTextField).center().padTop(30f).size(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
        table.row();

        TextButton newGameButton = new TextButton("New Game", this.skin);
        table.add(newGameButton).center().padTop(10f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        table.row();

        TextButton joinGameButton = new TextButton("Join Game", this.skin);
        table.add(joinGameButton).center().padTop(10f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        table.row();

        TextButton exitGameButton = new TextButton("Exit", this.skin);
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitGameButton).center().padTop(50f).size(BUTTON_WIDTH, BUTTON_HEIGHT);
        table.row();
    }

    @Override
    public void render(float delta) {
        if (false) {
            this.game.setScreen(new GameScreen(this.game));
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
        this.background.dispose();
        this.skin.dispose();
        this.spriteBatch.dispose();
        this.stage.dispose();
    }
}
