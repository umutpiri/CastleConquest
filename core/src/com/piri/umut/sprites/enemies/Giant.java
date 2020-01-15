package com.piri.umut.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.piri.umut.ConstantValues;
import com.piri.umut.MyWorld;
import com.piri.umut.sprites.Ground;

/**
 * Created by umut on 3/14/18.
 */

public class Giant extends Enemy {
    private float stateTimer;
    private Animation<TextureRegion> idle;
    private float x;

    public Giant(MyWorld game, World world, Ground ground) {
        super(world, ground);
        speed = 0;
        hx = 0.45f;
        hy = 0.3f;
        density = 50f;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 7; i++)
            frames.add(new TextureRegion(game.manager.get
                    ("characters/giant_idle.png", Texture.class), 266 * i, 0, 266, 152));
        idle = new Animation<TextureRegion>(0.1f, frames);
        setRegion(frames.get(0));
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(hx - .03f, hy - .01f);
        bodyType = BodyDef.BodyType.DynamicBody;
        defineBody();
        body.setFixedRotation(true);
        setBounds(body.getPosition().x - hx,
                body.getPosition().y - hy, hx * 2f, hy * 2f);
        this.x = getX();
    }

    @Override
    public void update(float dt) {
        setPosition(body.getPosition().x - hx, body.getPosition().y - hy);
        stateTimer += dt;
        TextureRegion region;
        region = idle.getKeyFrame(stateTimer, true);
        if (x > ConstantValues.WIDTH / 2f / ConstantValues.PPM && !region.isFlipX())
            region.flip(true, false);

        setRegion(region);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void drawFont(Batch batch) {

    }
}
