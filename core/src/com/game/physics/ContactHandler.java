package com.game.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.game.character.player.Player;

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

        if (contactUnitA.getId() == ContactUnit.PLAYER
                || contactUnitA.getId() == ContactUnit.PLAYER_FOOT
                || contactUnitA.getId() == ContactUnit.PLAYER_SIDE) {
            beginPlayerContact(contactUnitA, contactUnitB);
        }
        if (contactUnitB.getId() == ContactUnit.PLAYER
                || contactUnitB.getId() == ContactUnit.PLAYER_FOOT
                || contactUnitB.getId() == ContactUnit.PLAYER_SIDE) {
            beginPlayerContact(contactUnitB, contactUnitA);
        }
    }


    private void beginPlayerContact(ContactUnit playerUnit, ContactUnit otherUnit) {
        switch (playerUnit.getId()) {
            case ContactUnit.PLAYER:
                // Player collides with terrain
                if (otherUnit.getId() == ContactUnit.TERRAIN) {

                }
                //Player collides with death zone
                if (otherUnit.getId() == ContactUnit.DEATH_ZONE) {
                    ((Player) playerUnit.getData()).die();
                }
                break;
            case ContactUnit.PLAYER_FOOT:
                ((Player) playerUnit.getData()).changeGroundContact(1);

                break;
            case ContactUnit.PLAYER_SIDE:
                if (otherUnit.getId() == ContactUnit.TERRAIN) {
                    ((Player) playerUnit.getData()).changeSideContact(1);
                }
                break;
        }
    }


    @Override
    public void endContact(Contact contact) {
        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        if (contactUnitA.getId() == ContactUnit.PLAYER
                || contactUnitA.getId() == ContactUnit.PLAYER_FOOT
                || contactUnitA.getId() == ContactUnit.PLAYER_SIDE) {
            endPlayerContact(contactUnitA, contactUnitB);
        }
        if (contactUnitB.getId() == ContactUnit.PLAYER
                || contactUnitB.getId() == ContactUnit.PLAYER_FOOT
                || contactUnitB.getId() == ContactUnit.PLAYER_SIDE) {
            endPlayerContact(contactUnitB, contactUnitA);
        }
    }

    private void endPlayerContact(ContactUnit playerUnit, ContactUnit otherUnit) {
        switch (playerUnit.getId()) {
            case ContactUnit.PLAYER:

                break;
            case ContactUnit.PLAYER_FOOT:
                if (otherUnit.getId() == ContactUnit.TERRAIN) {
                    ((Player) playerUnit.getData()).changeGroundContact(-1);
                }
                break;
            case ContactUnit.PLAYER_SIDE:
                if (otherUnit.getId() == ContactUnit.TERRAIN) {
                    ((Player) playerUnit.getData()).changeSideContact(-1);
                }
                break;
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

        // if ContactUnitA is part of the player and ContactUnitB is some kind of one way platform
        // seems to be unused since the player is always ContactUnitB
        if (((contactUnitA.getId() & ContactUnit.PLAYER) != 0) && ((contactUnitB.getId() & ContactUnit.ONE_WAY) != 0)) {
            //if the player is under the platform
            if (fixtureA.getBody().getPosition().y - ((Player) contactUnitA.getData()).getHeight() / 2 < fixtureB.getBody().getPosition().y) {
                Gdx.app.log("One-Way-Platform", "Player was Contact Unit A");
                contact.setEnabled(false);
            }


        }
        // if ContactUnitB is part of the player and ContactUnitA is some kind of one way platform
        else if (((contactUnitB.getId() & ContactUnit.PLAYER) != 0) && ((contactUnitA.getId() & ContactUnit.ONE_WAY) != 0)) {
            //if the player is under the platform
            if (fixtureB.getBody().getPosition().y - ((Player) contactUnitB.getData()).getHeight() / 2 < fixtureA.getBody().getPosition().y) {
                Gdx.app.log("One-Way-Platform", "Player was Contact Unit B");
                contact.setEnabled(false);
            }
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
