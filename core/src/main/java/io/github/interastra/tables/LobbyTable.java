package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.message.models.LobbyPlayerModel;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.services.ValidationService;


public class LobbyTable extends Table {
    public static final float PLAYER_LABEL_WIDTH = 150f;
    public static final float READY_BUTTON_WIDTH = 100f;

    private final LobbyScreen screen;
    private final Skin skin;
    public Table playersTable;

    public LobbyTable(final LobbyScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.center();

        Label gameCodeLabel = this.getGameCodeLabel();
        this.add(gameCodeLabel).padTop(20f).width(gameCodeLabel.getPrefWidth() + 50f).center();
        this.row();

        this.playersTable = new Table();
        this.add(this.playersTable).padTop(20f).expandX().center();
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
        this.playersTable.clearChildren();
        this.screen.playersLock.lock();
        for (LobbyPlayerModel player : this.screen.players) {
            Table playerTable = this.getPlayerTable(player.name(), player.ready(), player.name().equals(this.screen.myName));
            this.playersTable.add(playerTable);
        }
        this.playersTable.row();
        this.screen.playersLock.unlock();

    }

    public Table getPlayerTable(final String name, final boolean ready, final boolean self) {
        Table playerTable = new Table();
        playerTable.setDebug(true);
        playerTable.center();
        Drawable background = this.skin.getDrawable("button_square_header_blade_square_screws");
        background.setMinWidth(0);
        background.setMinHeight(0);
        playerTable.setBackground(background);

        Label playerReadyLabel = new Label(ready ? "Ready" : "Not Ready", this.skin);
        playerReadyLabel.setEllipsis(true);
        playerTable.add(playerReadyLabel).left();

        playerTable.row();

        Label playerNameLabel = new Label(name, this.skin);
        playerNameLabel.setEllipsis(true);
        playerTable.add(playerNameLabel);

        return playerTable;
    }

    public TextButton getPlayerReadyButton(final boolean ready, final boolean self) {
        TextButton playerReadyButton = new TextButton(ready ? "Ready" : "Not Ready", this.skin);
        if (self) {
            playerReadyButton.addListener(new ClickListenerService(this.screen.buttonSound) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    screen.notificationTable.startLoading("Setting ready status");
                    RestService.setReady(screen, screen.gameCode, screen.myName, !screen.getReadyStatus(screen.myName));
                }
            });
        } else {
            playerReadyButton.setDisabled(true);
        }
        return playerReadyButton;
    }

    @Override
    public void act(float delta) {

    }
}
