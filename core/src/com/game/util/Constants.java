package com.game.util;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Constants {

    //1 tile = 1 meter
    public static final float PIXEL_PER_METER = 70f;

    public static final float WORLD_GRAVITY = -10f;
    public static final float WORLD_TIMESTEP = 1 / 30f;

    //Camera
    public static final float CAMERA_WIDTH = 24;
    public static final float CAMERA_HEIGHT = CAMERA_WIDTH * 9 / 16;
    public static final float CAMERA_VERTICAL_OFFSET=3.5f;

    //Collision filter category bits
    public static final short CATEGORY_BIT_NOTHING = 0;
    public static final short CATEGORY_BIT_TERRAIN = 1;
    public static final short CATEGORY_BIT_PLAYER = 2;
    public static final short CATEGORY_BIT_COIN = 4;
    public static final short CATEGORY_BIT_MONSTER = 8;
    public static final short CATEGORY_BIT_DEATH_ZONE = 16;
}
