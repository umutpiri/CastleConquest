package com.piri.umut.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.piri.umut.ConstantValues;
import com.piri.umut.Inventory;
import com.piri.umut.MyWorld;
import com.piri.umut.WorldContactListener;
import com.piri.umut.sprites.Ground;
import com.piri.umut.sprites.Player;
import com.piri.umut.sprites.enemies.Arrow;
import com.piri.umut.sprites.enemies.Commander;
import com.piri.umut.sprites.enemies.Enemy;
import com.piri.umut.sprites.enemies.Giant;
import com.piri.umut.sprites.enemies.Pawn;
import com.piri.umut.sprites.items.BluePotion;
import com.piri.umut.sprites.items.Coin;
import com.piri.umut.sprites.items.Item;
import com.piri.umut.sprites.items.Upgrade;


public class PlayScreen implements Screen {
    private World world;
    private MyWorld game;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera cam;
    private Viewport vp;

    private Ground ground;
    private Array<Ground> grounds;
    private Array<Player> players;
    private Array<Enemy> enemies;
    private Array<Arrow> arrows;
    private Array<Item> items;
    private float highest;

    private float score;
    private Texture background, background2;
    private Hud hud;
    private boolean isSlow;
    private int slowCooldown;

    private float i;
    private int watchCapacity;

    private int dificulty;
    private float bg1Y, bg2Y;
    private boolean isDebug;
    private boolean isBaseDestroyed;
    public boolean isPause;
    public boolean isStart;
    public boolean isGameOver;
    private float deathCountDown;
    private Filter filter;
    private Texture pauseBlack;
    private BitmapFont font;
    public boolean isRestart;
    public Music music;
    private Sound sound;
    private boolean videoShowedOnce;
    private int killedKnights;
    private int killedGiants;

    public PlayScreen(MyWorld game) {
        this.game = game;
        world = new World(new Vector2(0, -10f), true);
        cam = new OrthographicCamera();
        vp = new StretchViewport(ConstantValues.WIDTH / ConstantValues.PPM,
                ConstantValues.HEIGHT / ConstantValues.PPM, cam);
        world.setContactListener(new WorldContactListener());
        dificulty = 0;
        highest = 0;
        bg1Y = 0;
        i = 1;
        killedGiants = 0;
        killedKnights = 0;
        videoShowedOnce = false;
        isGameOver = false;
        deathCountDown = 5;
        isRestart = false;
        slowCooldown = 50;
        isDebug = false;
        isSlow = false;
        isBaseDestroyed = false;
        bg2Y = vp.getWorldHeight();
        filter = new Filter();
        filter.maskBits = ConstantValues.NOTHING_BIT;
        pauseBlack = game.manager.get("pause.png", Texture.class);
        watchCapacity = 1000 + Inventory.watchLevel * 100;
        font = new BitmapFont();
        font = game.skin.getFont("giant-font");
        font.setColor(Color.RED);
        font.getData().setScale(0.5f);
        sound = game.manager.get("audio/coin.wav", Sound.class);
    }

