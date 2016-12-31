package com.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.physics.ContactUnit;
import com.game.util.Constants;

import static com.game.util.Constants.PIXEL_PER_METER;

/**
 * Created by Philipp on 24.12.2016.
 */
public class Coin extends Actor {

    private static final String TILED_ITEM_ID = "coin_gold";
    private static final float DESPAWN_TIME = 1f;
    private Texture texture;
    //  private Animation<Texture> animation;
    private State state;
    private float timeUntilDespawn = DESPAWN_TIME;

    public Coin(World world, Ellipse ellipse) {
        setPosition(ellipse.x, ellipse.y);
        state = State.IDLE;
        texture = new Texture("items/coinGold.png");
        setWidth(ellipse.width);
        setHeight(ellipse.height);
        // animation = new Animation<Texture>(0, texture);
        createBody(world, ellipse);
    }

    private void createBody(World world, Ellipse ellipse) {
        Gdx.app.log("test", ellipse.x + " " + ellipse.width);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((ellipse.x + ellipse.width / 2) / PIXEL_PER_METER, (ellipse.y + ellipse.height / 2) / PIXEL_PER_METER);
        Body body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(ellipse.width / PIXEL_PER_METER / 2);
        //   shape.setAsBox(ellipse.width / 2 / PIXEL_PER_METER, ellipse.height / 2 / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_COIN;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_PLAYER;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(new ContactUnit(ContactUnit.COIN, this));
        shape.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        switch (state) {
            case IDLE:

                break;
            case DESPAWNING:
                timeUntilDespawn -= delta;
                break;
        }
        if (timeUntilDespawn < 0) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        switch (state) {
            case IDLE:
                //Use tiled for drawing when IDLE
                break;
            case DESPAWNING:
                //Maybe some fancy animation
                break;
        }
        //  batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void die() {
        state = State.DESPAWNING;
    }


    enum State {
        IDLE, DESPAWNING
    }
}
