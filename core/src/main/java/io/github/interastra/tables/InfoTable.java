package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.interastra.models.Planet;
import io.github.interastra.models.Rocket;
import io.github.interastra.screens.GameScreen;

public class InfoTable extends Dashboard {
    public InfoTable(final GameScreen screen, final Skin skin) {
        super(screen, skin);

        this.titleLabel.setText("Information");

        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(this.skin.get(Label.LabelStyle.class));
        headerLabelStyle.font = this.skin.getFont("Teko-32");

        this.contentTable.add(new Label("Base", headerLabelStyle)).colspan(2).pad(5f).padTop(15f);
        this.contentTable.row();
        this.contentTable.add(new Label("Price: " + Planet.BASE_PRICE, skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label(String.format("Cooldown: %.0f s, Multiplier: %.2f", Planet.BASE_COOLDOWN, Planet.BASE_RESOURCE_MULTIPLIER), skin)).colspan(2).pad(5f);
        this.contentTable.row();

        this.contentTable.add(new Label("Tier " + Rocket.ROCKET_TIER_STRING[0] + " Rocket", headerLabelStyle)).colspan(2).pad(5f).padTop(15f);
        this.contentTable.row();
        this.contentTable.add(new Label("Price: " + Rocket.ROCKET_TIER_PRICE[0].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label("Fuel Price: " + Rocket.ROCKET_TIER_FUEL_PRICE[0].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label(Rocket.ROCKET_TIER_STATS[0].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();

        this.contentTable.add(new Label("Tier " + Rocket.ROCKET_TIER_STRING[1] + " Rocket", headerLabelStyle)).colspan(2).pad(5f).padTop(15f);
        this.contentTable.row();
        this.contentTable.add(new Label("Price: " + Rocket.ROCKET_TIER_PRICE[1].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label("Fuel Price: " + Rocket.ROCKET_TIER_FUEL_PRICE[1].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label(Rocket.ROCKET_TIER_STATS[1].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();

        this.contentTable.add(new Label("Tier " + Rocket.ROCKET_TIER_STRING[2] + " Rocket", headerLabelStyle)).colspan(2).pad(5f).padTop(15f);
        this.contentTable.row();
        this.contentTable.add(new Label("Price: " + Rocket.ROCKET_TIER_PRICE[2].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label("Fuel Price: " + Rocket.ROCKET_TIER_FUEL_PRICE[2].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label(Rocket.ROCKET_TIER_STATS[2].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();

        this.contentTable.add(new Label("Tier " + Rocket.ROCKET_TIER_STRING[3] + " Rocket", headerLabelStyle)).colspan(2).pad(5f).padTop(15f);
        this.contentTable.row();
        this.contentTable.add(new Label("Price: " + Rocket.ROCKET_TIER_PRICE[3].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label("Fuel Price: " + Rocket.ROCKET_TIER_FUEL_PRICE[3].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
        this.contentTable.add(new Label(Rocket.ROCKET_TIER_STATS[3].toString(), skin)).colspan(2).pad(5f);
        this.contentTable.row();
    }
}
