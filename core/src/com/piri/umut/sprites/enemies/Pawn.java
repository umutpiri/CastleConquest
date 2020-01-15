package com.piri.umut.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.piri.umut.MyWorld;
import com.piri.umut.sprites.Ground;

/**
 * Created by umut on 3/3/18.
 */

public class Pawn extends Enemy {
    private Array<TextureRegion> frames;
    private float stateTimer;
    Animation<TextureRegion> walkAnimation;
    public Pawn(MyWorld game, World world, Ground ground) {
        super(world, ground);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(game.manager.get("characters/little_knight_walk.png", Texture.class), 0, 0, 32, 32));
        frames.add(new TextureRegion(game.manager.get("characters/little_knight_walk.png", Texture.class), 32, 0, 32, 32));
        frames.add(new TextureRegion(game.manager.get("characters/little_knight_walk.png", Texture.class), 64, 0, 32, 32));
        walkAnimation = new Animation<TextureRegion>(0.1f, frames);
        setRegion(frames.get(0));
        speed = 1.3f;
        hx = 0.1f;
        hy = 0.1f;
        stateTimer = 0;
        shape = new CircleShape();
        shape.setRadius(hx);
        bodyType = BodyDef.BodyType.DynamicBody;
        defineBody();
        setBounds(body.getPosition().x, body.getPosition().y, hx * 2.2f, hy * 2.2f);
    }

    @Override
    public void update(float dt) {
        stateTimer += dt;
        TextureRegion region;
        region = (TextureRegion) walkAnimation.getKeyFrame(stateTimer, true);
        if (speed > 0 && region.isFlipX())
            region.flip(true, false);
        if (speed < 0 && !region.isFlipX())
            region.flip(true, false);
        setRegion(region);
        setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
        if (getX() < ground.getX() && speed < 0) {
            reverseSpeed();
        }
        if (getX() + getWidth() > ground.getX() + ground.getWidth() && speed > 0) {
            reverseSpeed();
        }
        body.setLinearVelocity(speed, 0);
    }
}
