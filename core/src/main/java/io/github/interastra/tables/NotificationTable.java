package io.github.interastra.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.concurrent.locks.ReentrantLock;

public class NotificationTable extends Table {
    public static final float LOADING_TICK_LENGTH = .5f;
    public static final int MAX_NUMBER_OF_PERIODS = 3;
    public static final String LOADING_TICK_STRING = " .";

    private final StringBuilder message;
    private final Label messageLabel;
    private float duration;
    private boolean isLoading;
    private float loadingTimer;
    private int currentNumberOfPeriods;
    private final ReentrantLock lock;

    public NotificationTable(final Skin skin, final ScreenViewport stageViewport) {
        super();
        this.setFillParent(true);

        this.message = new StringBuilder();
        this.messageLabel = new Label(this.message, skin);
        this.messageLabel.setWrap(true);
        this.add(this.messageLabel).width(stageViewport.getWorldWidth() / 4f).growX().expandY().top().left().pad(5f);

        this.duration = 0f;
        this.isLoading = false;
        this.loadingTimer = 0f;
        this.currentNumberOfPeriods = 0;
        this.lock = new ReentrantLock(true);
    }

    public void setMessage(final String message) {
        this.setMessage(message, 10f);
    }

    public void setMessage(final String message, float duration) {
        this.lock.lock();
        try {
            this.isLoading = false;
            this.message.clear();
            this.message.append(message);
            this.messageLabel.setText(this.message);
            this.duration = duration;
        } finally {
            this.lock.unlock();
        }
    }

    public void startLoading(final String message) {
        this.lock.lock();
        try {
            this.message.clear();
            this.message.append(message);
            this.messageLabel.setText(this.message);
            this.isLoading = true;
            this.currentNumberOfPeriods = 0;
            this.loadingTimer = 0f;
        } finally {
            this.lock.unlock();
        }
    }

    public void clearNotification() {
        this.lock.lock();
        try {
            this.isLoading = false;
            this.message.clear();
            this.messageLabel.setText(this.message);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.lock.lock();
        try {
            if (!this.isLoading && this.duration >= 0f) {
                this.duration -= delta;
                if (this.duration <= 0f) {
                    this.message.clear();
                    this.messageLabel.setText(this.message);
                }
            }

            if (this.isLoading) {
                this.loadingTimer += delta;
                if (this.loadingTimer >= LOADING_TICK_LENGTH) {
                    this.loadingTimer -= LOADING_TICK_LENGTH;
                    this.currentNumberOfPeriods += 1;
                    if (this.currentNumberOfPeriods > MAX_NUMBER_OF_PERIODS) {
                        int length = this.message.length();
                        this.message.delete(length - (MAX_NUMBER_OF_PERIODS * LOADING_TICK_STRING.length()), length);
                        this.currentNumberOfPeriods = 0;
                        this.messageLabel.setText(this.message);
                    } else {
                        this.message.append(LOADING_TICK_STRING);
                        this.messageLabel.setText(this.message);
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
    }
}
