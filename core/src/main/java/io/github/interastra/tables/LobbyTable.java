package io.github.interastra.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Clipboard;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.message.models.LobbyPlayerMessageModel;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.services.ClickListenerService;


public class LobbyTable extends Table {
    public static final float PLAYER_LABEL_WIDTH = 120f;
    public static final float PLAYER_LABEL_HEIGHT = 70f;
    public static final float BUTTON_WIDTH = 100f;
    public static final float BUTTON_HEIGHT = 42f;

    private final LobbyScreen screen;
    private final Skin skin;
    public Table playersTable;
    public TextButton playerReadyButton;

    public LobbyTable(final LobbyScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.center();

        Label gameTitleLabel = this.getTitleLabel();
        this.add(gameTitleLabel).expandX().center().padTop(20);
        this.row();

        Table gameCodeTable = this.getGameCodeTable();
        this.add(gameCodeTable).padTop(20f);
        this.row();

        this.playersTable = new Table();
        this.playersTable.add()
            .width(PLAYER_LABEL_WIDTH).height(PLAYER_LABEL_HEIGHT);
        this.add(this.playersTable).padTop(20f).expandX().center();
        this.row();

        this.playerReadyButton = this.getPlayerReadyButton();
        this.add(this.playerReadyButton).padTop(50f).maxWidth(BUTTON_WIDTH).maxHeight(BUTTON_HEIGHT);
        this.row();
        this.add(this.getLeaveButton()).padTop(10f).maxWidth(BUTTON_WIDTH).maxHeight(BUTTON_HEIGHT);
    }

    public Label getTitleLabel() {
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = this.skin.getFont("Teko-48");

        return new Label("Inter Astra", titleLabelStyle);
    }

    public Table getGameCodeTable() {
        Table gameCodeTable = new Table();
        gameCodeTable.setBackground(this.skin.getDrawable("panel_square"));

        Label.LabelStyle gameCodeLabelStyle = new Label.LabelStyle();
        gameCodeLabelStyle.font = this.skin.getFont("Teko-32");
        Label gameCodeLabel = new Label(String.format("%s", this.screen.gameCode), gameCodeLabelStyle);

        gameCodeTable.add(gameCodeLabel).padLeft(10f);

        TextureRegion copyTextureRegion = this.screen.iconsTextureAtlas.findRegion("copy");
        Drawable copyDrawable = new TextureRegionDrawable(copyTextureRegion);
        copyDrawable.setMinWidth(24f);
        copyDrawable.setMinHeight(24f);

        ImageButton.ImageButtonStyle copyButtonStyle = new ImageButton.ImageButtonStyle();
        copyButtonStyle.imageUp = copyDrawable;
        copyButtonStyle.imageDown = copyDrawable;
        ImageButton copyImageButton = new ImageButton(copyButtonStyle);

        copyImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.notificationTable.setMessage("Game code copied to clipboard.");
                Clipboard clipboard = Gdx.app.getClipboard();
                clipboard.setContents(screen.gameCode);
            }
        });

        gameCodeTable.add(copyImageButton).pad(0f, 10f, 0f, 10f);

        return gameCodeTable;
    }

    public void updatePlayers() {
        this.playersTable.clearChildren();
        this.playerReadyButton.setDisabled(true);
        this.screen.players.lock();
        for (LobbyPlayerMessageModel player : this.screen.players.getData()) {
            this.playersTable.add(this.getPlayerContainer(player.name(), player.ready())).padLeft(10f).padRight(10f);

            if (player.name().equals(this.screen.myName)) {
                this.playerReadyButton.setText(player.ready() ? "Ready Down" : "Ready Up");
                this.playerReadyButton.setDisabled(false);
            }
        }
        this.screen.players.unlock();
    }

    public Container<Table> getPlayerContainer(final String name, final boolean ready) {
        Table playerTable = new Table();

        Label playerReadyLabel = new Label(ready ? "Ready" : "Not Ready", this.skin);
        playerReadyLabel.setAlignment(Align.center);
        playerTable.add(playerReadyLabel).height(PLAYER_LABEL_HEIGHT / 2f).width(PLAYER_LABEL_WIDTH / 2f);
        playerTable.add().height(PLAYER_LABEL_HEIGHT / 2f).width(PLAYER_LABEL_WIDTH / 2f);

        playerTable.row();

        ColorLabel playerNameLabel = new ColorLabel(name, this.skin, Color.BLACK);
        playerNameLabel.setEllipsis(true);
        playerNameLabel.setAlignment(Align.center);
        playerTable.add(playerNameLabel).colspan(2).height(PLAYER_LABEL_HEIGHT / 2).width(PLAYER_LABEL_WIDTH);

        Container<Table> playerContainer = new Container<>(playerTable);
        playerContainer.size(PLAYER_LABEL_WIDTH, PLAYER_LABEL_HEIGHT);
        playerContainer.setBackground(this.skin.getDrawable("button_square_header_notch_rectangle"));

        return playerContainer;
    }

    public TextButton getPlayerReadyButton() {
        TextButton playerReadyButton = new TextButton("Ready Up", this.skin);
        playerReadyButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.notificationTable.startLoading("Setting ready status");
                RestService.setReady(screen, screen.gameCode, screen.myName, !screen.getReadyStatus(screen.myName));
            }
        });
        playerReadyButton.setDisabled(true);
        return playerReadyButton;
    }

    public TextButton getLeaveButton() {
        TextButton leaveButton = new TextButton("Leave", this.skin);
        leaveButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.leaveSound.play();
                screen.leaveLobby = true;
            }
        });
        return leaveButton;
    }
}
