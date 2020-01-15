package com.piri.umut;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.piri.umut.sprites.Ground;
import com.piri.umut.sprites.Player;
import com.piri.umut.sprites.enemies.Arrow;
import com.piri.umut.sprites.enemies.Enemy;
import com.piri.umut.sprites.enemies.Giant;
import com.piri.umut.sprites.enemies.Pawn;

/**
 * Created by umut on 1/28/18.
 */

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case ConstantValues.FOOT_BIT | ConstantValues.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == ConstantValues.FOOT_BIT)
                    ((Player) fixA.getUserData()).collideCount++;
                else
                    ((Player) fixB.getUserData()).collideCount++;
                break;
            case ConstantValues.ENEMY_BIT | ConstantValues.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == ConstantValues.ENEMY_BIT) {
                    if (fixA.getUserData() instanceof Giant) {
                        if (Inventory.isGreaterThan8)
                            ((Enemy) fixA.getUserData()).readyToDie = true;
                        else
                            ((Player) fixB.getUserData()).readyToDie = true;
                    } else {
                        if (((Enemy) fixA.getUserData()).getY() > ((Player) fixB.getUserData()).getBody().getPosition().y) {
                            if (MathUtils.random(0, 200) < Inventory.battleLevel) {
                                ((Enemy) fixA.getUserData()).readyToDie = true;
                            } else {
                                if (!((Enemy) fixA.getUserData()).readyToDie)
                                    ((Player) fixB.getUserData()).readyToDie = true;
                                if (fixA.getUserData() instanceof Pawn)
                                    ((Enemy) fixA.getUserData()).readyToDie = true;
                            }
                        } else
                            ((Enemy) fixA.getUserData()).readyToDie = true;
                    }
                } else {
                    if (fixB.getUserData() instanceof Giant) {
                        if (Inventory.isGreaterThan8)
                            ((Enemy) fixB.getUserData()).readyToDie = true;
                        else
                            ((Player) fixA.getUserData()).readyToDie = true;
                    } else {
                        if (((Enemy) fixB.getUserData()).getY() > ((Player) fixA.getUserData()).getBody().getPosition().y) {
                            if (MathUtils.random(0, 200) < Inventory.battleLevel) {
                                ((Enemy) fixB.getUserData()).readyToDie = true;
                            } else {
                                if (!((Enemy) fixB.getUserData()).readyToDie)
                                    ((Player) fixA.getUserData()).readyToDie = true;
                                if (fixB.getUserData() instanceof Pawn)
                                    ((Enemy) fixB.getUserData()).readyToDie = true;
                            }
                        } else
                            ((Enemy) fixB.getUserData()).readyToDie = true;
                    }
                }
                break;
            case ConstantValues.ARROW_BIT | ConstantValues.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == ConstantValues.ARROW_BIT) {
                    if (!((Arrow) fixA.getUserData()).readyToDie) {
                        ((Arrow) fixA.getUserData()).readyToDie = true;
                        ((Player) fixB.getUserData()).readyToDie = true;
                    }
                } else {
                    if (!((Arrow) fixB.getUserData()).readyToDie) {
                        ((Arrow) fixB.getUserData()).readyToDie = true;
                        ((Player) fixA.getUserData()).readyToDie = true;
                    }
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case ConstantValues.FOOT_BIT | ConstantValues.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == ConstantValues.FOOT_BIT)
                    ((com.piri.umut.sprites.Player) fixA.getUserData()).collideCount--;
                else
                    ((com.piri.umut.sprites.Player) fixB.getUserData()).collideCount--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getFilterData().categoryBits == ConstantValues.GROUND_BIT &&
                fixB.getFilterData().categoryBits == ConstantValues.PLAYER_BIT) {
            if (((Ground) fixA.getUserData()).getY() + 0.12f >= ((Player) fixB.getUserData()).getBody().getPosition().y - 0.25f)
                contact.setEnabled(false);
        } else if (fixB.getFilterData().categoryBits == ConstantValues.GROUND_BIT &&
                fixA.getFilterData().categoryBits == ConstantValues.PLAYER_BIT) {
            if (((Ground) fixB.getUserData()).getY() + 0.12f >= ((Player) fixA.getUserData()).getBody().getPosition().y - 0.25f)
                contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        contact.setEnabled(true);
    }
}
