package com.celtican.pokemon.screens;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Map;

public class OverworldScreen extends Screen {

    public Map map;

    public OverworldScreen() {
        map = Map.fromFileLocation("overworld/maps/sample.json");
    }

    @Override public void update() {
        map.update();
    }
    @Override public void render() {
        map.render();
    }

    @Override public void show() {
        Game.game.audio.playMusic("bgm/route29.ogg");
    }
}
