package com.game.physics;

/**
 * Created by jonathan on 25.12.16.
 * This Class should always be used as fixture data
 */


public class ContactUnit {
    public static final int PLAYER = 1;
    public static final int PLAYER_FOOT = 2;
    public static final int COIN = 4;
    public static final int TERRAIN = 8;
    public static final int ONE_WAY = 16;
    public static final int DEATH_ZONE = 32;
    public static final int PLAYER_SIDE = 64;

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
