package com.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.game.character.player.Player;

/**
 * Created by jonathan on 25.12.16.
 */
public class ContactHandler implements ContactListener {
    static ContactHandler instance;

    private ContactHandler(){
        super();
    }

    public static ContactHandler getInstance() {
        if(instance == null)
            instance = new ContactHandler();

        return instance;
    }

    @Override
    public void beginContact(Contact contact) {
        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        if(contactUnitA.getId() == ContactUnit.PLAYER_FOOT && (contactUnitB.getId() == ContactUnit.TERRAIN  || contactUnitB.getId() == ContactUnit.COIN)){
            ((Player)contactUnitA.getData()).changeGroundContact( 1);

        }else if( contactUnitB.getId() == ContactUnit.PLAYER_FOOT && (contactUnitA.getId() == ContactUnit.TERRAIN || contactUnitA.getId() == ContactUnit.COIN)){
            ((Player)contactUnitB.getData()).changeGroundContact( 1);
        }
    }

    @Override
    public void endContact(Contact contact) {

        ContactUnit contactUnitA = ((ContactUnit) contact.getFixtureA().getUserData());
        ContactUnit contactUnitB = ((ContactUnit) contact.getFixtureB().getUserData());

        if(contactUnitA.getId() == ContactUnit.PLAYER_FOOT && (contactUnitB.getId() == ContactUnit.TERRAIN || contactUnitB.getId() == ContactUnit.COIN)){
            ((Player)contactUnitA.getData()).changeGroundContact( -1);

        }else if( contactUnitB.getId() == ContactUnit.PLAYER_FOOT && (contactUnitA.getId() == ContactUnit.TERRAIN || contactUnitA.getId() == ContactUnit.COIN)){
            ((Player)contactUnitB.getData()).changeGroundContact( -1);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
