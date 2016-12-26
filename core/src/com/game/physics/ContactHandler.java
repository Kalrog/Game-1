package com.game.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.game.character.player.Player;

/**
 * Created by jonathan on 25.12.16.
 */
public class ContactHandler implements ContactListener {
    static ContactHandler instance;

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

        // if ContactUnitA is a playerfoot and ContactUnitB is some kind of Terrain
        if (((contactUnitA.getId() & ContactUnit.PLAYER_FOOT) != 0) && ((contactUnitB.getId() & ContactUnit.TERRAIN) != 0)) {
            ((Player) contactUnitA.getData()).changeGroundContact(1);

        }
        // if ContactUnitB is a playerfoot and ContactUnitA is some kind of Terrain
        else if (((contactUnitB.getId() & ContactUnit.PLAYER_FOOT) != 0) && ((contactUnitA.getId() & ContactUnit.TERRAIN) != 0)) {
            ((Player) contactUnitB.getData()).changeGroundContact(1);
        }
    }

    @Override
    public void endContact(Contact contact) {

        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        // if ContactUnitA is a playerfoot and ContactUnitB is some kind of Terrain
        if (((contactUnitA.getId() & ContactUnit.PLAYER_FOOT) != 0) && ((contactUnitB.getId() & ContactUnit.TERRAIN) != 0)) {
            ((Player) contactUnitA.getData()).changeGroundContact(-1);

        }
        // if ContactUnitB is a playerfoot and ContactUnitA is some kind of Terrain
        else if (((contactUnitB.getId() & ContactUnit.PLAYER_FOOT) != 0) && ((contactUnitA.getId() & ContactUnit.TERRAIN) != 0)) {
            ((Player) contactUnitB.getData()).changeGroundContact(-1);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        WorldManifold worldManifold;
        worldManifold = contact.getWorldManifold();
        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        // if ContactUnitA is part of the player and ContactUnitB is some kind of one way platform
        if (((contactUnitA.getId() & (ContactUnit.PLAYER_FOOT | ContactUnit.PLAYER)) != 0) && ((contactUnitB.getId() & ContactUnit.ONE_WAY) != 0)) {
            //if the player is under the platform
            if(worldManifold.getNormal().y > 0){
                Gdx.app.log("One-Way-Platform","Player was Contact Unit A");
                contact.setEnabled(false);
            }


        }
        // if ContactUnitB is part of the player and ContactUnitA is some kind of one way platform
        else if (((contactUnitB.getId() & (ContactUnit.PLAYER_FOOT | ContactUnit.PLAYER)) != 0) && ((contactUnitA.getId() & ContactUnit.ONE_WAY) != 0)) {
            //if the player is under the platform
            if(worldManifold.getNormal().y < 0) {
                Gdx.app.log("One-Way-Platform","Player was Contact Unit B");
                contact.setEnabled(false);
            }
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