    @Override
    public void show() {
        music = game.manager.get("audio/game.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.25f);
        if (game.isMusic)
            music.play();
        background = game.manager.get("castleWall4.jpg", Texture.class);
        background2 = background;
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        isPause = true;
        isStart = false;
        createWorld();
    }

    private void createWorld() {
        ground = new Ground(game, world, 0, 0, ConstantValues.WIDTH, 20);
        Player player = new Player(game, world, ConstantValues.WIDTH / 2f / ConstantValues.PPM,
                55 / ConstantValues.PPM);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawVelocities(true);
        cam.position.set(vp.getWorldWidth() / 2f, vp.getWorldHeight() / 2f, 0);

        grounds = new Array<Ground>();
        players = new Array<Player>();
        enemies = new Array<Enemy>();
        arrows = new Array<Arrow>();
        items = new Array<Item>();
        players.add(player);
        for (int i = 1; i < 10; i++) {
            Ground ground = new Ground(game, world, MathUtils.random(0,
                    (ConstantValues.WIDTH - 399 - Inventory.floorLevel) > 0 ?
                            (ConstantValues.WIDTH - 399 - Inventory.floorLevel) : 0),
                    i * 100, 400 + Inventory.floorLevel, 12);
            grounds.add(ground);
            if (i == 1 || i == 7)
                items.add(new Upgrade(game, MathUtils.random(50 / ConstantValues.PPM,
                        (ConstantValues.WIDTH - 50) / ConstantValues.PPM), i + 0.3f));
            if (i == 5 || i == 9)
                items.add(new Coin(game, vp.getWorldWidth() / 2f, i + 0.3f));
            if (i == 2 || i == 7 || i == 6)
                enemies.add(new Pawn(game, world, ground));
        }
        score = 0;
        hud = new Hud(game, this);
    }

    @Override
    public void render(float delta) {
        game.batch.setColor(Color.WHITE);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!isPause) {
            if (!isSlow)
                world.step(delta, 6, 2);
            else
                world.step(delta / 2f, 6, 2);
        }
        game.batch.begin();
        game.batch.setProjectionMatrix(cam.combined);
        draw();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        for (Enemy enemy : enemies) {
            if (enemy instanceof Giant) {
                if (players.size >= 8)
                    font.setColor(Color.GREEN);
                else
                    font.setColor(Color.RED);
                font.draw(game.batch, players.size + "/8", (enemy.getX() + enemy.getWidth() / 2f) * 100,
                        (enemy.getY() - cam.position.y + vp.getWorldHeight() / 2f) * 100 + font.getCapHeight());
            }
        }
        game.batch.end();
        if (isDebug)
            b2dr.render(world, cam.combined);
        hud.stage.act(delta);
        hud.stage.draw();
        update(delta);
    }

    private void draw() {
        drawBackground();
        if (highest < 5)
            ground.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        for (Enemy enemy : enemies)
            enemy.draw(game.batch);
        for (Ground ground : grounds)
            ground.draw(game.batch);
        for (Arrow arrow : arrows)
            arrow.draw(game.batch);
        for (Player player : players)
            player.draw(game.batch);
        if (!isStart)
            game.batch.draw(pauseBlack, cam.position.x - vp.getWorldWidth() / 2f, cam.position.y - vp.getWorldHeight() / 2f,
                    vp.getWorldWidth(), vp.getWorldHeight());
    }

    private void update(float delta) {
        if (!isGameOver) {
            if (!game.isTutorial)
                if (isPause && !isStart && Gdx.input.justTouched()) {
                    isPause = false;
                    isStart = true;
                }
            if (!isPause || !isStart) {
                updateGround();
                updatePlayer();
            }
            updateEnemy(delta);
            updateWorld(delta);
            if (isRestart) {
                dispose();
                game.setScreen(new PlayScreen(game));
            }
        } else {
            isPause = true;
            if (!hud.isYes) deathCountDown -= delta;
            if (deathCountDown < 1) {
                dispose();
                game.setScreen(new GameOverScreen(game, (int) score, highest));
            } else {
                hud.deathUpdate((int) deathCountDown);
                if (hud.isNo) {
                    dispose();
                    game.setScreen(new GameOverScreen(game, (int) score, highest));
                } else if (hud.isYes) {
                    if (!videoShowedOnce) game.handler.showRewardedVideo();
                    videoShowedOnce = true;
                    if (game.handler.isClosed()) {
                        if (game.handler.isRewardEarned()) {
                            isGameOver = false;
                            players.add(new Player(game, world, ConstantValues.WIDTH / 2f / ConstantValues.PPM, highest));
                            deathCountDown = 0;
                            isPause = true;
                            isStart = false;
                            hud.isYes = false;
                            hud.isNo = false;
                            for (Enemy enemy : enemies) {
                                enemy.death();
                            }
                            enemies.clear();
                            for (Arrow arrow : arrows)
                                arrow.death();
                            arrows.clear();
                            isPause = false;
                        } else {
                            dispose();
                            game.setScreen(new GameOverScreen(game, (int) score, highest));
                        }
                    } else if (game.handler.isFailed()) {
                        dispose();
                        game.setScreen(new GameOverScreen(game, (int) score, highest));
                    }
                }
            }
        }
    }

