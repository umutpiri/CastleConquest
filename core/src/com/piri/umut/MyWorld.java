package com.piri.umut;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.piri.umut.screens.LoadingScreen;

public class MyWorld extends Game {
    public SpriteBatch batch;
    public AssetManager manager;
    public ShapeRenderer shapeRenderer;
    public Skin skin;
    public boolean isMusic, isSound;
    public String activeSkin;
    public Preferences prefs;
    public boolean touchControl;
    public AdHandler handler;
    public boolean isTutorial;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        shapeRenderer = new ShapeRenderer();
        setScreen(new LoadingScreen(this));
        skin = manager.get("skin/glassy-ui.json", Skin.class);
        skin.getFont("font").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.getFont("font-big").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.addRegions(manager.get("skin/glassy-ui.atlas", TextureAtlas.class));
        prefs = Gdx.app.getPreferences("castleConquest");
        loadData();
        handler.showAds(false);
        isTutorial = false;
        handler.signOut();
    }

    public MyWorld(AdHandler handler) {
        this.handler = handler;
    }

    private void loadData() {
        this.isMusic = prefs.getBoolean("isMusic", true);
        this.isSound = prefs.getBoolean("isSound", true);
        this.activeSkin = prefs.getString("activeSkin", "characters/viking1.png");
        this.touchControl = prefs.getBoolean("touchControl", true);
        Inventory.gold = prefs.getInteger("gold", 0);
        Inventory.highScore = prefs.getInteger("highScore", 0);
        Inventory.crewSize = prefs.getInteger("crewSize", 1);
        Inventory.isNinja = prefs.getBoolean("isNinja", false);
        Inventory.isSamurai = prefs.getBoolean("isSamurai", false);
        Inventory.isViking1 = prefs.getBoolean("isViking1", true);
        Inventory.isViking2 = prefs.getBoolean("isViking2", false);
        Inventory.isViking3 = prefs.getBoolean("isViking3", false);
        Inventory.isOrk1 = prefs.getBoolean("isOrk1", false);
        Inventory.isOrk2 = prefs.getBoolean("isOrk2", false);
        Inventory.isOrk3 = prefs.getBoolean("isOrk3", false);
        Inventory.watchLevel = prefs.getInteger("watchLevel", 0);
        Inventory.floorLevel = prefs.getInteger("floorLevel", 0);
        Inventory.dodgeLevel = prefs.getInteger("dodgeLevel", 0);
        Inventory.battleLevel = prefs.getInteger("battleLevel", 0);
        Inventory.watchFillLevel = prefs.getInteger("watchFillLevel", 0);
        Inventory.coinValueLevel = prefs.getInteger("coinValueLevel", 0);
        Inventory.cageLevel = prefs.getInteger("cageLevel", 0);
        Inventory.isFirstGiant = prefs.getBoolean("isFirstGiant", false);
        Inventory.boughtSkins = prefs.getInteger("boughtSkins", 0);
        Inventory.isGiantSlayer = prefs.getBoolean("isGiantSlayer", false);
        Inventory.isKnightSlayer = prefs.getBoolean("isKnightSlayer", false);
    }

    public void resetData() {
        prefs.clear();
        prefs.flush();
        loadData();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
        skin.dispose();
        shapeRenderer.dispose();
    }
}
