package io.github.interastra.labels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ColorLabel extends Label {
    public ColorLabel(final String text, final Skin skin, final Color color) {
        super(text, skin);
        this.setColor(color);
    }

    public ColorLabel(final String text, final LabelStyle style, final Color color) {
        super(text, style);
        this.setColor(color);
    }
}
