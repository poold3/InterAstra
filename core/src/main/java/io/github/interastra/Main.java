package io.github.interastra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.interastra.screens.LoadingScreen;

public class Main extends Game {
    public AssetManager assetManager;
    public BitmapFont titleFont;
    public BitmapFont subtitleFont;
    public BitmapFont regularFont;

    @Override
    public void create() {
        this.assetManager = new AssetManager();
        this.assetManager.load("planets/planets.atlas", TextureAtlas.class);
        this.assetManager.load("ship/ship.atlas", TextureAtlas.class);
        this.assetManager.load("background.png", Texture.class);
        this.assetManager.load("ui/uiskin.json", Skin.class);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Teko-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 100;
        this.titleFont = fontGenerator.generateFont(parameter);

        parameter.size = 30;
        this.subtitleFont = fontGenerator.generateFont(parameter);

        parameter.size = 20;
        this.regularFont = fontGenerator.generateFont(parameter);

        fontGenerator.dispose();

        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
        this.titleFont.dispose();
    }


}
