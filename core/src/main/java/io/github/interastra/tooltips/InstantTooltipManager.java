package io.github.interastra.tooltips;

import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;

public class InstantTooltipManager extends TooltipManager {
    public InstantTooltipManager() {
        super();
        this.instant();
        this.initialTime = 0f;
        this.subsequentTime = 0f;
    }

    public InstantTooltipManager(final float offsetX, final float offsetY) {
        super();
        this.instant();
        this.initialTime = 0f;
        this.subsequentTime = 0f;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
