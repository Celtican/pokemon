package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;

public class SoundResult extends Result {

    private final String filePath;

    public SoundResult(String filePath) {
        this.filePath = filePath;
    }

    @Override public boolean start() {
        Game.game.audio.playSound(filePath);
        return true;
    }
}
