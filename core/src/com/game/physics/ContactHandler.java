package com.game.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.game.character.monsters.Walker;
import com.game.character.player.Player;

import static com.game.physics.ContactUnit.*;

/**
 * Created by jonathan on 25.12.16.
 */
public class ContactHandler implements ContactListener {
    private static ContactHandler instance;

    private ContactHandler() {
        super();
    }

    public static ContactHandler getInstance() {
        if (instance == null)
            instance = new ContactHandler();

        return instance;
    }

    @Override
    public void beginContact(Contact contact) {
        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        if (contactUnitA.getId() == PLAYER
                || contactUnitA.getId() == PLAYER_FOOT
                || contactUnitA.getId() == PLAYER_SIDE) {
            beginPlayerContact(contactUnitA, contactUnitB);
        }
        if (contactUnitB.getId() == PLAYER
                || contactUnitB.getId() == PLAYER_FOOT
                || contactUnitB.getId() == PLAYER_SIDE) {
            beginPlayerContact(contactUnitB, contactUnitA);
        }
        if (contactUnitA.getId() == WALKER_SENSOR_L || contactUnitA.getId() == WALKER_SENSOR_R) {
            beginWalkerContact(contactUnitA, contactUnitB);
        }
        if (contactUnitB.getId() == WALKER_SENSOR_L || contactUnitB.getId() == WALKER_SENSOR_R) {
            beginWalkerContact(contactUnitB, contactUnitA);
        }
    }


    private void beginPlayerContact(ContactUnit playerUnit, ContactUnit otherUnit) {
        switch (playerUnit.getId()) {
            case PLAYER:
                // Player collides with terrain
                if (otherUnit.getId() == TERRAIN) {

                }
                //Player collides with death zone
                if (otherUnit.getId() == DEATH_ZONE) {
                    ((Player) playerUnit.getData()).die();
                }
                //Player collides with monster
                if (otherUnit.getId() == MONSTER) {
                    if (otherUnit.getData().getClass() == Walker.class) {
                        ((Player) playerUnit.getData()).die();
                    }
                }
                break;
            case PLAYER_FOOT:
                if (otherUnit.getId() == TERRAIN || otherUnit.getId() == TERRAIN_ONE_WAY || otherUnit.getId() == MONSTER) {
                    ((Player) playerUnit.getData()).changeGroundContact(1);
                }
                if (otherUnit.getId() == MONSTER) {
                    if (otherUnit.getData().getClass() == Walker.class) {
                        ((Walker) otherUnit.getData()).die();
                    }
                }

                break;
            case PLAYER_SIDE:
                if (otherUnit.getId() == TERRAIN) {
                    ((Player) playerUnit.getData()).changeSideContact(1);
                }
                break;
        }
    }

    private void beginWalkerContact(ContactUnit walkerUnit, ContactUnit otherUnit) {
        Walker walker = ((Walker) walkerUnit.getData());
        if (walkerUnit.getId() == WALKER_SENSOR_R && walker.isFacingRight()) {
            walker.turnAround();
        } else if (walkerUnit.getId() == WALKER_SENSOR_L && !walker.isFacingRight()) {
            walker.turnAround();
        }
    }


    @Override
    public void endContact(Contact contact) {
        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        if (contactUnitA.getId() == PLAYER
                || contactUnitA.getId() == PLAYER_FOOT
                || contactUnitA.getId() == PLAYER_SIDE) {
            endPlayerContact(contactUnitA, contactUnitB);
        }
        if (contactUnitB.getId() == PLAYER
                || contactUnitB.getId() == PLAYER_FOOT
                || contactUnitB.getId() == PLAYER_SIDE) {
            endPlayerContact(contactUnitB, contactUnitA);
        }
        if (contactUnitA.getId() == WALKER_SENSOR_L || contactUnitA.getId() == WALKER_SENSOR_R) {
            endWalkerContact(contactUnitA, contactUnitB);
        }
        if (contactUnitB.getId() == WALKER_SENSOR_L || contactUnitB.getId() == WALKER_SENSOR_R) {
            endWalkerContact(contactUnitB, contactUnitA);
        }
    }

    private void endPlayerContact(ContactUnit playerUnit, ContactUnit otherUnit) {
        switch (playerUnit.getId()) {
            case PLAYER:

                break;
            case PLAYER_FOOT:
                if (otherUnit.getId() == TERRAIN || otherUnit.getId() == TERRAIN_ONE_WAY || otherUnit.getId() == MONSTER) {
                    ((Player) playerUnit.getData()).changeGroundContact(-1);
                }
                break;
            case PLAYER_SIDE:
                if (otherUnit.getId() == TERRAIN) {
                    ((Player) playerUnit.getData()).changeSideContact(-1);
                }
                break;
        }
    }

    private void endWalkerContact(ContactUnit walkerUnit, ContactUnit otherUnit) {
        Walker walker = ((Walker) walkerUnit.getData());
        if (walkerUnit.getId() == WALKER_SENSOR_R && walker.isFacingRight()) {
            walker.turnAround();
        } else if (walkerUnit.getId() == WALKER_SENSOR_L && !walker.isFacingRight()) {
            walker.turnAround();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // get world information about the contact
        WorldManifold worldManifold;
        worldManifold = contact.getWorldManifold();
        // get fixtures involved in contact
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        // get contact units involved in contact
        ContactUnit contactUnitA = ((ContactUnit) fixtureA.getUserData());
        ContactUnit contactUnitB = ((ContactUnit) fixtureB.getUserData());

        // if ContactUnitB is the player and ContactUnitA is a one way platform
        if (contactUnitB.getId() == PLAYER && contactUnitA.getId() == TERRAIN_ONE_WAY) {
            //if the player is under the platform
            if (fixtureB.getBody().getPosition().y - ((Player) contactUnitB.getData()).getHeight() / 2 < fixtureA.getBody().getPosition().y) {
                //Gdx.app.log("One-Way-Platform", "Player was Contact Unit B");
                contact.setEnabled(false);
            }
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
