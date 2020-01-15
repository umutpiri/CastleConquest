package com.piri.umut;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by umut on 3/13/18.
 */

public class SellGroup extends Group {
    private MyWorld game;
    private Image background;
    public Image character, coin;
    public TextButton button;
    public int price;
    TextButton.TextButtonStyle buy;

    public SellGroup(MyWorld game, Texture skin, int x, int y, boolean isBought, int price) {
        this.game = game;
        this.price = price;
        this.setBounds(x, y, 100, 200);
        background = new Image(game.manager.get("sellbg.png", Texture.class));
        skin.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background.setBounds(0, 0, 100, 200);
        character = new Image(skin);
        character.setBounds(5, 40, 90, 155);
        buy = new TextButton.TextButtonStyle();
        addActor(background);
        addActor(character);
        if (isBought) {
            button = new TextButton("USE", game.skin, "shop");
            button.setColor(Color.GREEN);
            addActor(button);
            button.setBounds(5, 0, 95, 35);
        } else {
            button = new TextButton(price + " ", game.skin, "shop");
            coin = new Image(game.manager.get("coin2.png", Texture.class));
            coin.setBounds(78, 8, 15, 17);
            addActor(button);
            button.setBounds(5, 0, 95, 35);
            addActor(coin);
        }
    }
}
