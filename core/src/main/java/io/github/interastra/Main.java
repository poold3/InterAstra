package io.github.interastra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.screens.LoadingScreen;

public class Main extends Game {
    public AssetManager assetManager;

    @Override
    public void create() {
        this.assetManager = new AssetManager();
        this.assetManager.load("game/InterAstra.atlas", TextureAtlas.class);
        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
    }


}
