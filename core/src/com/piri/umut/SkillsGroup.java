package com.piri.umut;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by umut on 3/14/18.
 */

public class SkillsGroup extends Group {
    public TextButton buy;
    public Label label;
    public int price;

    public SkillsGroup(MyWorld game, Image image, String message, int x, int y, int level, int price) {
        this.setBounds(x + 5, y, ConstantValues.WIDTH - 10, 80);
        this.price = price;
        Image background = new Image(game.manager.get("sellbgh.png", Texture.class));
        background.setBounds(0, 0, ConstantValues.WIDTH - 10, 80);
        buy = new TextButton("UPGRADE\n" + price, game.skin, "small");
        buy.getLabel().setFontScale(.7f);
        buy.setBounds(getWidth() - 120, 0, 120, 80);
        label = new Label(message, game.skin);
        label.setFontScale(.5f);
        label.setWrap(true);
        label.setColor(Color.BLACK);
        label.setBounds(112, 5, 195, 70);
        addActor(background);
        addActor(buy);
        addActor(image);
        addActor(label);
    }
}
