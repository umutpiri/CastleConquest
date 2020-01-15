package com.piri.umut.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.piri.umut.ConstantValues;
import com.piri.umut.Inventory;
import com.piri.umut.MyWorld;

/**
 * Created by umut on 1/23/18.
 */

public class Hud implements Disposable {
    private MyWorld game;
    public Stage stage;
    private Viewport vp;
    private Label scoreBoard;
    private Label height;
    private Button musicButton, soundButton;
    private TextButton changeControl;
    private float watchX, watchY;
    private Window pause;
    private PlayScreen screen;
    private Image black;
    private Window death;
    public boolean isYes;
    public boolean isNo;
    private boolean isDeathShow;
    private Label countDown;
    private Window tutorial;

    public Hud(final MyWorld game, final PlayScreen screen) {
        this.game = game;
        this.screen = screen;
        isDeathShow = false;
        isYes = false;
        isNo = false;
        vp = new StretchViewport(ConstantValues.WIDTH, ConstantValues.HEIGHT, new OrthographicCamera());
        stage = new Stage(vp, game.batch);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, .6f);
        pixmap.fillRectangle(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT);
        black = new Image(new Texture(pixmap));
        black.setBounds(0, 0, stage.getWidth(), stage.getHeight());

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        Image coin = new Image(game.manager.get("coin2.png", Texture.class));
        coin.setBounds(5, ConstantValues.HEIGHT - 35, 25, 25);
        final Image blackBG = new Image(game.manager.get("black_background.png", Texture.class));
        blackBG.setBounds(coin.getX() - 5, coin.getY() - 10, 110, 60);
        stage.addActor(blackBG);
        stage.addActor(coin);
        height = new Label("0 m.", game.skin, "highest", Color.WHITE);
        height.setFontScale(.9f);
        scoreBoard = new Label("0", game.skin, "highest", Color.GOLD);
        scoreBoard.pack();
        scoreBoard.setPosition(coin.getX() + coin.getWidth() + 2, coin.getY() - 7);
        stage.addActor(scoreBoard);
        Image blackBG2 = new Image(game.manager.get("black_background.png", Texture.class));
        blackBG2.setBounds(ConstantValues.WIDTH / 2f - 70, coin.getY() - 10, 140, 60);
        stage.addActor(blackBG2);
        height.setPosition(ConstantValues.WIDTH / 2f - 70, coin.getY() - 5);
        stage.addActor(height);

        Button pauseButton = new Button(game.skin, "pause");
        pauseButton.setBounds(ConstantValues.WIDTH - 65, ConstantValues.HEIGHT - 65, 60, 60);
        stage.addActor(pauseButton);

        pause = new Window("", game.skin, "def2");
        Button exitButton = new Button(game.skin, "home");
        exitButton.setSize(75, 75);
        Button restartButton = new Button(game.skin, "restart");

        musicButton = new Button(game.skin, game.isMusic ? "musicOn" : "musicOff");
        musicButton.pack();
        musicButton.setSize(75, 75);
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                System.out.println("music changed");
                game.isMusic = !game.isMusic;
                if (game.isMusic) {
                    musicButton.setStyle(game.skin.get("musicOn", Button.ButtonStyle.class));
                    screen.music.play();
                } else {
                    musicButton.setStyle(game.skin.get("musicOff", Button.ButtonStyle.class));
                    screen.music.stop();
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
                    soundButton.setStyle(game.skin.get("soundOn", Button.ButtonStyle.class));
                    game.manager.get("audio/button.wav", Sound.class).play();
                }
                else
                    soundButton.setStyle(game.skin.get("soundOff", Button.ButtonStyle.class));
                game.prefs.putBoolean("isSound", game.isSound);
                game.prefs.flush();
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                screen.music.stop();
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
        });
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                if (!screen.isPause) {
                    screen.isPause = true;
                    stage.addActor(black);
                    stage.addActor(pause);
                }
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                screen.isRestart = true;
            }
        });
        changeControl = new TextButton("Controls : " + (game.touchControl ? "touch" : "rotate"), game.skin, "small");
        changeControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                game.touchControl = !game.touchControl;
                System.out.println("" + game.touchControl);
                changeControl.setText("Control : " + (game.touchControl ? "touch" : "rotate"));
                game.prefs.putBoolean("touchControl", game.touchControl);
                game.prefs.flush();

            }
        });

        Label title = new Label("PAUSED", game.skin, "cartoon");
        pause.getTitleTable().getCells().clear();
        pause.getTitleTable().add(title).expandX().padTop(3).center();
        pause.defaults().pad(15);
        pause.add(restartButton).size(75);
        pause.add(exitButton).size(75).row();
        pause.add(musicButton).size(75);
        pause.add(soundButton).size(75).row();
        pause.add(changeControl).colspan(3).padBottom(30);
        pause.pack();
        pause.setMovable(false);
        pause.setSize(ConstantValues.WIDTH - 60,
                ConstantValues.HEIGHT / 2f);
        pause.setPosition(ConstantValues.WIDTH / 2f - pause.getWidth() / 2f,
                ConstantValues.HEIGHT / 2f - pause.getHeight() / 2f);
        TextButton resumeButton = new TextButton("RESUME", game.skin, "small");
        resumeButton.pack();
        resumeButton.setPosition(pause.getWidth() / 2f - resumeButton.getWidth() / 2f, -resumeButton.getHeight() / 2f + 10);
