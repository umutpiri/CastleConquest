package com.piri.umut.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.piri.umut.ConstantValues;
import com.piri.umut.Inventory;
import com.piri.umut.MyWorld;
import com.piri.umut.SellGroup;
import com.piri.umut.SkillsGroup;

/**
 * Created by umut on 3/12/18.
 */

public class ShopScreen implements Screen {
    private Stage stage;
    private MyWorld game;
    private Image blackBG;
    private Label coinLabel;
    private boolean isVideo;

    //Characters tab
    private TextButton charactersTab;
    private Group characters;
    private Image activeImage;
    private SellGroup viking1;
    private SellGroup viking2;
    private SellGroup viking3;
    private SellGroup ninja;
    private SellGroup samurai;
    private SellGroup ork1;
    private SellGroup ork2;
    private SellGroup ork3;
    //Upgrades tab
    private TextButton upgradesTab;
    private Group upgrades;
    private SkillsGroup watch;
    private SkillsGroup watchFill;
    private SkillsGroup dodge;
    private SkillsGroup floor;
    private SkillsGroup battle;
    private SkillsGroup coinValue;
    private SkillsGroup cage;

    public ShopScreen(MyWorld game) {
        this.game = game;
        OrthographicCamera cam = new OrthographicCamera();
        stage = new Stage(new StretchViewport(ConstantValues.WIDTH, ConstantValues.HEIGHT, cam), game.batch);
        Gdx.input.setInputProcessor(stage);
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, .6f);
        pixmap.fillRectangle(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT);
        blackBG = new Image(new Texture(pixmap));
        blackBG.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        isVideo = false;

