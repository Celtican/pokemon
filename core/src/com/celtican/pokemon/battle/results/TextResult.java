package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;

public class TextResult extends Result {
    private String text;
    private int progress;

    public TextResult() {
        this(null, true);
    }
    public TextResult(boolean addToArray) {
        this(null, addToArray);
    }
    public TextResult(String text) {
        this(text, true);
    }
    public TextResult(String text, boolean addToArray) {
        super(addToArray);
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        progress = text == null ? -1 : 0;
    }

    public boolean isFinished() {
        return progress == -1;
    }

    @Override public void update() {
        if (progress == -1) {
            if (inArray) parent.nextResult();
        } else if (++progress > text.length()) progress = -Game.TARGET_FRAME_RATE;
    }
    @Override public void render() {
        if (progress != 0) Game.game.canvas.drawText(5, 25, progress > 0 ? text.substring(0, progress) : text);
    }

    @Override public String toString() {
        return super.toString() + " : \"" + text + "\"";
    }
}
