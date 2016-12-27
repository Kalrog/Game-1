package com.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {

    private SpriteBatch batch;


    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
