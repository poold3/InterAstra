package io.github.interastra.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import io.github.interastra.labels.ColorLabel;
import io.github.interastra.models.PlanetResource;
import io.github.interastra.models.Price;
import io.github.interastra.screens.GameScreen;
import io.github.interastra.services.ClickListenerService;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

public class BuySellResourcesTable extends Dashboard {

    private final EnterResourcesTable enterResourcesTable;
    private final ColorLabel buyLabel;
    private final ColorLabel sellLabel;
    private Price currentPrice;
    public ColorTextTooltip sellRateTextToolTip;
    //public float[] planetResourceSellRates = new float[PlanetResource.PLANET_RESOURCE_SELL_BASE_RATE.length];
    //public float[] planetResourceBuyRates = new float[PlanetResource.PLANET_RESOURCE_BUY_BASE_RATE.length];

    public BuySellResourcesTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        this.titleLabel.setText("Buy/Sell Resources");

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.enterResourcesTable = new EnterResourcesTable(this.screen.stage, this.skin, false);

        this.contentTable.add(this.enterResourcesTable).colspan(2);
        this.contentTable.row();

        this.contentTable.add(new Label("Buy or Sell", headerLabelStyle)).colspan(2).pad(5f);
        this.contentTable.row();

        TextButton buyTextButton = new TextButton("Buy", this.skin);
        buyTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Price buyAmount = enterResourcesTable.getPrice();
                if (buyAmount == null) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Invalid resource value(s).");
                    return;
                }

                float requiredBalance = buyAmount.getBuyAmount();
                if (requiredBalance > screen.myPlayer.balance && !screen.noCostMode) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You cannot afford this action.");
                    return;
                }

                screen.moneySound.play();
                screen.myPlayer.balance -= requiredBalance;
                buyAmount.sell(screen.myPlayer);
                enterResourcesTable.resetUI();
            }
        });
        buyTextButton.addListener(new ColorTextTooltip(getBuyRateString(), new InstantTooltipManager(), this.skin, Color.BLACK));
        this.contentTable.add(buyTextButton).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).padTop(10f);

        TextButton sellTextButton = new TextButton("Sell", this.skin);
        sellTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Price sellAmount = enterResourcesTable.getPrice();
                if (sellAmount == null) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("Invalid resource value(s).");
                    return;
                }


                if (!sellAmount.canAfford(screen.myPlayer) && !screen.noCostMode) {
                    screen.badSound.play(0.5f);
                    screen.notificationTable.setMessage("You cannot afford this action.");
                    return;
                }

                screen.moneySound.play();
                screen.myPlayer.balance += sellAmount.getSellAmount(screen);
                sellAmount.purchase(screen.myPlayer);
                enterResourcesTable.resetUI();
            }
        });
        this.sellRateTextToolTip = new ColorTextTooltip(this.getSellRateString(), new InstantTooltipManager(), this.skin, Color.BLACK);
        sellTextButton.addListener(this.sellRateTextToolTip);
        this.contentTable.add(sellTextButton).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).padTop(10f);
        this.contentTable.row();

        this.buyLabel = new ColorLabel("", this.skin, Color.BLACK);
        this.buyLabel.setAlignment(Align.center);
        this.contentTable.add(this.buyLabel).width(Dashboard.DASHBOARD_BUTTON_WIDTH).padTop(5f);
        this.sellLabel = new ColorLabel("", this.skin, Color.BLACK);
        this.sellLabel.setAlignment(Align.center);
        this.contentTable.add(this.sellLabel).width(Dashboard.DASHBOARD_BUTTON_WIDTH).padTop(5f);

        this.currentPrice = new Price();
        this.buyLabel.setText(String.format("$%.2f", this.currentPrice.getBuyAmount()));
        this.sellLabel.setText(String.format("$%.2f", this.currentPrice.getSellAmount(screen)));
    }

    public static String getBuyRateString() {
        return String.format(
            "Iron: $%.2f, Oil: $%.2f, Silicon: $%.2f, Lithium: $%.2f, Helium3: $%.2f",
            PlanetResource.PLANET_RESOURCE_BUY_RATE[PlanetResource.PLANET_RESOURCE.IRON.ordinal()],
            PlanetResource.PLANET_RESOURCE_BUY_RATE[PlanetResource.PLANET_RESOURCE.OIL.ordinal()],
            PlanetResource.PLANET_RESOURCE_BUY_RATE[PlanetResource.PLANET_RESOURCE.SILICON.ordinal()],
            PlanetResource.PLANET_RESOURCE_BUY_RATE[PlanetResource.PLANET_RESOURCE.LITHIUM.ordinal()],
            PlanetResource.PLANET_RESOURCE_BUY_RATE[PlanetResource.PLANET_RESOURCE.HELIUM3.ordinal()]
        );
    }

    public String getSellRateString() {
        return String.format(
            "Iron: $%.2f, Oil: $%.2f, Silicon: $%.2f, Lithium: $%.2f, Helium3: $%.2f",
            this.screen.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.IRON.ordinal()],
            this.screen.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.OIL.ordinal()],
            this.screen.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.SILICON.ordinal()],
            this.screen.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.LITHIUM.ordinal()],
            this.screen.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.HELIUM3.ordinal()]
        );
    }

    public void refreshRates() {
        this.sellRateTextToolTip.getActor().getActor().setText(this.getSellRateString());
        if (this.isVisible) {
            Price newPrice = this.enterResourcesTable.getPrice();
            if (newPrice == null) {
                this.buyLabel.setText("");
                this.sellLabel.setText("");
            } else {
                this.currentPrice = newPrice;
                this.buyLabel.setText(String.format("$%.2f", this.currentPrice.getBuyAmount()));
                this.sellLabel.setText(String.format("$%.2f", this.currentPrice.getSellAmount(screen)));
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Price newPrice = this.enterResourcesTable.getPrice();
        if (newPrice == null) {
            this.buyLabel.setText("");
            this.sellLabel.setText("");
        } else if (!newPrice.equals(this.currentPrice)) {
            this.currentPrice = newPrice;
            this.buyLabel.setText(String.format("$%.2f", this.currentPrice.getBuyAmount()));
            this.sellLabel.setText(String.format("$%.2f", this.currentPrice.getSellAmount(screen)));
        }
    }
}
