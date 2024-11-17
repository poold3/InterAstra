package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.interastra.services.CameraOperatorService;

public class CoordinatesTable extends Table {
    private final Label coordinateLabel;
    private final CameraOperatorService cameraOperatorService;

    public CoordinatesTable(final CameraOperatorService cameraOperatorService, final Skin skin) {
        super();
        this.setFillParent(true);

        this.cameraOperatorService = cameraOperatorService;
        Label.LabelStyle coordinateLabelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        coordinateLabelStyle.font = skin.getFont("Teko-32");
        this.coordinateLabel = new Label("", coordinateLabelStyle);
        this.add(this.coordinateLabel).expandX().expandY().bottom().left().pad(5f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.coordinateLabel.setText(String.format("%.0f, %.0f", this.cameraOperatorService.position.x, this.cameraOperatorService.position.y));
    }
}
