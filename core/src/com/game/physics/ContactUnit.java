package com.game.physics;

/**
 * Created by jonathan on 25.12.16.
 */


public class ContactUnit {
    public static final int PLAYER = 1;
    public static final int PLAYER_FOOT = 2;
    public static final int COIN = 4;
    public static final int TERRAIN = 8;
    public static final int ONE_WAY = 16;

    private int id;
    private Object data;

    public ContactUnit(int id,Object data){
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
