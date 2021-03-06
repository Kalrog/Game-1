package com.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.character.monsters.Walker;
import com.game.items.Coin;
import com.game.physics.ContactUnit;
import com.game.util.Constants;

import static com.game.util.Constants.PIXEL_PER_METER;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Level {
    public static final String MAP_LAYER_MONSTER_SPAWN_POINTS = "monster spawn points";
    public static final String MAP_LAYER_SPAWN_POINTS = "spawn points";
    public static final String MAP_LAYER_TERRAIN = "terrain";
    public static final String MAP_LAYER_TERRAIN_BODIES = "terrain bodies";
    public static final String MAP_LAYER_COINS = "coins";
    public static final String MAP_LAYER_DEATH_ZONE = "death zone";
    public static final String MAP_LAYER_MOVING_PLATFORMS = "moving platforms";
    public static final String MAP_PROPERTY_ONE_WAY_PLATFORM = "one-way-platform";
    public static final String MAP_PROPERTY_MONSTER_TYPE = "monster type";
    public static final String MONSTER_TYPE_WALKER = "walker";
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private MapLayers mapLayers;
    private int tileWidth;
    private int tileHeight;
    private int mapHeight;
    private int mapWidth;

    public Level(World world, Stage stage) {
        tiledMap = new TmxMapLoader().load("level/level1.tmx");
        mapLayers = tiledMap.getLayers();
        tileWidth = ((Integer) tiledMap.getProperties().get("tilewidth"));
        tileHeight = ((Integer) tiledMap.getProperties().get("tileheight"));
        mapWidth = ((Integer) tiledMap.getProperties().get("width"));
        mapHeight = ((Integer) tiledMap.getProperties().get("height"));
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PIXEL_PER_METER);
        createTerrainBodies(world);
        createDeathZones(world);
        createCoins(world, stage);
        createMovingPlatforms(world, stage);
        spawnMonsters(world, stage);
    }

    private void createTerrainBodies(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        //create rectangle bodies
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
            fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_PLAYER | Constants.CATEGORY_BIT_MONSTER;
            Fixture fixture = body.createFixture(fixtureDef);
            boolean oneWay = false;
            try {
                oneWay = mapObject.getProperties().get(MAP_PROPERTY_ONE_WAY_PLATFORM, boolean.class);
            } catch (Exception e) {
            }
            if (oneWay) {
                fixture.setUserData(new ContactUnit(ContactUnit.TERRAIN_ONE_WAY, this));
            } else {
                fixture.setUserData(new ContactUnit(ContactUnit.TERRAIN, this));
            }
            shape.dispose();
        }

        //create polygon bodies
        for (MapObject mapObject : mapLayers.get(MAP_LAYER_TERRAIN_BODIES).getObjects().getByType(PolygonMapObject.class)) {
            Polygon poly = ((PolygonMapObject) mapObject).getPolygon();
            bodyDef.position.set(poly.getX() / PIXEL_PER_METER, poly.getY() / PIXEL_PER_METER);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            float[] vertices = poly.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] /= PIXEL_PER_METER;
            }
            shape.set(vertices);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.friction = 0.2f;
            fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_TERRAIN;
            fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_PLAYER | Constants.CATEGORY_BIT_MONSTER;
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(new ContactUnit(ContactUnit.TERRAIN, this));
            shape.dispose();
        }
    }

    private void createCoins(World world, Stage stage) {
        Gdx.app.log("test", "create coins");

        for (MapObject mapObject : mapLayers.get(MAP_LAYER_COINS).getObjects().getByType(EllipseMapObject.class)) {
            Gdx.app.log("test", "for");
            Ellipse ellipse = ((EllipseMapObject) mapObject).getEllipse();
           stage.addActor(new Coin(world, ellipse));
        }
    }

    private void createDeathZones(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        //create rectangle bodies
        for (MapObject mapObject : mapLayers.get(MAP_LAYER_DEATH_ZONE).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PIXEL_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / PIXEL_PER_METER);
            Body body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() / 2 / PIXEL_PER_METER, rectangle.getHeight() / 2 / PIXEL_PER_METER);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.friction = 0.2f;
            fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_DEATH_ZONE;
            fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_PLAYER;
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(new ContactUnit(ContactUnit.DEATH_ZONE, this));
            shape.dispose();
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

    private void createMovingPlatforms(World world, Stage stage) {
        for (MapObject mapObject : mapLayers.get(MAP_LAYER_MOVING_PLATFORMS).getObjects().getByType(PolylineMapObject.class)) {
            Polyline poly = ((PolylineMapObject) mapObject).getPolyline();
            stage.addActor(new MovingPlatform(world, poly.getTransformedVertices(), 1));
        }
    }

    private void spawnMonsters(World world, Stage stage) {
        for (MapObject mapObject : mapLayers.get(MAP_LAYER_MONSTER_SPAWN_POINTS).getObjects().getByType(RectangleMapObject.class)) {
            String type = mapObject.getProperties().get(MAP_PROPERTY_MONSTER_TYPE, String.class);
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            Vector2 center = new Vector2();
            center = rectangle.getCenter(center);
            if (type.equals(MONSTER_TYPE_WALKER)) {
                Walker walker = new Walker(center, world);
                stage.addActor(walker);
            }
        }
    }

    public int getMapHeight() {
        return mapHeight;
    }
}
