package com.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by brentaureli on 8/17/15.
 */
public class Hud implements Disposable {

    private Stage stage;
    private Viewport viewport;

    private Label testLabel;
    private Label.LabelStyle labelStyle;

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(400, 200, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        testLabel = new Label("Hey Joni, ein Hud!", labelStyle);
        table.add(testLabel).expandX().padTop(10);
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
