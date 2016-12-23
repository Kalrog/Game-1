package com.game.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.game.util.Constants;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Level {
    public static final String MAP_LAYER_SPAWN_POINTS = "spawnPoints";

    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private MapLayers mapLayers;
    private MapLayer mapLayerSpawnPoints;


    public Level() {
        tiledMap = new TmxMapLoader().load("level/level1.tmx");
        mapLayers = tiledMap.getLayers();
        mapLayerSpawnPoints = mapLayers.get(MAP_LAYER_SPAWN_POINTS);

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.WORLD_SCALE);
    }

    public Vector2 getPlayerSpawn() {
        Rectangle rectangle = ((RectangleMapObject) mapLayerSpawnPoints.getObjects().get("playerSpawn")).getRectangle();
        return new Vector2(rectangle.getCenter(new Vector2()));
    }

    public Batch getBatch() {
        return mapRenderer.getBatch();
    }

    public void setView(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }


}
