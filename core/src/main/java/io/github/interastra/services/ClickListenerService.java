package io.github.interastra.services;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ClickListenerService extends ClickListener {
    public final Sound buttonSound;

    public ClickListenerService(final Sound buttonSound) {
        super();
        this.buttonSound = buttonSound;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (buttonSound != null) {
            buttonSound.play(0.1f);
        }
        return super.touchDown(event, x, y, pointer, button);
    }
}
