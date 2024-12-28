package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.interastra.services.CameraOperatorService;
import io.github.interastra.services.InterAstraLog;

import java.util.logging.Level;

public class CoordinatesTable extends Table {
    private final Label coordinateLabel;
    private final CameraOperatorService cameraOperatorService;

    public CoordinatesTable(final CameraOperatorService cameraOperatorService, final Skin skin) {
        super();
        this.setFillParent(true);

        this.cameraOperatorService = cameraOperatorService;
        this.coordinateLabel = new Label("", skin);
        this.add(this.coordinateLabel).expandX().expandY().bottom().left().pad(5f);
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
            this.coordinateLabel.setText(String.format("%.0f, %.0f", this.cameraOperatorService.position.x, this.cameraOperatorService.position.y));
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
