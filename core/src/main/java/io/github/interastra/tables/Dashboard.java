package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.interastra.screens.GameScreen;

public class Dashboard extends Table {
    public static final float DASHBOARD_BUTTON_WIDTH = 150f;
    public static final float DASHBOARD_BUTTON_HEIGHT = 60f;

    public final GameScreen screen;
    public final Skin skin;
    public boolean isVisible = false;
    public Label titleLabel;
    public Table contentTable;

    public Dashboard(final GameScreen screen, final Skin skin) {
        super();
        this.screen = screen;
        this.skin = skin;

        this.setWidth(this.screen.stageViewport.getWorldWidth() / 2f);
        this.setHeight(this.screen.stageViewport.getWorldHeight() - PlanetsTable.MAX_BUTTON_SIZE - (PlanetsTable.BUTTON_PAD) * 2f);
        this.setPosition(
            (this.screen.stageViewport.getWorldWidth() / 2f) - (this.getWidth() / 2f),
            (PlanetsTable.MAX_BUTTON_SIZE + (PlanetsTable.BUTTON_PAD * 2f))
        );
        this.top();
        Drawable background = this.skin.getDrawable("panel_square_screws");
        this.setBackground(background);

        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        titleLabelStyle.font = this.skin.getFont("Teko-48");

        this.titleLabel = new Label("", titleLabelStyle);
        this.add(titleLabel).expandX().center().pad(5f);
        this.row();

        this.contentTable = new Table();
        this.contentTable.top();
        ScrollPane scrollPane = new ScrollPane(this.contentTable, this.skin);
        scrollPane.addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (getStage() != null) {
                    getStage().setScrollFocus(scrollPane);
                }
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (getStage() != null) {
                    getStage().setScrollFocus(null);
                }
            }
        });
        this.add(scrollPane).grow().padTop(10f);
    }
}
