package com.piri.umut.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.piri.umut.ConstantValues;
import com.piri.umut.Inventory;
import com.piri.umut.MyWorld;


public class GameOverScreen implements Screen {
    private final MyWorld game;
    private Stage stage;
    private Texture background;

    public GameOverScreen(final MyWorld game, int gold, float peak) {
        this.game = game;
        int score = (int) peak;
        Inventory.gold += gold;
        boolean isNewHighScore = false;
        game.prefs.putInteger("crewSize", Inventory.crewSize);
        game.prefs.putInteger("gold", Inventory.gold);
        if (Inventory.crewSize >= 200 && game.handler.isSignedIn())
            game.handler.unlockAchievement("CgkIxLe0nq0MEAIQAg ");
        if (Inventory.highScore < score) {
            Inventory.highScore = score;
            game.prefs.putInteger("highScore", score);
            isNewHighScore = true;
            game.manager.get("audio/win.ogg", Sound.class).play(.7f);
            if (game.handler.isSignedIn()) {
                game.handler.submitScore(score);
                if (score >= 100)
                    game.handler.unlockAchievement("CgkIxLe0nq0MEAIQBQ");
                if (score >= 250)
                    game.handler.unlockAchievement("CgkIxLe0nq0MEAIQBg");
            }
        } else {
            game.manager.get("audio/lose.ogg", Sound.class).play(.7f);
        }
        game.prefs.flush();
        OrthographicCamera camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(ConstantValues.WIDTH, ConstantValues.HEIGHT, camera), game.batch);
        Gdx.input.setInputProcessor(stage);
        background = game.manager.get("gameOver.png", Texture.class);


        Image coin = new Image(game.manager.get("coinbag.png", Texture.class));
        coin.setSize(50, 50);
        coin.setPosition(10, stage.getHeight() - coin.getHeight() - 10);

        Label goldLabel = new Label(" " + gold, game.skin, "big");
        goldLabel.setFontScale(.7f);
        goldLabel.pack();
        goldLabel.setColor(Color.GOLD);
        goldLabel.setPosition(coin.getWidth() + coin.getX(), coin.getY() - 5);

        Label textLabel = new Label("GAME OVER", game.skin, "big");
        textLabel.setPosition(stage.getWidth() / 2f - textLabel.getWidth() / 2f,
                stage.getHeight() / 2f - textLabel.getHeight() / 2f);

        Label highScoreLabel = new Label(isNewHighScore ? "" : "High Score: " + Inventory.highScore + "m.", game.skin, "big");
        highScoreLabel.setFontScale(.7f);
        highScoreLabel.pack();
        highScoreLabel.setPosition(stage.getWidth() / 2f - highScoreLabel.getWidth() / 2f,
                textLabel.getY() + textLabel.getHeight() + 20);
        Label scoreLabel = new Label(isNewHighScore ? "NEW HIGH SCORE" :
                "Your score : " + score + "m. ", game.skin, "big");
        if (isNewHighScore) scoreLabel.setColor(Color.PURPLE);
        scoreLabel.pack();
        scoreLabel.setPosition(stage.getWidth() / 2f - scoreLabel.getWidth() / 2f,
                highScoreLabel.getY() + highScoreLabel.getHeight() + 50);
        Label scoreLabel2 = new Label("" + score, game.skin, "big");
        scoreLabel2.setColor(Color.PURPLE);
        scoreLabel2.pack();
        scoreLabel2.setPosition(stage.getWidth() / 2f - scoreLabel2.getWidth() / 2f,
                scoreLabel.getY() - scoreLabel2.getHeight() - 7);
        if (isNewHighScore) stage.addActor(scoreLabel2);


        Button restartButton = new Button(game.skin, "restart");
        restartButton.setSize(75, 75);
        restartButton.setPosition(textLabel.getX() + textLabel.getWidth() / 2f - restartButton.getWidth() - 20,
                textLabel.getY() - restartButton.getHeight() - 10);

        Button homeButton = new Button(game.skin, "home");
        homeButton.setSize(75, 75);
        homeButton.setPosition(restartButton.getX() + restartButton.getWidth() + 30, restartButton.getY());

        stage.addActor(highScoreLabel);
        stage.addActor(goldLabel);
        stage.addActor(coin);
        stage.addActor(scoreLabel);
        stage.addActor(textLabel);
        stage.addActor(homeButton);
        stage.addActor(restartButton);
//        stage.setDebugAll(true);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.isSound)
                    game.manager.get("audio/button.wav", Sound.class).play();
                dispose();
                game.setScreen(new PlayScreen(game));
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        game.batch.begin();
        game.batch.draw(background, 0, 0, stage.getWidth(), stage.getHeight());
        game.batch.end();
        stage.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

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