//        resumeButton.setBounds(pause.getWidth()/2f-65,10,130,50);
        pause.addActor(resumeButton);
        pause.setClip(false);
        pause.setTransform(true);
        pause.setModal(true);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                screen.isPause = false;
                if (!screen.isStart)
                    screen.isStart = true;
                black.remove();
                pause.remove();
            }
        });

        Image watch = new Image(game.manager.get("watch3.png", Texture.class));
        watch.setBounds(5, stage.getHeight() - 130, 70, 80);
        watchX = 40;
        watchY = watch.getY() + 35;

        stage.addActor(table);
        stage.addActor(watch);

        death = new Window("", game.skin, "def2");
        death.setBounds(stage.getWidth() / 2f - 200, stage.getHeight() / 2f - 150, 400, 350);
        Label deathLabel = new Label("Do you want to watch a video to continue ?", game.skin);
        deathLabel.setWrap(true);
        TextButton yes = new TextButton("YES", game.skin, "small");
        TextButton no = new TextButton("NO", game.skin, "small");
        yes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                isYes = true;
                death.remove();
            }
        });
        no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                isNo = true;
            }
        });
        countDown = new Label("", game.skin, "cartoon");
        death.defaults().expandX();
        death.add(countDown).colspan(2).row();
        death.add(deathLabel).width(380).pad(10).colspan(2).row();
        death.add(yes).padRight(50);
        death.add(no);
        death.setMovable(false);
        death.setModal(true);

        Window.WindowStyle tutStyle = new Window.WindowStyle();
        tutStyle.titleFont = game.skin.getFont("deneme");
        tutStyle.background = new TextureRegionDrawable(new TextureRegion(game.manager.get("rules.png", Texture.class)));
        tutorial = new Window("", tutStyle);
        tutorial.setSize(ConstantValues.WIDTH - 30, ConstantValues.HEIGHT - 60);
        tutorial.setPosition(stage.getWidth() / 2f - tutorial.getWidth() / 2f, stage.getHeight() / 2f - tutorial.getHeight() / 2f);
        TextButton agree = new TextButton("Agree", game.skin, "small");
        agree.setPosition(tutorial.getWidth() - agree.getWidth() - 10, 75);
        tutorial.addActor(agree);
        tutorial.setModal(true);
        if (Inventory.highScore == 0)
            stage.addActor(tutorial);
        agree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tutorial.remove();
                screen.isPause = true;
                screen.isStart = false;
                game.isTutorial = false;
            }
        });
    }

    public void deathUpdate(int countDown) {
        if (!isDeathShow) {
            stage.addActor(death);
            isDeathShow = true;
        }
        this.countDown.setText("" + countDown);
    }

    public void update(int score, int height, float i) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (!screen.isStart) {
                screen.isStart = true;
                screen.isPause = true;
                stage.addActor(black);
                stage.addActor(pause);
            } else {
                if (screen.isPause) {
                    screen.isPause = false;
                    black.remove();
                    pause.remove();
                } else {
                    screen.isPause = true;
                    stage.addActor(black);
                    stage.addActor(pause);
                }
            }
        }
        scoreBoard.setText("" + score);
        this.height.setText(height + " m.");
        game.shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.TEAL);
        game.shapeRenderer.arc(watchX, watchY, 28, 90, -i / (997 + ((float) Inventory.watchLevel * 100)) * 360, 50);
        game.shapeRenderer.setColor(Color.CYAN);
        game.shapeRenderer.arc(watchX, watchY, 20, 90, -i / (997 + ((float) Inventory.watchLevel * 100)) * 360, 50);
        game.shapeRenderer.setColor(Color.SKY);
        game.shapeRenderer.arc(watchX, watchY, 15, 90, -i / (997 + ((float) Inventory.watchLevel * 100)) * 360, 50);
        game.shapeRenderer.end();
        this.height.pack();
        this.height.setPosition(ConstantValues.WIDTH / 2f - this.height.getWidth() / 2f, this.height.getY());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
