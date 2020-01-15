package com.piri.umut.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.piri.umut.ConstantValues;
import com.piri.umut.MyWorld;


public class Player extends Sprite {

    private World world;
    private Body body;
    private float posx, posy;
    public float jumpTimeOut;
    public int collideCount;
    public boolean readyToDie;

    public Player(MyWorld game, World world, float posx, float posy) {
        super(game.manager.get(game.activeSkin, Texture.class));
        this.world = world;
        this.posx = posx;
        this.posy = posy;
        jumpTimeOut = 10;
        collideCount = 0;
        readyToDie = false;
        float width, height;
        width = getWidth() / 6f;
        height = getHeight() / 6f;
        if (width < 24f)
            width = 24f;
        if (height < 50f)
            height = 50f;
        if (width > 40f)
            width = 40f;
        if (height > 60)
            height = 60;
        definePlayer();
        setBounds(body.getPosition().x - width / 2f, body.getPosition().y - height / 2f,
                width / ConstantValues.PPM, height / ConstantValues.PPM);
    }

    public void update(float delta) {

    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(posx, posy);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearDamping = 1f;
        bdef.fixedRotation = true;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / ConstantValues.PPM, 25 / ConstantValues.PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0;
        fdef.density = 3.8f;
        fdef.filter.categoryBits = ConstantValues.PLAYER_BIT;
        fdef.filter.maskBits = ConstantValues.ENEMY_BIT | ConstantValues.GROUND_BIT | ConstantValues.ARROW_BIT;
        body.createFixture(fdef).setUserData(this);

        PolygonShape shape1 = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(12 / ConstantValues.PPM, -25 / ConstantValues.PPM);
        vertices[1] = new Vector2(-12 / ConstantValues.PPM, -25 / ConstantValues.PPM);
        vertices[2] = new Vector2(12 / ConstantValues.PPM, -24 / ConstantValues.PPM);
        vertices[3] = new Vector2(-12 / ConstantValues.PPM, -24 / ConstantValues.PPM);
        shape1.set(vertices);
        fdef.shape = shape1;
        fdef.isSensor = true;
        fdef.filter.categoryBits = ConstantValues.FOOT_BIT;
        body.createFixture(fdef).setUserData(this);


    }

    public void destroyBody() {
        world.destroyBody(body);
    }

    public Body getBody() {
        return body;
    }
}
