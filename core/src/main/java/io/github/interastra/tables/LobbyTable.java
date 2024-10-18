package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.message.models.LobbyPlayerModel;
import io.github.interastra.screens.LobbyScreen;

import java.util.ArrayList;

public class LobbyTable extends Table {
    public static final float PLAYER_LABEL_WIDTH = 200f;

    private final LobbyScreen screen;
    private final Skin skin;

    public LobbyTable(final LobbyScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.center();

        Label gameCodeLabel = this.getGameCodeLabel();
        this.add(gameCodeLabel).padTop(20f).width(gameCodeLabel.getPrefWidth() + 50f).center();
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
        this.screen.playersLock.lock();
        for (LobbyPlayerModel player : this.screen.players) {
            Label playerLabel = this.getPlayerLabel(player.name(), player.name().equals(this.screen.myName));
            playerLabel.setEllipsis(true);
            this.add(playerLabel)
                .pad(20f, 10f, 0f, 10f)
                .width(PLAYER_LABEL_WIDTH)
                .center();
        }
        this.screen.playersLock.unlock();
        this.row();
    }

    public Label getPlayerLabel(final String name, final boolean self) {
        Label.LabelStyle playerLabelStyle = new Label.LabelStyle();
        playerLabelStyle.font = this.skin.getFont("Teko-32");
        playerLabelStyle.background = this.skin.getDrawable("panel_square");

        Label playerLabel = new Label(String.format(name), playerLabelStyle);
        playerLabel.setAlignment(Align.center);
        return playerLabel;
    }

//    public TextButton getReadyButton(final boolean self) {
//
//    }

    @Override
    public void act(float delta) {

    }
}
