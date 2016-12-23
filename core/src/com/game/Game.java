package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.character.player.Player;
import com.game.level.Level;

public class Game extends ApplicationAdapter {
    private static final int CAMERA_WIDTH = 30;
    private static final int CAMERA_HEIGHT = 20;
    private GameState gameState;
    private Stage stage;
    private Level level;
    private OrthographicCamera camera;
    private Player player;
    private World world;

    @Override
    public void create() {
        world = new World(new Vector2(0, -98f), true);
        gameState = GameState.RUNNING;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.update();
        Viewport viewport = new FillViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
        level = new Level(world);
        player = new Player(level.getPlayerSpawn(), world);
        stage = new Stage(viewport, level.getBatch());
        stage.addActor(player);
        //player.setPosition(8,4);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        if (Gdx.graphics.getBufferFormat().coverageSampling)
            Gdx.gl.glClear(GL20.GL_COVERAGE_BUFFER_BIT_NV);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (player.getX() < CAMERA_WIDTH / 2) {
            camera.position.x = CAMERA_WIDTH / 2;
        } else {
            camera.position.x = player.getX();
        }
        camera.position.y = player.getY() + CAMERA_HEIGHT * 0.1f;
        camera.update();
        level.setView(camera);
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

    private enum GameState {
        RUNNING, PAUSED
    }
}
