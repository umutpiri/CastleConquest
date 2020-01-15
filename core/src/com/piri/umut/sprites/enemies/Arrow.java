package com.piri.umut.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.piri.umut.ConstantValues;
import com.piri.umut.Inventory;
import com.piri.umut.MyWorld;

/**
 * Created by umut on 3/2/18.
 */

public class Arrow extends Sprite {
    private World world;
    private Body body;
    private float posX, posY;
    public boolean readyToDie;

    public Arrow(MyWorld game, World world, float posX, float posY) {
        super(game.manager.get("arrow.png", Texture.class));
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        defineBody();
        setBounds(body.getPosition().x, body.getPosition().y, 0.2f, 0.4f);
        if (game.isSound)
            game.manager.get("audio/arrow.ogg", Sound.class).play();
    }

    public void update() {
        setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
    }

    public void death() {
        world.destroyBody(body);
    }

    private void defineBody() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(posX, posY);
        bdef.linearDamping = 2f;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.08f, 0.2f);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = MathUtils.random(6, 9) / 10f;
        fdef.filter.categoryBits = ConstantValues.ARROW_BIT;
        if (MathUtils.random(0, 100) > Inventory.dodgeLevel)
            fdef.filter.maskBits = ConstantValues.PLAYER_BIT;
        else
            fdef.filter.maskBits = ConstantValues.NOTHING_BIT;
        body.createFixture(fdef).setUserData(this);
    }
}
