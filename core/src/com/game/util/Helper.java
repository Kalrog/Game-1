package com.game.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Philipp on 28.12.2016.
 */
public class Helper {

    public static Vector2[] floatArrayToVector2Array(float[] vertices) {
        Vector2[] v = new Vector2[vertices.length / 2];
        for (int i = 0; i < vertices.length; i += 2) {
            v[i / 2] = new Vector2();
            v[i / 2].x = vertices[i];
        }
        for (int i = 1; i < vertices.length; i += 2) {
            v[(i - 1) / 2].y = vertices[i];
        }
        return v;
    }
}