    private void updateEnemy(float delta) {
        for (Enemy enemy : enemies) {
            if (enemy.readyToDie || enemy.getY() + enemy.getHeight() <= cam.position.y - vp.getWorldHeight() / 2f) {
                if (enemy.readyToDie) {
                    Coin coin = new Coin(game, enemy.getX(), enemy.getY());
                    items.add(coin);
                    coin.isTaken = true;
                    enemy.death();
                    if (enemy instanceof Giant) {
                        score += 5f;
                        killedGiants++;
                        if (!Inventory.isFirstGiant) {
                            if (game.handler.isSignedIn()) {
                                game.handler.unlockAchievement("CgkIxLe0nq0MEAIQBw");
                                Inventory.isFirstGiant = true;
                                game.prefs.putBoolean("isFirstGiant", true);
                                game.prefs.flush();
                            }
                        }
                        if (!Inventory.isGiantSlayer && killedGiants >= 7) {
                            if (game.handler.isSignedIn()) {
                                game.handler.unlockAchievement("CgkIxLe0nq0MEAIQCQ");
                                Inventory.isGiantSlayer = true;
                                game.prefs.putBoolean("isGiantSlayer", true);
                                game.prefs.flush();
                            }
                        }
                    } else if (enemy instanceof Commander) {
                        killedKnights++;
                        score += 2f;
                        if (!Inventory.isKnightSlayer && killedKnights >= 20) {
                            if (game.handler.isSignedIn()) {
                                game.handler.unlockAchievement("CgkIxLe0nq0MEAIQCA");
                                Inventory.isKnightSlayer = true;
                                game.prefs.putBoolean("isKnightSlayer", true);
                                game.prefs.flush();
                            }
                        }
                    } else
                        score += .5f;
                    if (game.isSound) {
                        if (enemy instanceof Pawn)
                            game.manager.get("audio/pawn.wav", Sound.class).play(.5f);
                        else if (enemy instanceof Commander)
                            game.manager.get("audio/commander3.wav", Sound.class).play(.35f);
                        else if (enemy instanceof Giant)
                            game.manager.get("audio/giant.wav", Sound.class).play(.5f);
                    }
                }
                enemies.removeValue(enemy, true);
            } else
                enemy.update(delta);
        }
        for (Arrow arrow : arrows) {
            if (arrow.readyToDie || arrow.getY() + arrow.getHeight() <= cam.position.y - vp.getWorldHeight() / 2f) {
                arrow.death();
                arrows.removeValue(arrow, true);
            } else
                arrow.update();
        }
    }

    private void drawBackground() {
        if (bg1Y <= cam.position.y - 3 * vp.getWorldHeight() / 2f)
            bg1Y += vp.getWorldHeight() * 2;
        if (bg2Y <= cam.position.y - 3 * vp.getWorldHeight() / 2f)
            bg2Y += vp.getWorldHeight() * 2;
        game.batch.draw(background, 0, bg1Y,
                vp.getWorldWidth(), vp.getWorldHeight());
        game.batch.draw(background2, 0, bg2Y,
                vp.getWorldWidth(), vp.getWorldHeight(), 0, 0,
                background2.getWidth(), background2.getHeight(), false, true);
    }

    private void updateGround() {
        for (Ground ground : grounds) {
            ground.update();

            if (cam.position.y - vp.getWorldHeight() / 2f >= ground.getY() + ground.getHeight()) {
                ground.reallocate();
                dificulty++;
                int random = MathUtils.random(1, 1000);
                if (random < dificulty + 200 && !(ground.getWidth() > 3.85f))
                    ground.move();
                //UPGRADE
                if (random > 800) {
                    items.add(new Upgrade(game, MathUtils.random(20, ConstantValues.WIDTH - 20) / ConstantValues.PPM,
                            ground.getY() + 0.3f));
                }
                //PAWN
                if (random > 375 && random < 700 + dificulty) {
                    enemies.add(new Pawn(game, world, ground));
                }
                //ARROWS
                if (random < 140 + dificulty / 2f)
                    arrows.add(new Arrow(game, world, MathUtils.random(10, ConstantValues.WIDTH - 10) / ConstantValues.PPM,
                            highest + 5 + random / 100f));
                if (random < dificulty / 3f)
                    arrows.add(new Arrow(game, world, MathUtils.random(10, ConstantValues.WIDTH - 10) / ConstantValues.PPM,
                            highest + 5 + random / 100f));
                //COMMANDER
                if (random > 325 && random < 475 + dificulty)
                    enemies.add(new Commander(game, world, ground));
                //COIN
                if (random > 600 && random < 850 + dificulty)
                    items.add(new Coin(game, MathUtils.random(30, ConstantValues.WIDTH - 30) / ConstantValues.PPM,
                            ground.getY() + 0.5f));
                if (random > 600 && random < 630)
                    items.add(new Coin(game, MathUtils.random(30, ConstantValues.WIDTH - 30) / ConstantValues.PPM,
                            ground.getY() + 0.5f));
                if (random > 40 && random < 70)
                    items.add(new Coin(game, MathUtils.random(30, ConstantValues.WIDTH - 30) / ConstantValues.PPM,
                            ground.getY() + 0.5f));
                //POTION
                if (random > 290 && random < 305)
                    items.add(new BluePotion(game, MathUtils.random(30, ConstantValues.WIDTH - 30) / ConstantValues.PPM,
                            ground.getY() + 0.2f));
                //GIANTS
                if (highest > 30) {
                    if (random > 75 && random < 125 + dificulty / 5f)
                        enemies.add(new Giant(game, world, ground));
                }
//                System.out.println("difficulty : " + dificulty + "    score : " + score + "    random : " + random);
            }

            if (ground.isMoving && ((ground.getX() <= 0 && ground.moveSpeed < 0) ||
                    (ground.getX() + ground.getWidth() >= ConstantValues.WIDTH / ConstantValues.PPM && ground.moveSpeed > 0)))
                ground.reverse();
        }
        if (!isBaseDestroyed && highest > 5) {
            world.destroyBody(ground.body);
            isBaseDestroyed = true;
        }
    }

