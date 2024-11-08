package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;

public class OptionsTable extends Table {
    public static final float MIN_SIZE = 400f;

    private final GameScreen screen;
    private final Skin skin;
    private final float size;
    public boolean isVisible = false;

    public OptionsTable(final GameScreen screen, final Skin skin) {
        this.screen = screen;
        this.skin = skin;
        this.size = Math.max(this.screen.stageViewport.getWorldWidth() / 4f, MIN_SIZE);

        this.setWidth(this.size);
        this.setHeight(this.size);
        this.setPosition(
            (this.screen.stageViewport.getWorldWidth() / 2f) - (this.size / 2f),
            (this.screen.stageViewport.getWorldHeight() / 2f) - (this.size / 2f)
        );
        this.top();
        this.setZIndex(99);
        Drawable background = this.skin.getDrawable("panel_square_screws");
        this.setBackground(background);

        this.add(this.getOptionsLabel()).expandX().center().padTop(20f);
        this.row();

        this.add(this.getResumeButton()).maxWidth(MainMenuTable.BUTTON_WIDTH).maxHeight(MainMenuTable.BUTTON_HEIGHT).padTop(40f);
        this.row();

        this.add(this.getLeaveButton()).maxWidth(MainMenuTable.BUTTON_WIDTH).maxHeight(MainMenuTable.BUTTON_HEIGHT);
        this.row();
    }

    public Label getOptionsLabel() {
        Label.LabelStyle pauseLabelStyle = new Label.LabelStyle();
        pauseLabelStyle.font = this.skin.getFont("Teko-48");

        return new Label("Options Menu", pauseLabelStyle);
    }

    public TextButton getResumeButton() {
        TextButton resumeButton = new TextButton("Resume Game", this.skin);
        resumeButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.toggleOptionsMenu();
            }
        });
        return resumeButton;
    }

    public TextButton getLeaveButton() {
        TextButton leaveButton = new TextButton("Leave Game", this.skin);
        leaveButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.leaveSound.play();
                screen.leaveGame = true;
            }
        });
        return leaveButton;
    }
}
