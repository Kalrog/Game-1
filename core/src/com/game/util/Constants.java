package com.game.util;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Constants {

    public static final float PIXEL_PER_METER = 50f;
    public static final float WORLD_GRAVITY = -10f;

    public static final float CAMERA_WIDTH = 30;
    public static final float CAMERA_HEIGHT = CAMERA_WIDTH * 9 / 16;


    //Collision filter category bits
    public static final short CATEGORY_BIT_NOTHING = 0;
    public static final short CATEGORY_BIT_TERRAIN = 1;
    public static final short CATEGORY_BIT_PLAYER = 2;
    public static final short CATEGORY_BIT_COIN = 4;
    public static final short CATEGORY_BIT_PLAYER_FOOT = 8;
}
