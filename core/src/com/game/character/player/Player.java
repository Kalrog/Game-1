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
import com.game.Game;
import com.game.physics.ContactUnit;
import com.game.screens.GameScreen;
import com.game.util.Constants;

import static com.game.util.Constants.*;

/**
 * Created by Philipp on 22.12.2016.
 */
public class Player extends Actor {

    private static final float MAX_VELOCITY = 15f;
    private static final float MOVEMENT_IMPULSE = 0.9f;
    private static final float JUMP_IMPULSE = 900f;
    private static final float WALL_JUMP_IMPULSE = 900f;
    private Vector2 velocity;
    private boolean facesRight = true;
    private State state;
    private Animation<TextureAtlas.AtlasRegion> walkAnim;
    private Animation<TextureRegion> standAnim, jumpAnim;
    private float elapsedTime = 0;
    private BodyDef bodyDef;
    private Body body;
    private World world;
    private boolean isGrounded = false;
    private boolean doubleJump = true;
    private boolean wallJump = false;
    private int groundContacts = 0;
    private int sideContacts = 0;
    private Game game;

    public Player(Vector2 spawnPoint, World world, Game game) {
        this.world = world;
        this.game = game;
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
        velocity = body.getLinearVelocity();
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
            batch.draw(frame, getX(), getY(), frame.getRegionWidth() / PIXEL_PER_METER, frame.getRegionHeight() / PIXEL_PER_METER);
        } else {
            batch.draw(frame, getX() + frame.getRegionWidth() / PIXEL_PER_METER, getY(), -frame.getRegionWidth() / PIXEL_PER_METER, frame.getRegionHeight() / PIXEL_PER_METER);
        }
    }

    private void handleInput() {
        state = State.STANDING;
        //jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //    Gdx.app.log("test" ," isGrounded: "+ isGrounded);
            // normal jump
            if (isGrounded) {
                body.applyForce(0, JUMP_IMPULSE, body.getLocalCenter().x, body.getLocalCenter().y, true);
            }
            // walljump
            else if (wallJump) {
                body.applyForce(0, JUMP_IMPULSE, body.getLocalCenter().x, body.getLocalCenter().y, true);
                if (facesRight) {
                    body.applyForce(-WALL_JUMP_IMPULSE, 0, body.getLocalCenter().x, body.getLocalCenter().y, true);
                    facesRight = false;
                } else {
                    body.applyForce(WALL_JUMP_IMPULSE, 0, body.getLocalCenter().x, body.getLocalCenter().y, true);
                    facesRight = true;
                }
                wallJump = false;
            }
            // double jump
            else if (doubleJump) {
                body.applyForce(0, JUMP_IMPULSE, body.getLocalCenter().x, body.getLocalCenter().y, true);
                doubleJump = false;
            }
        }
        //move left
        //apply linear impulse if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (velocity.x > -MAX_VELOCITY) {
                facesRight = false;
                body.applyLinearImpulse(new Vector2(-MOVEMENT_IMPULSE, 0), body.getWorldCenter(), true);
                state = State.RUNNING;
            }
        }

        //move right
        //apply linear impulse if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (velocity.x < MAX_VELOCITY) {
                facesRight = true;
                body.applyLinearImpulse(new Vector2(MOVEMENT_IMPULSE, 0), body.getWorldCenter(), true);
                state = State.RUNNING;
            }
        }
        handleState();
    }

    private void handleState() {
        if (body.getLinearVelocity().x == 0) {
            state = State.STANDING;
        }
        if (!isGrounded) {
            state = State.JUMPING;
        }
    }

    private void createBody(Vector2 position) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 2f;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(position.x / PIXEL_PER_METER, position.y / PIXEL_PER_METER);

        body = world.createBody(bodyDef);

        // Main Player Fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_TERRAIN |
                Constants.CATEGORY_BIT_COIN | CATEGORY_BIT_DEATH_ZONE | CATEGORY_BIT_MONSTER;
        body.createFixture(fixtureDef).setUserData(new ContactUnit(ContactUnit.PLAYER, this));

        // Player Foot Fixture
        shape.setAsBox(getWidth() / 2 * 0.8f, 0.1f, new Vector2(0, -0.9f), 0);
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_TERRAIN | CATEGORY_BIT_MONSTER;
        body.createFixture(fixtureDef).setUserData(new ContactUnit(ContactUnit.PLAYER_FOOT, this));

        // Player left / right sensor
        shape.setAsBox(getWidth() / 2 + 0.2f, getHeight() / 2 - 0.1f);
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = Constants.CATEGORY_BIT_TERRAIN | CATEGORY_BIT_MONSTER;
        body.createFixture(fixtureDef).setUserData(new ContactUnit(ContactUnit.PLAYER_SIDE, this));
        shape.dispose();

    }

    public void changeGroundContact(int change) {
        groundContacts += change;
        isGrounded = groundContacts != 0;
        doubleJump = true;
    }

    public void changeSideContact(int change) {
        sideContacts += change;
        wallJump = sideContacts != 0;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    private enum State {
        RUNNING, STANDING, JUMPING;
    }

    public void die() {
        game.setScreen(new GameScreen(game));
    }
}
