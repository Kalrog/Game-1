package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Game;
import com.game.character.player.Player;
import com.game.level.Level;
import com.game.physics.ContactHandler;
import com.game.util.Constants;

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
    private Game game;
    private FPSLogger fpsLogger;
    private GameState gameState;
    private Hud hud;

    public GameScreen(Game game) {
        hud = new Hud(game.getBatch());
        this.game = game;
        gameState = GameState.RUNNING;
        fpsLogger = new FPSLogger();
        world = new World(new Vector2(0, WORLD_GRAVITY), true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.update();
        Viewport viewport = new FillViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
        stage = new Stage(viewport, game.getBatch());
        level = new Level(world, stage);
        player = new Player(level.getPlayerSpawn(), world, game);
        stage.addActor(player);
        world.setContactListener(ContactHandler.getInstance());
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (gameState == GameState.RUNNING) {
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            world.step(Constants.WORLD_TIMESTEP, 6, 2);
            world.clearForces();
            if (Gdx.graphics.getBufferFormat().coverageSampling) {
                Gdx.gl.glClear(GL20.GL_COVERAGE_BUFFER_BIT_NV);
            }
            if (player.getX() < CAMERA_WIDTH / 2) {
                camera.position.x = CAMERA_WIDTH / 2;
            } else {
                camera.position.x = player.getX();
            }
            if (player.getY() < CAMERA_HEIGHT - ((CAMERA_HEIGHT / 2) + CAMERA_VERTICAL_OFFSET)) {
                camera.position.y = CAMERA_HEIGHT / 2;
            } else {
                camera.position.y = player.getY() + Constants.CAMERA_VERTICAL_OFFSET;
            }
            if (player.getY() > level.getMapHeight() - CAMERA_HEIGHT / 2 - CAMERA_VERTICAL_OFFSET) {
                camera.position.y = level.getMapHeight() - CAMERA_HEIGHT / 2;
            }
            camera.update();
            level.setView(camera);
            debugRenderer.render(world, camera.combined);
            stage.act();
            stage.draw();

            //render hud
            game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
            hud.getStage().draw();
            //   fpsLogger.log();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        gameState = GameState.PAUSED;
    }

    @Override
    public void resume() {
        gameState = GameState.RUNNING;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        stage.dispose();
    }

    enum GameState {
        RUNNING, PAUSED
    }
}
