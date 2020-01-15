package com.piri.umut.sprites.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.piri.umut.MyWorld;

/**
 * Created by umut on 2/28/18.
 */

public class Upgrade extends Item {
    private Texture texture;
    public Upgrade(MyWorld game, float x, float y) {
        super(x, y);
        setRegion(game.manager.get(game.activeSkin, Texture.class));
        texture = game.manager.get("cage.png");
        setSize(0.2f, 0.4f);
        rectangle = getBoundingRectangle();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        batch.draw(texture, getX() - 0.05f, getY(), .3f, .5f);
    }
}
