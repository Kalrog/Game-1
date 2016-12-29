package com.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.util.Constants;

/**
 * Created by Philipp on 25.12.2016.
 */
public class GameOverScreen {

    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    public GameOverScreen() {
        batch = new SpriteBatch();
        viewport = new FillViewport(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        stage = new Stage(viewport, batch);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label labelGameOver = new Label("Game Over", labelStyle);
        labelGameOver.setSize(100, 100);

        stage.addActor(labelGameOver);
    }

    public void update(float delta) {
        batch.begin();
        stage.draw();
        batch.end();

    }


}
