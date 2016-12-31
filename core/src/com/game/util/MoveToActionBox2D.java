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
    private boolean began = false;

    public MoveToActionBox2D(Body body) {
        this.body = body;
        if (body.getType() == BodyDef.BodyType.StaticBody)
            throw new IllegalArgumentException();
        startX = body.getPosition().x;
        startY = body.getPosition().y;
    }

    @Override
    public boolean act(float delta) {
        if (!began) {
            calculateVelocity();
            body.setLinearVelocity(velocity);
            began = true;
        }

        // This Action is done if the body will reach it's destination within one World Timestep or less
        // this needs to be optimized because Math.hypot uses the slow squareroot function
        //TODO
        if (Math.hypot(endX - body.getPosition().x, endY - body.getPosition().y) <= speed * Constants.WORLD_TIMESTEP) {
            //body.setLinearVelocity(0,0);
            //body.setTransform(endX, endY, body.getAngle());
            return true;
        }

        return false;
    }

    @Override
    public void restart() {
        super.restart();
        // adding this back into the code will make the platform reset to it's original position after it's done
        // by teleporting instead of moving
        //body.setTransform(startX, startY, body.getAngle());
        began = false;
    }

    public void setPosition(float x, float y) {
        endX = x;
        endY = y;
    }

    public float getX() {
        return endX;
    }

    public void setX(float x) {
        endX = x;
    }

    public float getY() {
        return endY;
    }

    public void setY(float y) {
        endY = y;
    }

    public void setSpeed(float s) {
        speed = s;
    }

    private void calculateVelocity() {
        velocity = new Vector2(endX - body.getPosition().x, endY - body.getPosition().y);
        // this can probably be optimized too
        velocity.setLength(speed);
    }
}
