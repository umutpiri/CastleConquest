package com.piri.umut.sprites.items;

import com.badlogic.gdx.graphics.Texture;
import com.piri.umut.MyWorld;

/**
 * Created by umut on 3/7/18.
 */

public class BluePotion extends Item {
    public BluePotion(MyWorld game, float posx, float posy) {
        super(posx, posy);
        setRegion(game.manager.get("blue_pot1.png", Texture.class));
        setSize(0.25f, 0.33f);
        rectangle = getBoundingRectangle();
    }

    @Override
    public void update(float delta) {

    }
}
