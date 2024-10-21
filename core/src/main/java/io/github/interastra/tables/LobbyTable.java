package io.github.interastra.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Clipboard;
import io.github.interastra.message.models.LobbyPlayerModel;
import io.github.interastra.rest.RestService;
import io.github.interastra.screens.LobbyScreen;
import io.github.interastra.services.ClickListenerService;


public class LobbyTable extends Table {
    public static final float PLAYER_LABEL_WIDTH = 120f;
    public static final float PLAYER_LABEL_HEIGHT = 50f;
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
        this.screen.playersLock.lock();
        for (LobbyPlayerModel player : this.screen.players) {
            this.playersTable.add(this.getPlayerTable(player.name(), player.ready()))
                .maxWidth(PLAYER_LABEL_WIDTH).maxHeight(PLAYER_LABEL_HEIGHT).padLeft(10f).padRight(10f);

            if (player.name().equals(this.screen.myName)) {
                this.playerReadyButton.setText(player.ready() ? "Ready Down" : "Ready Up");
            }
        }
        this.screen.playersLock.unlock();
    }

    public Table getPlayerTable(final String name, final boolean ready) {
        Table playerTable = new Table();
        playerTable.setBackground(this.skin.getDrawable("button_square_header_notch_rectangle"));

        Label playerReadyLabel = new Label(ready ? "Ready" : "Not Ready", this.skin);
        playerTable.add(playerReadyLabel).maxWidth(PLAYER_LABEL_WIDTH / 2f - 8f).uniform().growX().right();
        playerTable.add().maxWidth(PLAYER_LABEL_WIDTH / 2f).uniform().expandX();

        playerTable.row();

        Label playerNameLabel = new Label(name, this.skin);
        playerNameLabel.setColor(0f, 0f, 0f, 1f);
        playerNameLabel.setEllipsis(true);
        playerTable.add(playerNameLabel).colspan(2).maxWidth(PLAYER_LABEL_WIDTH - 16f).expandX().center().padTop(10f);

        return playerTable;
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
        return playerReadyButton;
    }

    public TextButton getLeaveButton() {
        TextButton leaveButton = new TextButton("Leave", this.skin);
        leaveButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.leaveSound.play();
                screen.leave();
            }
        });
        return leaveButton;
    }
}
