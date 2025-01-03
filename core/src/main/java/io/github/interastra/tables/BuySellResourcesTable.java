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
import io.github.interastra.services.InterAstraLog;
import io.github.interastra.tooltips.ColorTextTooltip;
import io.github.interastra.tooltips.InstantTooltipManager;

import java.util.logging.Level;

public class BuySellResourcesTable extends Dashboard {

    private final EnterResourcesTable enterResourcesTable;
    private final ColorLabel buyLabel;
    private final ColorLabel sellLabel;
    private Price currentPrice;
    public Label revaluationLabel;
    public float revaluationLabelUpdateTimer = 0f;
    public ColorTextTooltip sellRateTextToolTip;
    public ColorTextTooltip buyRateTextToolTip;
    public float[] planetResourceSellRates = new float[PlanetResource.PLANET_RESOURCE_SELL_BASE_RATE.length];
    public float[] planetResourceBuyRates = new float[PlanetResource.PLANET_RESOURCE_BUY_BASE_RATE.length];
    public float resourceRevaluationTimer = 0f;

    public BuySellResourcesTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        System.arraycopy(PlanetResource.PLANET_RESOURCE_SELL_BASE_RATE, 0, this.planetResourceSellRates, 0, PlanetResource.PLANET_RESOURCE_SELL_BASE_RATE.length);
        System.arraycopy(PlanetResource.PLANET_RESOURCE_BUY_BASE_RATE, 0, this.planetResourceBuyRates, 0, PlanetResource.PLANET_RESOURCE_BUY_BASE_RATE.length);

        this.titleLabel.setText("Buy/Sell Resources");

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.revaluationLabel = new Label("", headerLabelStyle);
        this.contentTable.add(this.revaluationLabel).colspan(2);
        this.contentTable.row();

        this.enterResourcesTable = new EnterResourcesTable(this.screen.stage, this.skin, false);

        this.contentTable.add(this.enterResourcesTable).colspan(2);
        this.contentTable.row();

        this.contentTable.add(new Label("Buy or Sell", headerLabelStyle)).colspan(2).pad(5f);
        this.contentTable.row();

        TextButton buyTextButton = new TextButton("Buy", this.skin);
        buyTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Price buyAmount = enterResourcesTable.getPrice();
                    if (buyAmount == null) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("Invalid resource value(s).");
                        return;
                    }

                    float requiredBalance = buyAmount.getBuyAmount(screen);
                    float balanceStillNeeded = requiredBalance - screen.myPlayer.balance;
                    if (balanceStillNeeded > 0f && !screen.noCostMode) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage(String.format("Missing: $%.2f", balanceStillNeeded));
                        return;
                    }

                    screen.moneySound.play();
                    screen.myPlayer.balance -= requiredBalance;
                    buyAmount.sell(screen.myPlayer);
                    enterResourcesTable.resetUI();
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        this.buyRateTextToolTip = new ColorTextTooltip(this.getBuyRateString(), new InstantTooltipManager(), this.skin, Color.BLACK);
        buyTextButton.addListener(this.buyRateTextToolTip);
        this.contentTable.add(buyTextButton).size(Dashboard.DASHBOARD_BUTTON_WIDTH, Dashboard.DASHBOARD_BUTTON_HEIGHT).padTop(10f);

        TextButton sellTextButton = new TextButton("Sell", this.skin);
        sellTextButton.addListener(new ClickListenerService(this.screen.buttonSound, Cursor.SystemCursor.Hand) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Price sellAmount = enterResourcesTable.getPrice();
                    if (sellAmount == null) {
                        screen.badSound.play(0.5f);
                        screen.notificationTable.setMessage("Invalid resource value(s).");
                        return;
                    }


                    if (!sellAmount.canAfford(screen) && !screen.noCostMode) {
                        screen.badSound.play(0.5f);
                        return;
                    }

                    screen.moneySound.play();
                    screen.myPlayer.balance += sellAmount.getSellAmount(screen);
                    sellAmount.purchase(screen);
                    enterResourcesTable.resetUI();
                } catch (Exception e) {
                    InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
                }
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
        this.buyLabel.setText("$0.00");
        this.sellLabel.setText("$0.00");
    }

    public String getBuyRateString() {
        return String.format(
            "Iron: $%.2f, Oil: $%.2f, Aluminum: $%.2f, Copper: $%.2f, Stone: $%.2f",
            this.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.IRON.ordinal()],
            this.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.OIL.ordinal()],
            this.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.ALUMINUM.ordinal()],
            this.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.COPPER.ordinal()],
            this.planetResourceBuyRates[PlanetResource.PLANET_RESOURCE.STONE.ordinal()]
        );
    }

    public String getSellRateString() {
        return String.format(
            "Iron: $%.2f, Oil: $%.2f, Aluminum: $%.2f, Copper: $%.2f, Stone: $%.2f",
            this.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.IRON.ordinal()],
            this.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.OIL.ordinal()],
            this.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.ALUMINUM.ordinal()],
            this.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.COPPER.ordinal()],
            this.planetResourceSellRates[PlanetResource.PLANET_RESOURCE.STONE.ordinal()]
        );
    }

    public void refreshRates() {
        try {
            this.sellRateTextToolTip.getActor().getActor().setText(this.getSellRateString());
            this.buyRateTextToolTip.getActor().getActor().setText(this.getBuyRateString());
            if (this.isVisible) {
                Price newPrice = this.enterResourcesTable.getPrice();
                if (newPrice == null) {
                    this.buyLabel.setText("");
                    this.sellLabel.setText("");
                } else {
                    this.currentPrice = newPrice;
                    this.buyLabel.setText(String.format("$%.2f", this.currentPrice.getBuyAmount(screen)));
                    this.sellLabel.setText(String.format("$%.2f", this.currentPrice.getSellAmount(screen)));
                }
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void update(float delta) {
        try {
            // Update resource valuation
            this.resourceRevaluationTimer += delta;
            if (this.resourceRevaluationTimer >= PlanetResource.RESOURCE_VALUATION_TIMER) {
                this.resourceRevaluationTimer -= PlanetResource.RESOURCE_VALUATION_TIMER;
                for (int i = 0; i < this.planetResourceSellRates.length; ++i) {
                    this.planetResourceSellRates[i] *= PlanetResource.RESOURCE_SELL_VALUATION_RATE;
                }
                for (int i = 0; i < this.planetResourceBuyRates.length; ++i) {
                    this.planetResourceBuyRates[i] *= PlanetResource.RESOURCE_BUY_VALUATION_RATE;
                }
                this.refreshRates();
                this.screen.valueSound.play(0.5f);
                this.screen.notificationTable.setMessage("Resource buy/sell rates have been revalued.");
            }
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);

            Price newPrice = this.enterResourcesTable.getPrice();
            if (newPrice == null) {
                this.buyLabel.setText("");
                this.sellLabel.setText("");
            } else if (!newPrice.equals(this.currentPrice)) {
                this.currentPrice = newPrice;
                this.buyLabel.setText(String.format("$%.2f", this.currentPrice.getBuyAmount(screen)));
                this.sellLabel.setText(String.format("$%.2f", this.currentPrice.getSellAmount(screen)));
            }

            this.revaluationLabelUpdateTimer += delta;
            if (this.revaluationLabelUpdateTimer < 1f) {
                return;
            }

            this.revaluationLabelUpdateTimer -= 1f;
            this.revaluationLabel.setText(String.format("Revaluation: %.0f s", PlanetResource.RESOURCE_VALUATION_TIMER - this.resourceRevaluationTimer));
        } catch (Exception e) {
            InterAstraLog.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
