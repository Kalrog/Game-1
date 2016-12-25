package com.game.character.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.game.util.Constants.PIXEL_PER_METER;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Player extends Actor {

    private static final float MAX_VELOCITY = 2f;
    private static final float MOVEMENT_IMPULSE = 0.3f;
    private static final float JUMP_IMPULSE = 500f;
    private Vector2 velocity;
    private boolean facesRight = true;
    private State state;
    private Animation<TextureAtlas.AtlasRegion> walkAnim;
    private Animation<TextureRegion> standAnim, jumpAnim;
    private float elapsedTime = 0;
    private BodyDef bodyDef;
    private Body body;
    private World world;

    public Player(Vector2 spawnPoint, World world) {
        this.world = world;
        velocity = new Vector2();
        TextureAtlas textureAtlasWalking = new TextureAtlas("player/player_walk.atlas");
        walkAnim = new Animation<TextureAtlas.AtlasRegion>(1 / 12f, textureAtlasWalking.getRegions());
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion textureRegionStanding = new TextureRegion(new Texture("player/p1_stand.png"));
        standAnim = new Animation<TextureRegion>(0, textureRegionStanding);

        TextureRegion textureRegionJumping = new TextureRegion(new Texture("player/p1_jump.png"));
        jumpAnim = new Animation<TextureRegion>(0, textureRegionJumping);

        setWidth(textureRegionStanding.getRegionWidth() / PIXEL_PER_METER);
        setHeight(textureRegionStanding.getRegionHeight() / PIXEL_PER_METER);
        createBody(spawnPoint);

        state = State.STANDING;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        handleInput();

        setPosition((body.getPosition().x - getWidth() / 2), body.getPosition().y - getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion frame = null;
        switch (state) {
            case STANDING:
                frame = standAnim.getKeyFrame(elapsedTime);
                break;
            case RUNNING:
                frame = walkAnim.getKeyFrame(elapsedTime, true);
                break;
            case JUMPING:
                frame = jumpAnim.getKeyFrame(elapsedTime);
                break;
        }
        if (facesRight) {
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(frame, getX() + getWidth(), getY(), -getWidth(), getHeight());
        }
    }

    private void handleInput() {
        //jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            state = State.JUMPING;
            body.applyForce(0, JUMP_IMPULSE, body.getLocalCenter().x, body.getLocalCenter().y, true);
        }

        //move left
        //apply linear impulse if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            state = State.RUNNING;
            facesRight = false;
            if (velocity.x > -MAX_VELOCITY) {
                body.applyLinearImpulse(new Vector2(-MOVEMENT_IMPULSE, 0), body.getWorldCenter(), true);
                //   body.applyLinearImpulse(-MOVEMENT_IMPULSE, 0, body.getPosition().x, body.getPosition().y, true);
            }
        }

        //move right
        //apply linear impulse if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            state = State.RUNNING;
            facesRight = true;
            if (velocity.x < MAX_VELOCITY) {
                body.applyLinearImpulse(MOVEMENT_IMPULSE, 0, body.getPosition().x, body.getPosition().y, true);
            }
        }
    }

    private void createBody(Vector2 position) {
        Gdx.app.log("test", "x: " + position.y);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(position.x / PIXEL_PER_METER, position.y / PIXEL_PER_METER);
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    private enum State {
        RUNNING, STANDING, JUMPING;
    }
}
