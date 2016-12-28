package com.game.character.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.physics.ContactUnit;
import com.game.util.Constants;

import static com.game.util.Constants.CATEGORY_BIT_MONSTER;
import static com.game.util.Constants.PIXEL_PER_METER;

/**
 * Created by jonathan on 26.12.16.
 */
public class Walker extends Actor {
    private static final float MAX_VELOCITY = 2f;
    private static final float MOVEMENT_IMPULSE = 0.3f;
    private static final float DESPAWN_TIME = 1f;
    private Vector2 velocity;
    private boolean facesRight = false;
    private World world;
    private Body body;
    private Animation<TextureRegion> walkAnim, deadAnim;
    private State state;
    private float elapsedTime = 0;
    private float timeUntilDespawn = DESPAWN_TIME;

    public Walker(Vector2 spawnPoint, World world) {
        this.world = world;
        velocity = new Vector2();

        TextureRegion[] walkingFrames = new TextureRegion[2];
        walkingFrames[0] = new TextureRegion(new Texture("monsters/slime/slimeWalk1.png"));
        walkingFrames[1] = new TextureRegion(new Texture("monsters/slime/slimeWalk2.png"));
        walkAnim = new Animation<TextureRegion>(1 / 3f, walkingFrames);
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion textureRegionDead = new TextureRegion(new Texture("monsters/slime/slimeDead.png"));
        deadAnim = new Animation<TextureRegion>(0, textureRegionDead);

        setWidth(walkingFrames[0].getRegionWidth() / PIXEL_PER_METER);
        setHeight(walkingFrames[0].getRegionHeight() / PIXEL_PER_METER);

        createBody(spawnPoint);

        state = State.WALKING;
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        setPosition((body.getPosition().x - getWidth() / 2), body.getPosition().y - getHeight() / 2);
        velocity = body.getLinearVelocity();

        if(Math.abs(velocity.x) < MAX_VELOCITY && state == State.WALKING){
            if(facesRight){
                body.applyLinearImpulse(new Vector2(MOVEMENT_IMPULSE, 0),body.getWorldCenter(), true);
            }else {
                body.applyLinearImpulse(new Vector2(-MOVEMENT_IMPULSE, 0),body.getWorldCenter(), true);
            }
        }

        if(state == State.DEAD){
            timeUntilDespawn -= delta;
        }

        if(timeUntilDespawn < 0){
            this.remove();
            world.destroyBody(body);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion frame = null;
        switch (state) {
            case WALKING:
                frame = walkAnim.getKeyFrame(elapsedTime, true);
                break;
            case DEAD:
                frame = deadAnim.getKeyFrame(elapsedTime);
                break;
        }

        if (facesRight) {
            batch.draw(frame, getX() + frame.getRegionWidth() / PIXEL_PER_METER, getY(), -frame.getRegionWidth() / PIXEL_PER_METER, frame.getRegionHeight() / PIXEL_PER_METER);
        } else {
            batch.draw(frame, getX(), getY(), frame.getRegionWidth() / PIXEL_PER_METER, frame.getRegionHeight() / PIXEL_PER_METER);
        }
    }

    private void createBody(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / PIXEL_PER_METER, position.y / PIXEL_PER_METER);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        //Main Walker Body
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_MONSTER;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_TERRAIN | Constants.CATEGORY_BIT_PLAYER | Constants.CATEGORY_BIT_MONSTER;
        body.createFixture(fixtureDef).setUserData(new ContactUnit(ContactUnit.MONSTER, this));

        //Sensor left
        shape.setAsBox(getWidth() / 2 * 0.1f, getHeight() / 2 * 0.1f, new Vector2(-getWidth() / 2, -getHeight() / 2), 0);
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_MONSTER;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_TERRAIN | Constants.CATEGORY_BIT_PLAYER | Constants.CATEGORY_BIT_MONSTER;
        body.createFixture(fixtureDef).setUserData(new ContactUnit(ContactUnit.WALKER_SENSOR_L, this));
        //Sensor right
        shape.setAsBox(getWidth() / 2 * 0.1f, getHeight() / 2 * 0.1f, new Vector2(getWidth() / 2, -getHeight() / 2), 0);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(new ContactUnit(ContactUnit.WALKER_SENSOR_R, this));
        shape.dispose();


    }

    public void turnAround(){
        velocity.x *= -1;
        facesRight = !facesRight;
        body.setLinearVelocity(velocity);
    }

    public void die(){
        state = State.DEAD;
    }

    public boolean isFacingRight(){
        return facesRight;
    }

    private enum State {
        WALKING, DEAD;
    }
}
