package com.celtican.pokemon.utils;

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
        if (!assets.contains(fileName, type))
            assets.load(fileName, type);
        return null;
    }

    public void update() {
        assets.update();
    }
    public void heavyUpdate() {
        assets.update(Game.MILLIS_PER_FRAME);
    }

    public void dispose() {
        assets.dispose();
    }
}