        Gdx.input.setCatchBackKey(true);
//        stage.setDebugAll(true);
    }

    @Override
    public void show() {
        Image image = new Image(game.manager.get("shopbg.png", Texture.class));
        image.setBounds(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT);
        stage.addActor(image);
        initCharacters();
        initUpgrades();

        Image goldbar = new Image(new NinePatchDrawable(new NinePatch(game.manager.get("goldbar.9.png", Texture.class))));
        goldbar.setBounds(stage.getWidth() - 155, stage.getHeight() - 65, 149, 59);
        stage.addActor(goldbar);

        coinLabel = new Label("" + Inventory.gold, game.skin, "big");
        coinLabel.setFontScale(.6f);
        coinLabel.setColor(Color.GOLD);
        coinLabel.setPosition(goldbar.getX() + 40, goldbar.getY());
        stage.addActor(coinLabel);
        Button backButton = new Button(game.skin, "back");
        backButton.setBounds(5, stage.getHeight() - 80, 75, 75);
        stage.addActor(backButton);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                game.prefs.putString("activeSkin", game.activeSkin);
                game.prefs.flush();
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        });

        charactersTab = new TextButton("Characters", game.skin, "small");
        charactersTab.setBounds(15, 10, 200, 80);
        charactersTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                upgrades.setVisible(false);
                characters.setVisible(true);
                charactersTab.setDisabled(true);
                upgradesTab.setDisabled(false);
            }
        });

        upgradesTab = new TextButton("Upgrades", game.skin, "small");
        upgradesTab.setBounds(235, 10, 200, 80);
        upgradesTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                characters.setVisible(false);
                upgrades.setVisible(true);
                upgradesTab.setDisabled(true);
                charactersTab.setDisabled(false);
            }
        });
        charactersTab.setDisabled(true);
        stage.addActor(charactersTab);
        stage.addActor(upgradesTab);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0.5f, .7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        coinLabel.setText("" + Inventory.gold);
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.prefs.putString("activeSkin", game.activeSkin);
            game.prefs.flush();
            dispose();
            game.setScreen(new MenuScreen(game));
        }
        if (isVideo && game.handler.isRewardEarned()) {
            Inventory.gold += 100;
            game.prefs.putInteger("gold", Inventory.gold);
            game.prefs.flush();
            isVideo = false;
        }
        if (game.handler.isSignedIn()) {
            if (Inventory.boughtSkins == 1)
                game.handler.unlockAchievement("CgkIxLe0nq0MEAIQAw");
            if (Inventory.boughtSkins == 2)
                game.handler.unlockAchievement("CgkIxLe0nq0MEAIQBA");
        }

        stage.act(delta);
        stage.draw();
    }

    private void initUpgrades() {
        upgrades = new Group();
        upgrades.setBounds(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT / 2f + 50);

        Image watchImage = new Image(game.manager.get("watchSkill.png", Texture.class));
        watchImage.setBounds(23, 10, 65, 65);
        String message = "Increase your clock energy capacity\nCurrent level : " + Inventory.watchLevel;
        watch = new SkillsGroup(game, watchImage, message, 0, 0, Inventory.watchLevel, (Inventory.watchLevel + 1) * 15);
        watch.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < watch.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= watch.price;
                    Inventory.watchLevel++;
                    watch.price = (Inventory.watchLevel + 1) * 15;
                    watch.buy.setText("UPGRADE\n" + watch.price);
                    watch.label.setText("Increase your clock energy capacity\nCurrent level : " + Inventory.watchLevel);
                    game.prefs.putInteger("watchLevel", Inventory.watchLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });

        Image dodgeImage = new Image(game.manager.get("dodgeSkill.png", Texture.class));
        dodgeImage.setBounds(25, 5, 70, 75);
        message = "Increase chance to dodge arrows\nCurrent level : " + Inventory.dodgeLevel;
        dodge = new SkillsGroup(game, dodgeImage, message, 0, 90,
                Inventory.dodgeLevel, (Inventory.dodgeLevel + 1) * 20);
        dodge.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < dodge.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= dodge.price;
                    Inventory.dodgeLevel++;
                    dodge.price = (Inventory.dodgeLevel + 1) * 20;
                    dodge.buy.setText("UPGRADE\n" + dodge.price);
                    dodge.label.setText("Increase chance to dodge arrows\nCurrent level : " + Inventory.dodgeLevel);
                    game.prefs.putInteger("dodgeLevel", Inventory.dodgeLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });

        Image floorImage = new Image(game.manager.get("floorSkill.png", Texture.class));
        floorImage.setBounds(2, 30, 100, 20);
        message = "Enlarge size of the floors\nCurrent level : " + Inventory.floorLevel;
        floor = new SkillsGroup(game, floorImage, message, 0, 180,
                Inventory.floorLevel, (Inventory.floorLevel + 1) * 30);
        floor.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < floor.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= floor.price;
                    Inventory.floorLevel++;
                    floor.price = (Inventory.floorLevel + 1) * 30;
                    floor.buy.setText("UPGRADE\n" + floor.price);
                    floor.label.setText("Enlarge size of the floors\nCurrent level : " + Inventory.floorLevel);
                    game.prefs.putInteger("floorLevel", Inventory.floorLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });

        Image battleImage = new Image(game.manager.get("battleSkill.png", Texture.class));
        battleImage.setBounds(22, 10, 65, 65);
        message = "Increase chance to win battles under enemy \nCurrent level : " + Inventory.battleLevel;
        battle = new SkillsGroup(game, battleImage, message, 0, 270,
                Inventory.battleLevel, (Inventory.battleLevel + 1) * 40);
        battle.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < battle.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= battle.price;
                    Inventory.battleLevel++;
                    battle.price = (Inventory.battleLevel + 1) * 40;
                    battle.buy.setText("UPGRADE\n" + battle.price);
                    battle.label.setText("Increase chance to win battles under enemy \nCurrent level : " + Inventory.battleLevel);
                    game.prefs.putInteger("battleLevel", Inventory.battleLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });

        Image watchFillImage = new Image(game.manager.get("fillSkill.png", Texture.class));
        watchFillImage.setBounds(23, 10, 65, 65);
        message = "Increase clock energy fill speed\nCurrent level : " + Inventory.watchFillLevel;
        watchFill = new SkillsGroup(game, watchFillImage, message, 0, 360,
                Inventory.watchFillLevel, (Inventory.watchFillLevel + 1) * 15);
        watchFill.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < watchFill.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= watchFill.price;
                    Inventory.watchFillLevel++;
                    watchFill.price = (Inventory.watchFillLevel + 1) * 15;
                    watchFill.buy.setText("UPGRADE\n" + watchFill.price);
                    watchFill.label.setText("Increase clock energy fill speed\nCurrent level : " + Inventory.watchFillLevel);
                    game.prefs.putInteger("watchFillLevel", Inventory.watchFillLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });

        Image coinValueImage = new Image(game.manager.get("coinSkill.png", Texture.class));
        coinValueImage.setBounds(32, 15, 50, 50);
        message = "Increase Coin value\nCurrent level : " + Inventory.coinValueLevel;
        coinValue = new SkillsGroup(game, coinValueImage, message, 0, 450,
                Inventory.coinValueLevel, (Inventory.coinValueLevel + 1) * 30);
        coinValue.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < coinValue.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= coinValue.price;
                    Inventory.coinValueLevel++;
                    coinValue.price = (Inventory.coinValueLevel + 1) * 30;
                    coinValue.buy.setText("UPGRADE\n" + coinValue.price);
                    coinValue.label.setText("Increase Coin value\nCurrent level : " + Inventory.coinValueLevel);
                    game.prefs.putInteger("coinValueLevel", Inventory.coinValueLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });

        Image cageImage = new Image(game.manager.get("cageSkill.png", Texture.class));
        cageImage.setBounds(23, 10, 60, 60);
        message = "Increase chance to win 2 prisoners from one cage\nCurrent level : " + Inventory.cageLevel;
        cage = new SkillsGroup(game, cageImage, message, 0, 540,
                Inventory.cageLevel, (Inventory.cageLevel + 1) * 25);
        cage.buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.gold < cage.price) {
                    stage.addActor(blackBG);
                    showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                } else {
                    if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                    Inventory.gold -= cage.price;
                    Inventory.cageLevel++;
                    cage.price = (Inventory.cageLevel + 1) * 25;
                    cage.buy.setText("UPGRADE\n" + cage.price);
                    cage.label.setText("Increase chance to win 2 prisoners from one cage\nCurrent level : " + Inventory.cageLevel);
                    game.prefs.putInteger("cageLevel", Inventory.cageLevel);
                }
                game.prefs.putInteger("gold", Inventory.gold);
                game.prefs.flush();
            }
        });


        upgrades.addActor(watch);
        upgrades.addActor(dodge);
        upgrades.addActor(floor);
        upgrades.addActor(battle);
        upgrades.addActor(watchFill);
        upgrades.addActor(coinValue);
        upgrades.addActor(cage);

        upgrades.setVisible(false);
        stage.addActor(upgrades);
        upgrades.moveBy(0, 100);
    }

    private void initCharacters() {
        characters = new Group();
        characters.setBounds(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT / 2f + 50);
        viking1 = new SellGroup(game, game.manager.get("characters/viking1.png", Texture.class),
                5, 5, Inventory.isViking1, 0);
        viking1.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(viking1.character.getDrawable());
                if (Inventory.isViking1)
                    game.activeSkin = "characters/viking1.png";
            }
        });
        viking1.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isViking1) {
                    activeImage.setDrawable(viking1.character.getDrawable());
                    game.activeSkin = "characters/viking1.png";
                } else {
                    if (Inventory.gold < viking1.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= viking1.price;
                        Inventory.isViking1 = true;
                        activeImage.setDrawable(viking1.character.getDrawable());
                        game.activeSkin = "characters/viking1.png";
                        viking1.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        viking1.button.setText("USE");
                        viking1.button.setColor(Color.GREEN);
                        viking1.coin.remove();
                        game.prefs.putBoolean("isViking1", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });
        viking2 = new SellGroup(game, game.manager.get("characters/viking2.png", Texture.class),
                115, 5, Inventory.isViking2, 400);
        viking2.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(viking2.character.getDrawable());
                if (Inventory.isViking2)
                    game.activeSkin = "characters/viking2.png";
            }
        });
        viking2.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isViking2) {
                    activeImage.setDrawable(viking2.character.getDrawable());
                    game.activeSkin = "characters/viking2.png";
                } else {
                    if (Inventory.gold < viking2.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= viking2.price;
                        Inventory.isViking2 = true;
                        activeImage.setDrawable(viking2.character.getDrawable());
                        game.activeSkin = "characters/viking2.png";
                        viking2.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        viking2.button.setText("USE");
                        viking2.button.setColor(Color.GREEN);
                        viking2.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isViking2", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });
        viking3 = new SellGroup(game, game.manager.get("characters/viking3.png", Texture.class),
                225, 5, Inventory.isViking3, 600);
        viking3.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(viking3.character.getDrawable());
                if (Inventory.isViking3)
                    game.activeSkin = "characters/viking3.png";
            }
        });
        viking3.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isViking3) {
                    activeImage.setDrawable(viking3.character.getDrawable());
                    game.activeSkin = "characters/viking3.png";
                } else {
                    if (Inventory.gold < viking3.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= viking3.price;
                        Inventory.isViking3 = true;
                        activeImage.setDrawable(viking3.character.getDrawable());
                        game.activeSkin = "characters/viking3.png";
                        viking3.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        viking3.button.setText("USE");
                        viking3.button.setColor(Color.GREEN);
                        viking3.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isViking3", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });
        samurai = new SellGroup(game, game.manager.get("characters/samurai.png", Texture.class),
                5, 210, Inventory.isSamurai, 4000);
        samurai.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(samurai.character.getDrawable());
                if (Inventory.isSamurai)
                    game.activeSkin = "characters/samurai.png";
            }
        });
        samurai.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isSamurai) {
                    activeImage.setDrawable(samurai.character.getDrawable());
                    game.activeSkin = "characters/samurai.png";
                } else {
                    if (Inventory.gold < samurai.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= samurai.price;
                        Inventory.isSamurai = true;
                        activeImage.setDrawable(samurai.character.getDrawable());
                        game.activeSkin = "characters/samurai.png";
                        samurai.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        samurai.button.setText("USE");
                        samurai.button.setColor(Color.GREEN);
                        samurai.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isSamurai", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });
        ninja = new SellGroup(game, game.manager.get("characters/ninja.png", Texture.class),
                115, 210, Inventory.isNinja, 2500);
        ninja.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(ninja.character.getDrawable());
                if (Inventory.isNinja)
                    game.activeSkin = "characters/ninja.png";
            }
        });
        ninja.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isNinja) {
                    activeImage.setDrawable(ninja.character.getDrawable());
                    game.activeSkin = "characters/ninja.png";
                } else {
                    if (Inventory.gold < ninja.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= ninja.price;
                        Inventory.isNinja = true;
                        activeImage.setDrawable(ninja.character.getDrawable());
                        game.activeSkin = "characters/ninja.png";
                        ninja.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        ninja.button.setText("USE");
                        ninja.button.setColor(Color.GREEN);
                        ninja.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isNinja", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });

        ork1 = new SellGroup(game, game.manager.get("characters/ork1.png", Texture.class),
                335, 5, Inventory.isOrk1, 600);
        ork1.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(ork1.character.getDrawable());
                if (Inventory.isOrk1)
                    game.activeSkin = "characters/ork1.png";
            }
        });
        ork1.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isOrk1) {
                    activeImage.setDrawable(ork1.character.getDrawable());
                    game.activeSkin = "characters/ork1.png";
                } else {
                    if (Inventory.gold < ork1.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= ork1.price;
                        Inventory.isOrk1 = true;
                        activeImage.setDrawable(ork1.character.getDrawable());
                        game.activeSkin = "characters/ork1.png";
                        ork1.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        ork1.button.setText("USE");
                        ork1.button.setColor(Color.GREEN);
                        ork1.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isOrk1", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });

        ork2 = new SellGroup(game, game.manager.get("characters/ork2.png", Texture.class),
                335, 210, Inventory.isOrk2, 800);
        ork2.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(ork2.character.getDrawable());
                if (Inventory.isOrk2)
                    game.activeSkin = "characters/ork2.png";
            }
        });
        ork2.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isOrk2) {
                    activeImage.setDrawable(ork2.character.getDrawable());
                    game.activeSkin = "characters/ork2.png";
                } else {
                    if (Inventory.gold < ork2.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= ork2.price;
                        Inventory.isOrk2 = true;
                        activeImage.setDrawable(ork2.character.getDrawable());
                        game.activeSkin = "characters/ork2.png";
                        ork2.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        ork2.button.setText("USE");
                        ork2.button.setColor(Color.GREEN);
                        ork2.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isOrk2", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });

        ork3 = new SellGroup(game, game.manager.get("characters/ork3.png", Texture.class),
                225, 210, Inventory.isOrk3, 1000);
        ork3.character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                activeImage.setDrawable(ork3.character.getDrawable());
                if (Inventory.isOrk3)
                    game.activeSkin = "characters/ork3.png";
            }
        });
        ork3.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Inventory.isOrk3) {
                    activeImage.setDrawable(ork3.character.getDrawable());
                    game.activeSkin = "characters/ork3.png";
                } else {
                    if (Inventory.gold < ork3.price) {
                        stage.addActor(blackBG);
                        showVideoWindow("Not enough gold!\n Do you want to watch a video for 100 gold?");
                    } else {
                        if (game.isSound) game.manager.get("audio/buy.wav", Sound.class).play(.5f);
                        Inventory.gold -= ork3.price;
                        Inventory.isOrk3 = true;
                        activeImage.setDrawable(ork3.character.getDrawable());
                        game.activeSkin = "characters/ork3.png";
                        ork3.button.setStyle(game.skin.get("small", TextButton.TextButtonStyle.class));
                        ork3.button.setText("USE");
                        ork3.button.setColor(Color.GREEN);
                        ork3.coin.remove();
                        Inventory.boughtSkins++;
                        game.prefs.putInteger("boughtSkins", Inventory.boughtSkins);
                        game.prefs.putBoolean("isOrk3", true);
                        game.prefs.putInteger("gold", Inventory.gold);
                        game.prefs.flush();
                    }
                }
            }
        });
        activeImage = new Image(game.manager.get(game.activeSkin, Texture.class));
        activeImage.setBounds(stage.getWidth() / 2f - 75, 420, 150, 230);

        characters.addActor(viking1);
        characters.addActor(viking2);
        characters.addActor(viking3);
        characters.addActor(ork1);
        characters.addActor(ork2);
        characters.addActor(ork3);
        characters.addActor(ninja);
        characters.addActor(samurai);
        characters.addActor(activeImage);
        stage.addActor(characters);
//        characters.debugAll();
        characters.moveBy(0, 100);
    }

    private void showVideoWindow(final String message) {
        new Dialog("", game.skin, "def2") {
            {
                getContentTable().defaults().width(400);
                Label label = new Label(message, game.skin);
//                label.setBounds(0,0,400,300);
                label.setColor(Color.BLACK);
                label.setWrap(true);
                getContentTable().add(label).left();
                button(new TextButton("YES", game.skin, "small"), true);
                button(new TextButton("NO", game.skin, "small"), false);
                setMovable(false);
                setModal(true);
            }

            @Override
            protected void result(Object object) {
                super.result(object);
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                if ((Boolean) object) {
                    game.handler.showRewardedVideo();
                    isVideo = true;
                }
                blackBG.remove();
            }
        }.show(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
