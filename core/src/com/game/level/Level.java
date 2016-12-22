package com.game.level;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.game.util.Constants;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Level {

    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;

    public Level() {
        tiledMap = new TmxMapLoader().load("level/test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.WORLD_SCALE);
    }

    public Batch getBatch() {
        return mapRenderer.getBatch();
    }


}
