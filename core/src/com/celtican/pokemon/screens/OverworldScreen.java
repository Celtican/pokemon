package com.celtican.pokemon.screens;

import com.celtican.pokemon.overworld.Map;

public class OverworldScreen extends Screen {

    public Map map;

    public OverworldScreen() {
        map = new Map();
    }

    @Override public void update() {
        map.update();
    }
    @Override public void render() {
        map.render();
    }

}