    private void updatePlayer() {
//        long startTime = System.nanoTime();
        for (Player player : players) {
            player.setPosition(player.getBody().getPosition().x - player.getWidth() / 2f,
                    player.getBody().getPosition().y - player.getHeight() / 2f);
            if (player.readyToDie) {
                for (Fixture fixture : player.getBody().getFixtureList())
                    fixture.setFilterData(filter);
            } else {
                if (player.jumpTimeOut > 0 && !isPause)
                    player.jumpTimeOut--;

                if (player.getBody().getPosition().x > ConstantValues.WIDTH / ConstantValues.PPM ||
                        player.getBody().getPosition().x < 0)
                    player.getBody().setTransform((player.getBody().getPosition().x +
                                    ConstantValues.WIDTH / ConstantValues.PPM) % (ConstantValues.WIDTH / ConstantValues.PPM),
                            player.getBody().getPosition().y, 0);

                if (player.getY() > highest)
                    highest = player.getY();

                for (Item item : items) {
                    if (player.getBoundingRectangle().overlaps(item.rectangle)) {
                        if (item instanceof Coin) {
                            if (!((Coin) item).isTaken) {
                                ((Coin) item).isTaken = true;
                                score += 1 + Inventory.coinValueLevel / 20f;
                                if (game.isSound) sound.play(.4f);
                            }
                        } else if (item instanceof Upgrade) {
                            items.removeValue(item, true);
                            players.add(new Player(game, world, item.getX(),
                                    item.getY() + item.getHeight()));
                            Inventory.crewSize++;
                            if (MathUtils.random(0, 100) < Inventory.cageLevel) {
                                players.add(new Player(game, world, item.getX() + 0.1f,
                                        item.getY() + item.getHeight() + 0.06f));
                                Inventory.crewSize++;
                            }
                            if (game.isSound)
                                game.manager.get("audio/unlock.wav", Sound.class).play();
                        } else if (item instanceof BluePotion) {
                            items.removeValue(item, true);
                            i = watchCapacity;
                            if (game.isSound)
                                game.manager.get("audio/bubble.wav", Sound.class).play(.5f);
                        }
                    }
                }

                if (player.collideCount > 0 && player.jumpTimeOut < 1) {
                    player.getBody().applyLinearImpulse(new Vector2(0, 3.5f), player.getBody().getWorldCenter(), true);
                    player.jumpTimeOut = 40 - (highest - player.getY()) * 3;
                }
                player.getBody().setLinearDamping((highest - player.getY()) / 3f + 1f);

                if (!isPause) {
                    if (game.touchControl) {
                        if (Gdx.input.isTouched()) {
                            if (Gdx.input.getX() >= vp.getScreenWidth() / 2f && player.getBody().getLinearVelocity().x <= 2.5f) {
                                player.getBody().applyLinearImpulse(new Vector2(0.15f, 0),
                                        player.getBody().getWorldCenter(), true);
                                if (player.isFlipX())
                                    player.flip(true, false);
                            } else if (Gdx.input.getX() <= vp.getScreenWidth() / 2f && player.getBody().getLinearVelocity().x >= -2.5f) {
                                player.getBody().applyLinearImpulse(new Vector2(-0.15f, 0),
                                        player.getBody().getWorldCenter(), true);
                                if (!player.isFlipX())
                                    player.flip(true, false);
                            }
                        }
                    } else {
                        player.getBody().applyLinearImpulse(new Vector2(Gdx.input.getRoll() / 250f, 0),
                                player.getBody().getWorldCenter(), true);
                        if (player.getBody().getLinearVelocity().x < 0 && !player.isFlipX())
                            player.flip(true, false);
                        if (player.getBody().getLinearVelocity().x > 0 && player.isFlipX())
                            player.flip(true, false);
                        if (player.getBody().getLinearVelocity().x > 2.8f)
                            player.getBody().setLinearVelocity(2.5f, player.getBody().getLinearVelocity().y);
                        if (player.getBody().getLinearVelocity().x < -2.8f)
                            player.getBody().setLinearVelocity(-2.8f, player.getBody().getLinearVelocity().y);
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getBody().getLinearVelocity().x >= -2.5f) {
                    player.getBody().applyLinearImpulse(new Vector2(-0.15f, 0),
                            player.getBody().getWorldCenter(), true);
                    if (!player.isFlipX())
                        player.flip(true, false);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
                    player.setRegion(game.manager.get("viking.png", Texture.class));
                if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
                    player.setRegion(game.manager.get("viking2.png", Texture.class));
                if (Gdx.input.isKeyPressed(Input.Keys.NUM_3))
                    player.setRegion(game.manager.get("viking3.png", Texture.class));
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getBody().getLinearVelocity().x <= 2.5f) {
                    player.getBody().applyLinearImpulse(new Vector2(0.15f, 0),
                            player.getBody().getWorldCenter(), true);
                    if (player.isFlipX())
                        player.flip(true, false);
                }
                if (player.getBody().getLinearVelocity().y > 6f)
                    player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 6f);
            }
            if (player.getBody().getPosition().y <= cam.position.y - vp.getWorldHeight() / 2f) {
                player.destroyBody();
                players.removeValue(player, true);
            }
        }
//        System.out.println(System.nanoTime()-startTime);
    }

