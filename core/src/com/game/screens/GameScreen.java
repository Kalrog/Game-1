package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.character.player.Player;
import com.game.level.Level;
import com.game.physics.ContactHandler;

import static com.game.util.Constants.*;

/**
 * Created by Philipp on 25.12.2016.
 */
public class GameScreen implements Screen {
    private World world;
    private OrthographicCamera camera;
    private Level level;
    private Player player;
    private Stage stage;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen() {
        world = new World(new Vector2(0, WORLD_GRAVITY), true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.update();
        Viewport viewport = new FillViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
        level = new Level(world);
        player = new Player(level.getPlayerSpawn(), world);
        stage = new Stage(viewport, level.getBatch());
        level.createCoins(stage);
        stage.addActor(player);
        world.setContactListener(ContactHandler.getInstance());
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 30f, 6, 2);
        world.clearForces();

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
        debugRenderer.render(world, camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
