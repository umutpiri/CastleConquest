package com.piri.umut.sprites.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.piri.umut.MyWorld;


/**
 * Created by umut on 3/4/18.
 */

public class Coin extends Item {
    private Array<TextureRegion> frames;
    private Animation<TextureRegion> spin;
    private float elapsedTime;
    public boolean isTaken;
    public float alpha;

    public Coin(MyWorld game, float x, float y) {
        super(x, y);
        elapsedTime = 0;
        isTaken = false;
        alpha = 1f;
        setSize(0.2f, 0.2f);
        rectangle = getBoundingRectangle();
        frames = new Array<TextureRegion>();
        TextureRegion frame1_5 = new TextureRegion(game.manager.get("coin1.png", Texture.class));
        TextureRegion frame2_6 = new TextureRegion(game.manager.get("coin2.png", Texture.class));
        TextureRegion frame3_5 = new TextureRegion(game.manager.get("coin3.png", Texture.class));
        TextureRegion frame4_8 = new TextureRegion(game.manager.get("coin4.png", Texture.class));
        frames.add(frame1_5);
        frames.add(frame2_6);
        frames.add(frame3_5);
        frames.add(frame4_8);
        frame3_5.flip(true, false);
        frame2_6.flip(true, false);
        frames.add(frame3_5);
        frames.add(frame2_6);
        spin = new Animation<TextureRegion>(0.1f, frames);
        setRegion(frames.get(0));
        frames.clear();
    }

    @Override
    public void update(float delta) {
        elapsedTime += delta;
        if (isTaken) {
            elapsedTime += delta;
            setRegion(spin.getKeyFrame(elapsedTime, true));
            alpha -= alpha / 20f;
            setAlpha(alpha);
            setPosition(getX(), getY() + 0.01f);
        } else
            setRegion(spin.getKeyFrame(elapsedTime, true));
    }
}
