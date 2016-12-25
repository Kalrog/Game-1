package com.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Level {
    public static final String MAP_LAYER_SPAWN_POINTS = "spawnPoints";
    public static final String MAP_LAYER_TERRAIN = "terrain";
    public static final String MAP_LAYER_TERRAIN_BODIES = "terrain_bodies";

    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private MapLayers mapLayers;
    private MapLayer mapLayerSpawnPoints;
    private MapLayer mapLayerTerrainBodies;
    private int tileWidth;
    private int tileHeight;
    private int mapHeight;
    private int mapWidth;

    public Level(World world) {
        tiledMap = new TmxMapLoader().load("level/level1.tmx");
        mapLayers = tiledMap.getLayers();
        mapLayerSpawnPoints = mapLayers.get(MAP_LAYER_SPAWN_POINTS);
        mapLayerTerrainBodies = mapLayers.get(MAP_LAYER_TERRAIN_BODIES);
        tileWidth = ((Integer) tiledMap.getProperties().get("tilewidth"));
        tileHeight = ((Integer) tiledMap.getProperties().get("tileheight"));
        mapWidth = ((Integer) tiledMap.getProperties().get("width"));
        mapHeight = ((Integer) tiledMap.getProperties().get("height"));
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        for (MapObject mapObject : mapLayerTerrainBodies.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            Gdx.app.log("test", "object");
            FixtureDef fixtureDef = new FixtureDef();
            Gdx.app.log("test", "object");
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;

            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setFriction(0f);
            shape.dispose();
        }
    }


    public Vector2 getPlayerSpawn() {
        Rectangle rectangle = ((RectangleMapObject) mapLayerSpawnPoints.getObjects().get("playerSpawn")).getRectangle();
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
