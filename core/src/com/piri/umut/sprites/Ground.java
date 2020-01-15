package com.piri.umut.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.piri.umut.ConstantValues;
import com.piri.umut.MyWorld;

public class Ground extends Sprite {
    private World world;
    public Body body;
    public float moveSpeed;
    public boolean isMoving;

    public Ground(MyWorld game, World world, float posx, float posy, int width, int height) {
        super(game.manager.get("plank.png", Texture.class));
        this.world=world;
        BodyDef bdef = new BodyDef();
        bdef.position.set((posx + width / 2f) / ConstantValues.PPM, (posy + height / 2f) / ConstantValues.PPM);
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f / ConstantValues.PPM, height / 2f / ConstantValues.PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0;
        body.createFixture(fdef).setUserData(this);
        setBounds(posx / ConstantValues.PPM, posy / ConstantValues.PPM,
                width / ConstantValues.PPM, height / ConstantValues.PPM);
        moveSpeed = 0.6f;
        isMoving = false;
    }

    public void update() {
        setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
    }

    public void reallocate(){
        float width = getWidth() > 2f ? getWidth() - 0.06f : getWidth();
        float bodyX = MathUtils.random(width / 2f, ConstantValues.WIDTH / ConstantValues.PPM - width / 2f);
        float bodyY = body.getPosition().y+900/ConstantValues.PPM;
        world.destroyBody(body);
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.position.set(bodyX,bodyY);
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, getHeight() / 2f);

        FixtureDef fdef = new FixtureDef();
        fdef.shape=shape;
        fdef.filter.categoryBits = ConstantValues.GROUND_BIT;
        fdef.friction = 1f;
        body.createFixture(fdef).setUserData(this);
        setBounds(bodyX - width / 2f, bodyY - getHeight() / 2f,
                width, getHeight());
    }

    public void move() {
        body.setLinearVelocity(moveSpeed, 0);
        isMoving = true;
    }

    public void reverse() {
        moveSpeed = -moveSpeed;
        body.setLinearVelocity(moveSpeed, 0);
    }
}
