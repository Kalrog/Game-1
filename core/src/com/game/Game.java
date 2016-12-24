package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import static com.game.util.Constants.*;

public class Game extends ApplicationAdapter {

    private GameState gameState;
    private Stage stage;
    private Level level;
    private OrthographicCamera camera;
    private Player player;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        world = new World(new Vector2(0, WORLD_GRAVITY), true);
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
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
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
        debugRenderer.render(world, camera.projection.scl(WORLD_SCALE,WORLD_SCALE,1f).translate(-15 * 70 , 0 ,0));

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
