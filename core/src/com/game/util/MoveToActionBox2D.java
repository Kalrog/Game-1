package com.game.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by jonathan on 28.12.16.
 */
public class MoveToActionBox2D extends Action {
    private Body body;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Vector2 velocity;
    private float speed = 1f;

    public MoveToActionBox2D(Body body) {
        this.body = body;
        if (body.getType() == BodyDef.BodyType.StaticBody)
            throw new IllegalArgumentException();
        calculateVelocity();
    }

    @Override
    public boolean act(float delta) {

        if (Math.abs(body.getPosition().x - endX) < 0.05 * speed && Math.abs(body.getPosition().y - endY) < 0.05 * speed ) {
            body.setLinearVelocity(0,0);
            //body.setTransform(endX, endY, body.getAngle());
            return true;
        }

        calculateVelocity();
        body.setLinearVelocity(velocity);

        return false;
    }

    @Override
    public void restart() {
        super.restart();
        body.setTransform(startX, startY, body.getAngle());
    }

    public void setPosition(float x, float y) {
        endX = x;
        endY = y;
        calculateVelocity();
    }

    public float getX() {
        return endX;
    }

    public void setX(float x) {
        endX = x;
        calculateVelocity();
    }

    public float getY() {
        return endY;
    }

    public void setY(float y) {
        endY = y;
        calculateVelocity();
    }

    public void setSpeed(float s) {
        speed = s;
        calculateVelocity();
    }

    private void calculateVelocity() {
        startX = body.getPosition().x;
        startY = body.getPosition().y;
        velocity = new Vector2(endX - startX, endY - startY);
        velocity.setLength(speed);
    }
}
