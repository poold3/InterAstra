package io.github.interastra.tooltips;

import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;

public class InstantTooltipManager extends TooltipManager {
    public InstantTooltipManager() {
        super();
        this.instant();
        this.initialTime = 0f;
        this.subsequentTime = 0f;
    }
}
