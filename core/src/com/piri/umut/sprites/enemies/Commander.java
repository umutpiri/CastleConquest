package com.piri.umut.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.piri.umut.ConstantValues;
import com.piri.umut.MyWorld;
import com.piri.umut.sprites.Ground;

/**
 * Created by umut on 3/3/18.
 */

public class Commander extends Enemy {
    private float stateTimer;
    private Animation<TextureRegion> idle;
    private float x;
    public Commander(MyWorld game, World world, Ground ground) {
        super(world, ground);
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 10; i++)
            frames.add(new TextureRegion(game.manager.get("characters/knight_idle.png", Texture.class),
                    i * 100, 0, 100, 138));
        idle = new Animation<TextureRegion>(0.1f, frames);
        setRegion(frames.get(0));
        speed = 0;
        hx = 0.2f;
        hy = 0.3f;
        density = 50f;
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(hx - 0.02f, hy);
        bodyType = BodyDef.BodyType.DynamicBody;
        defineBody();
        body.setFixedRotation(true);
        setBounds(body.getPosition().x - hx,
                body.getPosition().y - hy, hx * 2f, hy * 2f);
        this.x = getX();
    }

    @Override
    public void update(float dt) {
        setPosition(body.getPosition().x - hx, body.getPosition().y - hy - 0.02f);
        stateTimer += dt;
        TextureRegion region;
        region = idle.getKeyFrame(stateTimer, true);
        if (x > ConstantValues.WIDTH / 2f / ConstantValues.PPM && !region.isFlipX())
            region.flip(true, false);
        setRegion(region);
    }
}
