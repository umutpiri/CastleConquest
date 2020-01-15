package com.piri.umut.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.piri.umut.ConstantValues;
import com.piri.umut.Inventory;
import com.piri.umut.MyWorld;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by umut on 2/28/18.
 */

public class MenuScreen implements Screen {
    private Stage stage;
    private MyWorld game;

    private TextButton changeControl;
    private Texture background;
    private Button soundButton, musicButton;
    private Music music;
    private Window options;
    private Label musicLabel;
    private Label soundLabel;
    private Image blackBG;
    private boolean isDialog;
    private boolean isOptions;
    private TextureRegion hero;
    private Texture enemy;
    private Image image;
    private Window tutorial;


    public MenuScreen(MyWorld game) {
        this.game = game;
        isDialog = false;
        isOptions = false;
        OrthographicCamera cam = new OrthographicCamera();
        stage = new Stage(new StretchViewport(ConstantValues.WIDTH, ConstantValues.HEIGHT, cam), game.batch);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, .6f);
        pixmap.fillRectangle(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT);
        blackBG = new Image(new Texture(pixmap));
        blackBG.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        if (Inventory.highScore <= 0)
            game.isTutorial = true;
//        stage.setDebugAll(true);
    }

    @Override
    public void show() {
        music = game.manager.get("audio/menu.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(1);
        if (game.isMusic)
            music.play();
        game.handler.showAds(true);

        background = game.manager.get("background.png", Texture.class);
        hero = new TextureRegion(game.manager.get(game.activeSkin, Texture.class));
        enemy = game.manager.get("characters/knight.png", Texture.class);
        enemy.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hero.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        title();
        initButtons();
    }

    private void title() {

        Label label = new Label("CASTLE", game.skin, "big");
        label.setPosition(stage.getWidth() / 2f - 160, stage.getHeight() / 2f + 100 + label.getHeight() / 2f);
        label.setColor(Color.CYAN);

        Label label1 = new Label("CONQUEST", game.skin, "big");
        label1.setPosition(stage.getWidth() / 2f, stage.getHeight() / 2f + 100 + label.getHeight() / 2f);
        label1.setColor(Color.CYAN);

        stage.addActor(label);
        stage.addActor(label1);
        label.addAction(sequence(moveBy(-400, 0, 0), fadeOut(0), delay(0.5f),
                parallel(moveBy(400, 0, 1, Interpolation.bounce), fadeIn(1))));
        label1.addAction(sequence(moveBy(400, 0, 0), fadeOut(0), delay(1f),
                parallel(moveBy(-400, 0, 1, Interpolation.bounce),
                        fadeIn(1))));
    }

    private void initButtons() {
        options = new Window("", game.skin, "def2");

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Button playButton = new Button(game.skin, "play");

        Button optionsButton = new Button(game.skin, "options");
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                stage.addActor(blackBG);
                stage.addActor(options);
                isOptions = true;
            }
        });

        Button exitButton = new Button(game.skin, "back");
        exitButton.setBounds(5, stage.getHeight() - 80, 75, 75);

        musicButton = new Button(game.skin, game.isMusic ? "musicOn" : "musicOff");
        musicButton.setSize(75, 75);
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                game.isMusic = !game.isMusic;
                if (game.isMusic) {
                    musicButton.setStyle(game.skin.get("musicOn", Button.ButtonStyle.class));
                    musicLabel.setText("MUSIC : ON");
                    musicLabel.setColor(Color.GREEN);
                    music.play();
                } else {
                    musicButton.setStyle(game.skin.get("musicOff", Button.ButtonStyle.class));
                    musicLabel.setText("MUSIC : OFF");
                    musicLabel.setColor(Color.RED);
                    music.pause();
                }
                game.prefs.putBoolean("isMusic", game.isMusic);
                game.prefs.flush();
            }
        });


        soundButton = new Button(game.skin, game.isSound ? "soundOn" : "soundOff");
        soundButton.pack();
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("music changed");
                game.isSound = !game.isSound;
                if (game.isSound) {
                    game.manager.get("audio/button.wav", Sound.class).play();
                    soundButton.setStyle(game.skin.get("soundOn", Button.ButtonStyle.class));
                    soundLabel.setText("SOUND : ON");
                    soundLabel.setColor(Color.GREEN);
                } else {
                    soundButton.setStyle(game.skin.get("soundOff", Button.ButtonStyle.class));
                    soundLabel.setText("SOUND : OFF");
                    soundLabel.setColor(Color.RED);
                }
                game.prefs.putBoolean("isSound", game.isSound);
                game.prefs.flush();
            }
        });

        Button shopButton = new Button(game.skin, "shop");
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                dispose();
                game.setScreen(new ShopScreen(game));
            }
        });
        Button leaderBoardButton = new Button(game.skin, "leaderboard");
        leaderBoardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                game.handler.showScore();
            }
        });

        Button closeWindow = new Button(game.skin, "close");
        closeWindow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                blackBG.remove();
                options.remove();
                isOptions = false;
            }
        });

        Button achievementsButton = new Button(game.skin, "achievement");
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.handler.showAchievement();
            }
        });

        table.defaults().pad(30);
        table.add(shopButton).size(75);
        table.add(playButton).size(75);
        table.add(optionsButton).size(75).row();
        table.add(leaderBoardButton).size(75).colspan(3).center().row();
        table.add(achievementsButton).size(75).colspan(3).center();
        table.pack();
        stage.addActor(table);
        table.moveBy(0, -80);
        stage.addActor(exitButton);

        Label title = new Label("OPTIONS", game.skin, "cartoon");
        title.setColor(Color.WHITE);
        Label title2 = new Label("ABOUT", game.skin, "cartoon");
        title2.setColor(Color.WHITE);
        musicLabel = new Label(game.isMusic ? "MUSIC : ON" : "MUSIC : OFF", game.skin, "little");
        musicLabel.setColor(game.isMusic ? Color.GREEN : Color.RED);
        soundLabel = new Label(game.isSound ? "SOUND : ON" : "SOUND : OFF", game.skin, "little");
        soundLabel.setColor(game.isSound ? Color.GREEN : Color.RED);

        options.setSize(ConstantValues.WIDTH - 50, 700);
        closeWindow.setBounds(options.getWidth() - 55,
                -20, 60, 60);
        changeControl = new TextButton("Controls : " + (game.touchControl ? "touch" : "rotate"), game.skin, "small");
        changeControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                game.touchControl = !game.touchControl;
                changeControl.setText("Control : " + (game.touchControl ? "touch" : "rotate"));
                game.prefs.putBoolean("touchControl", game.touchControl);
                game.prefs.flush();

            }
        });
        Image libgdx = new Image(game.manager.get("libgdx.png", Texture.class));
        TextButton resetData = new TextButton("Reset Game", game.skin, "small");
        resetData.pack();
        resetData.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                new Dialog("", game.skin, "def2") {
                    {
                        isDialog = true;
                        getContentTable().defaults().width(400);
                        Label label = new Label("All saved data will be deleted. Are you sure ?", game.skin);
                        label.setColor(Color.BLACK);
                        label.setWrap(true);
                        getContentTable().add(label).padLeft(20);
                        button(new TextButton("YES", game.skin, "small"), true);
                        button(new TextButton("NO", game.skin, "small"), false);
                        setModal(true);
                        setMovable(false);
                    }

                    @Override
                    protected void result(Object object) {
                        super.result(object);
                        if (game.isSound)
                            game.manager.get("audio/button.wav", Sound.class).play();
                        if ((Boolean) object) {
                            game.resetData();
                            dispose();
                            game.setScreen(new MenuScreen(game));
                        }
                        isDialog = false;
                    }
                }.show(stage);
            }
        });

        options.getTitleTable().getCells().clear();
        options.getTitleTable().add(title).expandX().padTop(3).center();
        options.getTitleTable().addActor(closeWindow);
        options.add(soundButton).size(75).padBottom(10);
        options.add(soundLabel).left().width(100).padBottom(10).row();
        options.add(musicButton).padBottom(10).size(75);
        options.add(musicLabel).left().width(100).padBottom(10).row();
        options.add(changeControl).colspan(2).padBottom(10).row();
        options.add(resetData).colspan(2).padTop(5).row();
        options.row().space(20);
        options.add(title2).colspan(2).padBottom(-5).row();
        options.add(new Label(" UMPI\n Developer : UMUT PIRI", game.skin,
                "little")).colspan(2).padTop(-5).row();
        options.add(libgdx).size(100, 40).padTop(5).left();

        options.setPosition(stage.getWidth() / 2f - options.getWidth() / 2f,
                stage.getHeight() / 2f - options.getHeight() / 2f);
        options.setModal(true);
        options.setMovable(false);

        table.addAction(sequence(moveBy(0, 400, 0), fadeOut(0),
                parallel(moveBy(0, -400, 1), fadeIn(1))));

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                music.stop();
                dispose();
                game.setScreen(new PlayScreen(game));
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                showExitDialog();
            }
        });
    }

    @Override
    public void render(float delta) {
        game.batch.setColor(1, 1, 1, 1);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        game.batch.begin();
        game.batch.draw(background, 0, 0, stage.getWidth(), stage.getHeight());
        game.batch.draw(enemy, 415, 100, 65, 0, 130, 210, 1, 1,
                30, 0, 0, 200, 276, true, false);
        game.batch.draw(hero, -110, 78, 75, 0, 150, 230, 1, 1, -30);
        game.batch.end();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (isOptions && !isDialog) {
                options.remove();
                blackBG.remove();
                isOptions = false;
            } else if (!isDialog) {
                showExitDialog();
            }
        }
    }

    private void showExitDialog() {
        isDialog = true;
        new Dialog("", game.skin, "def2") {
            {
                getContentTable().defaults().width(400);
                Label label = new Label("Do you want to quit the game?", game.skin);
                label.setColor(Color.BLACK);
                label.setWrap(true);
                getContentTable().add(label).padLeft(20);
                button(new TextButton("YES", game.skin, "small"), true);
                button(new TextButton("NO", game.skin, "small"), false);
                setModal(true);
                setMovable(false);
            }

            @Override
            protected void result(Object object) {
                super.result(object);
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                if ((Boolean) object) {
                    dispose();
                    Gdx.app.exit();
                }
                isDialog = false;
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
        game.handler.showAds(false);
        stage.dispose();
    }
}
