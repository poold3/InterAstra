package io.github.interastra.services;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NotificationService {
    public static final float LOADING_TICK = .5f;
    public static final int MAX_NUMBER_OF_PERIODS = 3;

    private String message;
    private float duration;
    private boolean isLoading;
    private float loadingTimer;
    private int currentNumberOfPeriods;
    private String loadingMessage;
    private BitmapFont font;

    public NotificationService(final BitmapFont font) {
        this.message = "";
        this.duration = 0f;
        this.isLoading = false;
        this.loadingTimer = 0f;
        this.currentNumberOfPeriods = 0;
        this.loadingMessage = "";
        this.font = font;
    }

    public void dispose() {
        this.font.dispose();
    }

    public void setMessage(final String message) {
        this.message = message;
        this.duration = 10f;
        this.stopLoading();
    }

    public void setMessage(final String message, float duration) {
        this.message = message;
        this.duration = duration;
        this.stopLoading();
    }

    public void startLoading(final String message) {
        this.message = message;
        this.isLoading = true;
        this.currentNumberOfPeriods = 1;
        this.loadingTimer = 0f;
        this.loadingMessage = this.message + " .";
    }

    public void stopLoading() {
        this.isLoading = false;
    }

    public void drawMessage(final SpriteBatch spriteBatch, float delta, float x, float y) {
        if (this.message.isBlank()) {
            return;
        }

        if (!this.isLoading) {
            this.duration -= delta;
            if (this.duration <= 0f) {
                this.message = "";
                return;
            }
            this.font.draw(spriteBatch, this.message, x, y);
            return;
        }

        this.loadingTimer += delta;

        if (this.loadingTimer >= LOADING_TICK) {
            this.loadingTimer -= LOADING_TICK;
            this.currentNumberOfPeriods += 1;
            if (this.currentNumberOfPeriods > MAX_NUMBER_OF_PERIODS) {
                this.currentNumberOfPeriods = 1;
                this.loadingMessage = this.message + " .";
            } else {
                this.loadingMessage += " .";
            }
        }

        this.font.draw(spriteBatch, this.loadingMessage, x, y);
    }
}
