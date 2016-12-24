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
import com.game.util.Constants;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Player extends Actor {

    private static final float MAX_VELOCITY = 300f;
    private static final float MOVEMENT_IMPULSE = 100f;
    private Vector2 velocity;
    private boolean facesRight = true;
    private State state;
    private Animation<TextureAtlas.AtlasRegion> walkAnim;
    private Animation<TextureRegion> standAnim, jumpAnim;
    private TextureAtlas textureAtlasWalking;
    private TextureRegion textureRegionStanding, textureRegionJumping;
    private float elapsedTime = 0;
    private BodyDef bodyDef;
    private Body body;

    public Player(Vector2 spawnPoint, World world) {
        velocity = new Vector2();
        textureAtlasWalking = new TextureAtlas("player/player_walk.atlas");
        walkAnim = new Animation<TextureAtlas.AtlasRegion>(1 / 12f, textureAtlasWalking.getRegions());
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        textureRegionStanding = new TextureRegion(new Texture("player/p1_stand.png"));
        standAnim = new Animation<TextureRegion>(0, textureRegionStanding);

        textureRegionJumping = new TextureRegion(new Texture("player/p1_jump.png"));
        jumpAnim = new Animation<TextureRegion>(0, textureRegionJumping);

        setPosition(spawnPoint.x, spawnPoint.y);
        setWidth(textureRegionStanding.getRegionWidth() * Constants.WORLD_SCALE);
        setHeight(textureRegionStanding.getRegionHeight() * Constants.WORLD_SCALE);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        //                  tileWidth       tileHeight
        bodyDef.position.set(getX() * 70, getY() * 70);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth(), getHeight());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setFriction(0.2f);
        //body.setLinearVelocity(0,5);
        //body.applyForce(0,5,0.5f,0.5f,true);
        shape.dispose();


        state = State.STANDING;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
        Vector2 velocity = body.getLinearVelocity();


        //cap max x velocity
        if (Math.abs(velocity.x) > MAX_VELOCITY) {
            velocity.x = Math.signum(velocity.x) * MAX_VELOCITY;
            body.setLinearVelocity(velocity.x, velocity.y);
        }

        //apply damping
        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && velocity.x != 0) {
            Gdx.app.log("test", "damp");
            body.setLinearVelocity(velocity.x * 0.9f, velocity.y);
        } else {
            state = State.STANDING;
        }

        //move left
        //apply linear impulse if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            state = State.RUNNING;
            facesRight = false;
            if (velocity.x > -MAX_VELOCITY) {
                body.applyLinearImpulse(-MOVEMENT_IMPULSE, 0, body.getPosition().x, body.getPosition().y, true);
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


        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            state = State.JUMPING;
            body.applyForce(0, 10000, body.getLocalCenter().x, body.getLocalCenter().y, true);
        }


        //                               tileWidth                      tileHeight
        setPosition(body.getPosition().x / 70.0f, body.getPosition().y / 70.0f);
        Gdx.app.log("test", "vel x: " + body.getLinearVelocity().x + "x: " + getX() + " y: " + getY());

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

    public BodyDef getBodyDef() {
        return bodyDef;
    }


    private enum State {
        RUNNING, STANDING, JUMPING;
    }
}
