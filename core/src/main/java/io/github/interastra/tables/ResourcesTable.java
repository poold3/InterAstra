package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.models.PlanetResource;
import io.github.interastra.models.Player;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

public class ResourcesTable extends Table {
    public final float RESOURCE_CELL_WIDTH = 80f;
    public final float TRADE_ICON_SIZE = 30f;

    private final GameScreen screen;
    private final Skin skin;
    public Label basesLabel;
    public Label balanceLabel;
    public Label ironLabel;
    public Label oilLabel;
    public Label siliconLabel;
    public Label lithiumLabel;
    public Label helium3Label;
    private float updateTimer = 0f;

    public ResourcesTable(final GameScreen screen, final Skin skin) {
        super();

        this.screen = screen;
        this.skin = skin;

        this.setFillParent(true);
        this.right();

        this.addResourceTextLabel("Bases");
        this.basesLabel = new Label("", this.skin);
        this.addResourceLabel(this.basesLabel);

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

        ImageButton buySellImageButton = new ImageButton(new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("money-white")));
        buySellImageButton.addListener(new ColorTextTooltip("Buy/Sell Resources", new InstantTooltipManager(), this.skin, Color.BLACK));
        buySellImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.toggleBuySellTable();
            }
        });
        this.add(buySellImageButton).size(TRADE_ICON_SIZE).pad(2f).right();
        this.row();

        ImageButton transferImageButton = new ImageButton(new TextureRegionDrawable(this.screen.iconsTextureAtlas.findRegion("transfer")));
        transferImageButton.addListener(new ColorTextTooltip("Transfer Resources", new InstantTooltipManager(), this.skin, Color.BLACK));
        transferImageButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.toggleTransferTable();
            }
        });
        this.add(transferImageButton).size(TRADE_ICON_SIZE).pad(2f).right();
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

        this.updateTimer += delta;
        if (this.updateTimer < 1f) {
            return;
        }

        this.updateTimer -= 1f;

        // Cap resources
        this.screen.myPlayer.balance = MathUtils.clamp(this.screen.myPlayer.balance, 0f, Player.BALANCE_CAP);
        this.capResource(PlanetResource.PLANET_RESOURCE.IRON);
        this.capResource(PlanetResource.PLANET_RESOURCE.OIL);
        this.capResource(PlanetResource.PLANET_RESOURCE.SILICON);
        this.capResource(PlanetResource.PLANET_RESOURCE.LITHIUM);
        this.capResource(PlanetResource.PLANET_RESOURCE.HELIUM3);

        float balance = this.screen.myPlayer.balance;
        float ironBalance = this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.IRON);
        float oilBalance = this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.OIL);
        float siliconBalance = this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.SILICON);
        float lithiumBalance = this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.LITHIUM);
        float helium3Balance = this.screen.myPlayer.resourceBalances.get(PlanetResource.PLANET_RESOURCE.HELIUM3);

        this.basesLabel.setText(String.format("%d / %d", this.screen.myPlayer.bases, this.screen.basesToWin));
        this.balanceLabel.setText(String.format("$%.2f (%.0f%%)", balance, balance / Player.BALANCE_CAP * 100f));
        this.ironLabel.setText(String.format("%.3f (%.0f%%)", ironBalance, ironBalance / PlanetResource.PLANET_RESOURCE_CAPS[PlanetResource.PLANET_RESOURCE.IRON.ordinal()] * 100f));
        this.oilLabel.setText(String.format("%.3f (%.0f%%)", oilBalance, oilBalance / PlanetResource.PLANET_RESOURCE_CAPS[PlanetResource.PLANET_RESOURCE.OIL.ordinal()] * 100f));
        this.siliconLabel.setText(String.format("%.3f (%.0f%%)", siliconBalance, siliconBalance / PlanetResource.PLANET_RESOURCE_CAPS[PlanetResource.PLANET_RESOURCE.SILICON.ordinal()] * 100f));
        this.lithiumLabel.setText(String.format("%.3f (%.0f%%)", lithiumBalance, lithiumBalance / PlanetResource.PLANET_RESOURCE_CAPS[PlanetResource.PLANET_RESOURCE.LITHIUM.ordinal()] * 100f));
        this.helium3Label.setText(String.format("%.3f (%.0f%%)", helium3Balance, helium3Balance / PlanetResource.PLANET_RESOURCE_CAPS[PlanetResource.PLANET_RESOURCE.HELIUM3.ordinal()] * 100f));
    }

    private void capResource(PlanetResource.PLANET_RESOURCE planetResource) {
        this.screen.myPlayer.resourceBalances.computeIfPresent(
            planetResource,
            (k, currentBalance) -> MathUtils.clamp(currentBalance, 0f, PlanetResource.PLANET_RESOURCE_CAPS[k.ordinal()])
        );
    }
}
