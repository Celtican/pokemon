package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.utils.graphics.Text;

public class TextResult extends Result {
    public final Text text;

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
        this.text = new Text(text);
    }

    @Override public void update() {
        text.update();
        if (inArray && text.isFinished()) parent.nextResult();
    }
    @Override public void render() {
        text.render();
    }

    @Override public String toString() {
        return super.toString() + " : \"" + text + "\"";
    }
}
