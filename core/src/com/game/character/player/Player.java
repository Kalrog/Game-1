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
import com.game.level.Level;
import com.game.util.Constants;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Player extends Actor {

    private static final float MAX_VELOCITY = 5f;
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
        //                  tileWidth       tileHeight
        bodyDef.position.set(getX()*70, getY()*70);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2, getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        //body.setLinearVelocity(0,5);
        //body.applyForce(0,5,0.5f,0.5f,true);
        shape.dispose();





        state = State.STANDING;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;

        //                               tileWidth                      tileHeight
        setPosition(body.getPosition().x / 70, body.getPosition().y / 70.0f);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -MAX_VELOCITY;
            state = State.RUNNING;
            facesRight = false;

        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = MAX_VELOCITY;
            state = State.RUNNING;
            facesRight = true;
        } else {
            velocity.x = 0;
            state = State.STANDING;
        }
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

    private enum State {
        RUNNING, STANDING, JUMPING;
    }


    public BodyDef getBodyDef() {
        return bodyDef;
    }
}
