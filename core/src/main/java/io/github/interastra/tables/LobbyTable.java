package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.interastra.screens.MainMenuScreen;

public class LobbyTable extends Table {

    private final MainMenuScreen screen;
    private final Skin skin;

    public LobbyTable(final MainMenuScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;
    }
}
