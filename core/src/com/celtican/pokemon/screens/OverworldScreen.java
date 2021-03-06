package com.celtican.pokemon.screens;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Map;

public class OverworldScreen extends Screen {

    public final Map map;

    public OverworldScreen() {
        if (Game.game.map == null) map = Map.fromFileLocation("overworld/maps/sample.json");
        else map = Game.game.map;
    }
    public OverworldScreen(Map map) {
        this.map = map;
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
