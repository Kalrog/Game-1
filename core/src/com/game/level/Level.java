package com.game.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.items.Coin;
import com.game.util.Constants;

import static com.game.util.Constants.PIXEL_PER_METER;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Level {
    public static final String MAP_LAYER_SPAWN_POINTS = "spawnPoints";
    public static final String MAP_LAYER_TERRAIN = "terrain";
    public static final String MAP_LAYER_TERRAIN_BODIES = "terrain_bodies";
    public static final String MAP_LAYER_COINS = "coins";
    World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private MapLayers mapLayers;
    private int tileWidth;
    private int tileHeight;
    private int mapHeight;
    private int mapWidth;
    private Stage stage;

    public Level(World world) {
        this.world = world;
        tiledMap = new TmxMapLoader().load("level/level1.tmx");
        mapLayers = tiledMap.getLayers();
        tileWidth = ((Integer) tiledMap.getProperties().get("tilewidth"));
        tileHeight = ((Integer) tiledMap.getProperties().get("tileheight"));
        mapWidth = ((Integer) tiledMap.getProperties().get("width"));
        mapHeight = ((Integer) tiledMap.getProperties().get("height"));
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PIXEL_PER_METER);
        createTerrainBodies();
    }

    private void createTerrainBodies() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        for (MapObject mapObject : mapLayers.get(MAP_LAYER_TERRAIN_BODIES).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PIXEL_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / PIXEL_PER_METER);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() / 2 / PIXEL_PER_METER, rectangle.getHeight() / 2 / PIXEL_PER_METER);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.friction = 0.2f;
            fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_TERRAIN;
            fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_PLAYER;

            Fixture fixture = body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    public void createCoins(Stage stage) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        for (MapObject mapObject : mapLayers.get(MAP_LAYER_COINS).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PIXEL_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / PIXEL_PER_METER);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() / 2 / PIXEL_PER_METER, rectangle.getHeight() / 2 / PIXEL_PER_METER);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_COIN;
            Fixture fixture = body.createFixture(fixtureDef);
            shape.dispose();
            stage.addActor(new Coin(body.getPosition().x - rectangle.getWidth() / 2 / PIXEL_PER_METER, body.getPosition().y - rectangle.getHeight() / 2 / PIXEL_PER_METER, rectangle.getWidth() / PIXEL_PER_METER, rectangle.getHeight() / PIXEL_PER_METER));
        }
    }

    public Vector2 getPlayerSpawn() {
        Rectangle rectangle = ((RectangleMapObject) mapLayers.get(MAP_LAYER_SPAWN_POINTS).getObjects().get("playerSpawn")).getRectangle();
        Vector2 center = new Vector2();
        center = rectangle.getCenter(center);
        return center;
    }

    public Batch getBatch() {
        return mapRenderer.getBatch();
    }

    public void setView(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }


}
