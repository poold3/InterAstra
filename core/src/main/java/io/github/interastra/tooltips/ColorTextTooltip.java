package io.github.interastra.tooltips;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.interastra.labels.ColorLabel;

public class ColorTextTooltip extends Tooltip<Container<Label>> {
    public ColorTextTooltip(final String text, final TooltipManager manager, final Skin skin, final Color color) {
        super(new Container<>(new ColorLabel(text, skin, color)), manager);

        Container<Label> container = this.getActor();
        container.setBackground(skin.getDrawable("panel_glass"));
        container.pad(10f);
    }
}
