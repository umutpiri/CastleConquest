package com.piri.umut.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.piri.umut.ConstantValues;
import com.piri.umut.sprites.Ground;

/**
 * Created by umut on 2/28/18.
 */

public abstract class Enemy extends Sprite {
    private World world;
    public Body body;
    protected Ground ground;
    protected float speed;
    protected Shape shape;
    protected BodyDef.BodyType bodyType;
    protected float hx, hy;
    protected float density;
    public boolean readyToDie;

    public Enemy(World world, Ground ground) {
        this.world = world;
        this.ground = ground;
        readyToDie = false;
        density = 5f;
    }

    abstract public void update(float dt);

    public void death() {
        world.destroyBody(body);
    }

    protected void defineBody() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(MathUtils.random(ground.getX() + hx, ground.getX() + ground.getWidth()),
                ground.getY() + ground.getHeight() + hy);
        bdef.type = bodyType;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = density;
        fdef.friction = 1f;
        fdef.filter.categoryBits = ConstantValues.ENEMY_BIT;
        fdef.filter.maskBits = ConstantValues.PLAYER_BIT | ConstantValues.GROUND_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public void reverseSpeed() {
        speed = -speed;
    }
}
