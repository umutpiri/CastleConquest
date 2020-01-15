package com.piri.umut.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.piri.umut.ConstantValues;
import com.piri.umut.MyWorld;

/**
 * Created by umut on 3/3/18.
 */

public class LoadingScreen implements Screen {
    private MyWorld game;
    private Stage stage;
    private OrthographicCamera cam;
    private Texture texture, bounds;

    public LoadingScreen(MyWorld game) {
        this.game = game;
        cam = new OrthographicCamera();
        stage = new Stage(new StretchViewport(ConstantValues.WIDTH, ConstantValues.HEIGHT, cam), game.batch);
        texture = new Texture("black.png");
        bounds = new Texture("bounds.png");
        game.manager.load("skin/glassy-ui.atlas", TextureAtlas.class);
        game.manager.load("skin/glassy-ui.json", Skin.class);
        game.manager.load("umpiLogo3.png", Texture.class);
        game.manager.finishLoading();
    }

    @Override
    public void show() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(.4f, .4f, .4f, 1f);
        pixmap.fillRectangle(0, 0, ConstantValues.WIDTH, ConstantValues.HEIGHT);
        Image grayBG = new Image(new Texture(pixmap));
        Image logo = new Image(game.manager.get("umpiLogo3.png", Texture.class));
        logo.setBounds(ConstantValues.WIDTH / 2f - 75, ConstantValues.HEIGHT - 450, 150, 230);
        grayBG.setBounds(0, logo.getY() - 15, ConstantValues.WIDTH, 200);
//        stage.addActor(grayBG);
        stage.addActor(logo);
        logo.addAction(Actions.sequence(Actions.fadeOut(0), Actions.fadeIn(3)));
        game.manager.load("audio/menu.mp3", Music.class);
        game.manager.load("audio/game.mp3", Music.class);
        game.manager.load("audio/arrow.ogg", Sound.class);
        game.manager.load("audio/win.ogg", Sound.class);
        game.manager.load("audio/lose.ogg", Sound.class);
        game.manager.load("audio/coin.wav", Sound.class);
        game.manager.load("audio/buy.wav", Sound.class);
        game.manager.load("audio/pawn.wav", Sound.class);
        game.manager.load("audio/commander3.wav", Sound.class);
        game.manager.load("audio/giant.wav", Sound.class);
        game.manager.load("audio/bubble.wav", Sound.class);
        game.manager.load("audio/button.wav", Sound.class);
        game.manager.load("audio/unlock.wav", Sound.class);
        game.manager.load("arrow.png", Texture.class);
        game.manager.load("plank.png", Texture.class);
        game.manager.load("libgdx.png", Texture.class);
        game.manager.load("castleWall4.jpg", Texture.class);
        game.manager.load("pause.png", Texture.class);
        game.manager.load("coin1.png", Texture.class);
        game.manager.load("coin2.png", Texture.class);
        game.manager.load("coin3.png", Texture.class);
        game.manager.load("coin4.png", Texture.class);
        game.manager.load("coinbag.png", Texture.class);
        game.manager.load("goldbar.9.png", Texture.class);
        game.manager.load("blue_pot1.png", Texture.class);
        game.manager.load("background.png", Texture.class);
        game.manager.load("rules.png", Texture.class);
        game.manager.load("gameOver.png", Texture.class);
        game.manager.load("watch3.png", Texture.class);
        game.manager.load("fillSkill.png", Texture.class);
        game.manager.load("watchSkill.png", Texture.class);
        game.manager.load("black_background.png", Texture.class);
        game.manager.load("characters/viking1.png", Texture.class);
        game.manager.load("characters/viking2.png", Texture.class);
        game.manager.load("characters/viking3.png", Texture.class);
        game.manager.load("characters/ninja.png", Texture.class);
        game.manager.load("characters/samurai.png", Texture.class);
        game.manager.load("characters/ork1.png", Texture.class);
        game.manager.load("characters/ork2.png", Texture.class);
        game.manager.load("characters/ork3.png", Texture.class);
        game.manager.load("characters/knight_idle.png", Texture.class);
        game.manager.load("characters/knight.png", Texture.class);
        game.manager.load("characters/giant_idle.png", Texture.class);
        game.manager.load("characters/ork1_idle.png", Texture.class);
        game.manager.load("characters/little_knight_walk.png", Texture.class);
        game.manager.load("sellbg.png", Texture.class);
        game.manager.load("sellbgh.png", Texture.class);
        game.manager.load("shopbg.png", Texture.class);
        game.manager.load("cage.png", Texture.class);
        game.manager.load("floorSkill.png", Texture.class);
        game.manager.load("coinSkill.png", Texture.class);
        game.manager.load("dodgeSkill.png", Texture.class);
        game.manager.load("cageSkill.png", Texture.class);
        game.manager.load("battleSkill.png", Texture.class);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        game.batch.begin();
        game.batch.draw(bounds, ConstantValues.WIDTH / 2f - 200, 200, 400, 34);
        game.batch.draw(texture, ConstantValues.WIDTH / 2f - 200 + 3, 202, game.manager.getProgress() * 400 - 6, 30);
        game.batch.end();

        if (game.manager.update()) {
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
