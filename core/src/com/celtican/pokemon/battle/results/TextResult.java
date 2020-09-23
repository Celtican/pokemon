package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;

public class TextResult extends Result {
    private final String text;

    private int progress;

    public TextResult(String text) {
        this.text = text;
    }

    @Override public void update() {
        if (++progress > text.length())
            progress = - Game.TARGET_FRAME_RATE;
        if (progress == 0)
            parent.nextResult();
    }
    @Override public void render() {
        Game.game.canvas.drawText(2, 2, progress > 0 ? text.substring(0, progress) : text);
    }
}
