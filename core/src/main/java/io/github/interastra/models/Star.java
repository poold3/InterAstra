package io.github.interastra.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.interastra.message.models.StarMessageModel;

public class Star implements CameraEnabledEntity {
    public int index;
    public String name;
    public Sprite starSprite;

    public Star(final TextureAtlas textureAtlas, StarMessageModel starMessageModel) {
        this.index = starMessageModel.index();
        this.name = starMessageModel.name();
        this.starSprite = new Sprite(textureAtlas.findRegion("star", this.index));
        float size = starMessageModel.size();
        this.starSprite.setSize(size, size);
        this.starSprite.setOrigin(size / 2f, size / 2f);
    }

    public void reposition(float x, float y) {
        this.starSprite.setCenter(x, y);
    }

    @Override
    public float getX() {
        return this.starSprite.getX() + this.starSprite.getOriginX();
    }

    @Override
    public float getY() {
        return this.starSprite.getY() + this.starSprite.getOriginY();
    }

    @Override
    public float getWidth() {
        return this.starSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return this.starSprite.getHeight();
    }
}
