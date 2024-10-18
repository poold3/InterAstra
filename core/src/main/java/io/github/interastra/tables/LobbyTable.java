package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.screens.LobbyScreen;

public class LobbyTable extends Table {

    private final LobbyScreen screen;
    private final Skin skin;

    public LobbyTable(final LobbyScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.center();

        Label gameCodeLabel = this.getGameCodeLabel();
        this.add(gameCodeLabel).expandX().center().padTop(20f).width(200f);
        this.row();

        this.addPlayers();
    }

    public Label getGameCodeLabel() {
        Label.LabelStyle gameCodeLabelStyle = new Label.LabelStyle();
        gameCodeLabelStyle.font = this.skin.getFont("Teko-32");
        gameCodeLabelStyle.background = this.skin.getDrawable("panel_square");

        Label gameCodeLabel = new Label(String.format("Lobby: %s", this.screen.gameCode), gameCodeLabelStyle);
        gameCodeLabel.setAlignment(Align.center);
        return gameCodeLabel;
    }

    public void addPlayers() {

    }

    @Override
    public void act(float delta) {

    }
}
