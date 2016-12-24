package com.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.util.Constants;

/**
 * Created by Philipp on 24.12.2016.
 */
public class Coin extends Actor {

    private static final String TILED_ITEM_ID = "coin_gold";

    private Texture texture;
    private Animation<Texture> animation;

    public Coin(float x, float y) {
        setPosition(x, y);

        texture = new Texture("assets/items/coinGold.png");
        setWidth(texture.getWidth() * Constants.WORLD_SCALE);
        setHeight(texture.getHeight() * Constants.WORLD_SCALE);
        animation = new Animation<Texture>(0, texture);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
