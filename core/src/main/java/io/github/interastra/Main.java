package io.github.interastra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.interastra.screens.LoadingScreen;

public class Main extends Game {
    public AssetManager assetManager;
    public SpriteBatch spriteBatch;

    @Override
    public void create() {
        this.assetManager = new AssetManager();
        this.assetManager.load("icons/icons.atlas", TextureAtlas.class);
        this.assetManager.load("planets/planets.atlas", TextureAtlas.class);
        this.assetManager.load("spacecraft/spacecraft.atlas", TextureAtlas.class);
        this.assetManager.load("background.png", Texture.class);
        this.assetManager.load("spaceskin/spaceskin.json", Skin.class);
        this.assetManager.load("audio/bad.mp3", Sound.class);
        this.assetManager.load("audio/good.mp3", Sound.class);
        this.assetManager.load("audio/leave.mp3", Sound.class);
        this.assetManager.load("audio/button.mp3", Sound.class);
        this.assetManager.load("audio/money.mp3", Sound.class);
        this.assetManager.load("audio/build.mp3", Sound.class);
        this.assetManager.load("audio/devalue.mp3", Sound.class);

        this.spriteBatch = new SpriteBatch();
        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        this.spriteBatch.dispose();
        this.assetManager.dispose();
    }


}
