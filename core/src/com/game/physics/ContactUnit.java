package com.game.physics;

/**
 * Created by jonathan on 25.12.16.
 * This Class should always be used as fixture data
 */


public class ContactUnit {
    public static final int PLAYER = 1;
    public static final int PLAYER_FOOT = 2;
    public static final int COIN = 3;
    public static final int TERRAIN = 4;
    public static final int TERRAIN_ONE_WAY = 5;
    public static final int MONSTER = 6;
    public static final int DEATH_ZONE = 7;
    public static final int PLAYER_SIDE = 8;
    public static final int WALKER_SENSOR_L = 9;
    public static final int WALKER_SENSOR_R = 10;
    private int id;
    private Object data;

    /**
     * Creates a new ContactUnit to be used as fixture data
     *
     * @param id   this parameter is used to recognize the type of fixture involved in the contact
     * @param data this parameter should be the game object that this ContactUnit represents
     */
    public ContactUnit(int id, Object data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

}
