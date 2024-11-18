package io.github.interastra.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

public class ClickListenerService extends ClickListener {
    public final Sound clickSound;
    public final Cursor.SystemCursor cursor;

    public ClickListenerService(final Sound clickSound, final Cursor.SystemCursor cursor) {
        super();
        this.clickSound = clickSound;
        this.cursor = cursor;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (clickSound != null) {
            clickSound.play(0.1f);
        }
        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        if (cursor != null) {
            Gdx.graphics.setSystemCursor(this.cursor);
        }
    }

    @Override
    public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        if (pointer == -1 && cursor != null) {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }
    }
}
