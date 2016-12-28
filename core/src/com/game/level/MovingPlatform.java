package com.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.game.physics.ContactUnit;
import com.game.util.Constants;
import com.game.util.Helper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.game.util.Constants.PIXEL_PER_METER;

/**
 * Created by Philipp on 28.12.2016.
 */
public class MovingPlatform extends Actor {

    private int width;
    private Texture texture;
    private Body body;
    private Vector2[] vertices;

    public MovingPlatform(World world, float[] vertices, int width) {
        this.vertices = Helper.floatArrayToVector2Array(vertices);
        this.width = width;
        setPosition(vertices[0] / PIXEL_PER_METER, vertices[1] / PIXEL_PER_METER);

        texture = new Texture("level/grassMid.png");
        setSize(texture.getWidth() / PIXEL_PER_METER, texture.getHeight() / PIXEL_PER_METER);

        MoveToAction[] actions = new MoveToAction[this.vertices.length];
        for (int i = 0; i < this.vertices.length; i++) {
            actions[i] = new MoveToAction();
            actions[i].setPosition(this.vertices[i].x / PIXEL_PER_METER, this.vertices[i].y / PIXEL_PER_METER);
            actions[i].setDuration(1f);
        }
        ArrayList<MoveToAction> allActions = new ArrayList<MoveToAction>();
        allActions.addAll(Arrays.asList(actions));
        body = createBody(world);
        //TODO add actions in reverse order to allActions
       // Gdx.app.log("test", Arrays.toString(allActions.toArray()));
        addAction(Actions.forever(Actions.sequence(allActions.toArray(new MoveToAction[allActions.size()]))));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        //Gdx.app.log("floatArrayToVector2Array", getX() + " " + getY() + " " + getWidth() + " " + getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        body.setTransform(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
    }

    private Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((getX() + getWidth() / 2), (getY() + getHeight() / 2));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_TERRAIN;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_PLAYER;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(new ContactUnit(ContactUnit.TERRAIN, this));
        shape.dispose();
        return body;
    }
}
