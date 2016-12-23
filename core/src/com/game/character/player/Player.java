package com.game.character.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

    public Player(Vector2 spawnPoint) {
        velocity = new Vector2();
        textureAtlasWalking = new TextureAtlas("player/player_walk.atlas");
        walkAnim = new Animation<TextureAtlas.AtlasRegion>(1 / 15f, textureAtlasWalking.getRegions());
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        textureRegionStanding = new TextureRegion(new Texture("player/p1_stand.png"));
        standAnim = new Animation<TextureRegion>(0, textureRegionStanding);

        textureRegionJumping = new TextureRegion(new Texture("player/p1_jump.png"));
        jumpAnim = new Animation<TextureRegion>(0, textureRegionJumping);

        setPosition(spawnPoint.x, spawnPoint.y);
        setWidth(textureRegionStanding.getRegionWidth() * Constants.WORLD_SCALE);
        setHeight(textureRegionStanding.getRegionHeight() * Constants.WORLD_SCALE);
        state = State.STANDING;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -MAX_VELOCITY;
            state = State.RUNNING;
            facesRight = false;

        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = MAX_VELOCITY;
            state = State.RUNNING;
            facesRight = true;
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
}
