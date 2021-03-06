package com.celtican.pokemon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.celtican.pokemon.Game;

public class AssetHandler {
    private final AssetManager assets;

    public AssetHandler() {
        assets = new AssetManager();
    }

    public <T> T get(String fileName, Class<T> type) {
        if (assets.isLoaded(fileName, type))
            return assets.get(fileName, type);
        if (!assets.contains(fileName, type) && Gdx.files.internal(fileName).exists())
            assets.load(fileName, type);
        return null;
    }
    public <T> void preLoad(String fileName, Class<T> type) {
        assets.load(fileName, type);
    }
    public float getProgress() {
        return assets.getProgress();
    }

    public boolean update() {
        return assets.update();
    }
    public boolean heavyUpdate() {
        return assets.update((int)(Game.MILLIS_PER_FRAME*0.8f));
    }

    public void dispose() {
        assets.dispose();
    }
}
