package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.level.Level;

public class Game extends ApplicationAdapter {
    private static final int CAMERA_WIDTH = 30;
    private static final int CAMERA_HEIGHT = 20;
    private GameState gameState;
    private Stage stage;
    private Level level;
    private OrthographicCamera camera;

    private enum GameState {
        RUNNING, PAUSED
    }


    @Override
    public void create() {
        gameState = GameState.RUNNING;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.update();
        Viewport viewport = new FillViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
        level = new Level();
        stage = new Stage(viewport, level.getBatch());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        camera.update();
        switch (gameState) {
            case RUNNING:
                stage.act();

                stage.act();


                break;
            case PAUSED:

                break;

        }
        stage.draw();

    }

    @Override
    public void dispose() {
    }

    @Override
    public void resume() {
        super.resume();
        gameState = GameState.RUNNING;
    }

    @Override
    public void pause() {
        super.pause();
        gameState = GameState.PAUSED;
    }
}
