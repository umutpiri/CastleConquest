package com.piri.umut.sprites.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by umut on 3/8/18.
 */

public abstract class Item extends Sprite {
    public Rectangle rectangle;

    public Item(float posx, float posy) {
        setPosition(posx, posy);
    }

    public abstract void update(float delta);
}