    private void updateWorld(float delta) {
        hud.update((int) score, (int) highest, i);
        if (!isPause) {
            if (slowCooldown > 0)
                slowCooldown--;
            if (highest + 100 / ConstantValues.PPM >= cam.position.y)
                cam.position.y += (highest + 100 / ConstantValues.PPM - cam.position.y) / 20f;
            if (Gdx.input.isTouched(1) && slowCooldown == 0 && i > 10) {
                isSlow = !isSlow;
                slowCooldown = 40;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                isSlow = !isSlow;
            }
            if (i < 10 && isSlow) {
                isSlow = false;
                slowCooldown = 40;
            }
            if (isSlow)
                i -= 4;
            if (!isSlow && i < watchCapacity - .5f && slowCooldown == 0)
                i += .4f * (Inventory.watchFillLevel / 4f + 1);
            if (i > watchCapacity)
                i = watchCapacity;
            if (i < 0)
                i = 0;
        }
        for (Item item : items) {
            item.update(delta);
            if (item.getY() + item.getHeight() <= cam.position.y - vp.getWorldHeight() / 2f)
                items.removeValue(item, true);
            else if (item instanceof Coin) {
                if (((Coin) item).alpha < 0.1f)
                    items.removeValue(item, true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            cam.zoom += 0.02f;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            cam.zoom -= 0.02f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            isDebug = !isDebug;
//            cam.rotate(0.5f,0,0,1);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            cam.rotate(-0.5f, 0, 0, 1);
        cam.update();
        Inventory.isGreaterThan8 = players.size >= 8;
        if (players.size <= 0) {
            isGameOver = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        vp.update(width, height, false);
        hud.stage.getViewport().update(width, height, false);
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
        music.stop();
        b2dr.dispose();
        world.dispose();
        grounds.clear();
        players.clear();
        enemies.clear();
        arrows.clear();
        items.clear();
        font.dispose();
        hud.dispose();
    }
}
