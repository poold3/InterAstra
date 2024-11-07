package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.models.PlanetResource;
import io.github.interastra.screens.GameScreen;

public class ResourcesTable extends Table {
    public final float RESOURCE_CELL_WIDTH = 80f;

    private final GameScreen screen;
    private final Skin skin;
    public Label balanceLabel;
    public Label ironLabel;
    public Label oilLabel;
    public Label siliconLabel;
    public Label lithiumLabel;
    public Label helium3Label;

    public ResourcesTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.right();

        this.addResourceTextLabel("Balance");
        this.balanceLabel = new Label("", this.skin);
        this.addResourceLabel(this.balanceLabel);

        this.addResourceTextLabel("Iron");
        this.ironLabel = new Label("", this.skin);
        this.addResourceLabel(this.ironLabel);

        this.addResourceTextLabel("Oil");
        this.oilLabel = new Label("", this.skin);
        this.addResourceLabel(this.oilLabel);

        this.addResourceTextLabel("Silicon");
        this.siliconLabel = new Label("", this.skin);
        this.addResourceLabel(this.siliconLabel);

        this.addResourceTextLabel("Lithium");
        this.lithiumLabel = new Label("", this.skin);
        this.addResourceLabel(this.lithiumLabel);

        this.addResourceTextLabel("Helium3");
        this.helium3Label = new Label("", this.skin);
        this.addResourceLabel(this.helium3Label);
    }

    public void addResourceTextLabel(final String text) {
        Label resourceTextLabel = new Label(text, this.skin);
        resourceTextLabel.setAlignment(Align.right);
        this.add(resourceTextLabel).maxWidth(RESOURCE_CELL_WIDTH).pad(2f).right();
        this.row();
    }

    public void addResourceLabel(final Label resourceLabel) {
        resourceLabel.setAlignment(Align.right);
        this.add(resourceLabel).maxWidth(RESOURCE_CELL_WIDTH).pad(2f).padBottom(10f).right();
        this.row();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.balanceLabel.setText(String.format("$%.2f", this.screen.myPlayer.balance));
        this.ironLabel.setText(String.format("%.3f", this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.IRON)));
        this.oilLabel.setText(String.format("%.3f", this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.OIL)));
        this.siliconLabel.setText(String.format("%.3f", this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.SILICON)));
        this.lithiumLabel.setText(String.format("%.3f", this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.LITHIUM)));
        this.helium3Label.setText(String.format("%.3f", this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.HELIUM3)));
    }
}
