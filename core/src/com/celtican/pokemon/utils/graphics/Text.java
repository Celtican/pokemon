package com.celtican.pokemon.utils.graphics;

import com.celtican.pokemon.Game;

public class Text {

    public String text;
    public int frame;
    public int idleTicks;

    public Text() {
        this(null);
    }
    public Text(String text) {
        this(text, Game.TARGET_FRAME_RATE);
    }
    public Text(String text, int idleTicks) {
        this.idleTicks = idleTicks;
        setText(text);
    }

    public void update() {
//        if (frame > -idleTicks && ++frame > text.length()) frame = -Game.TARGET_FRAME_RATE;
        if (frame < -idleTicks) return;
        if (frame >= 0) {
            if (++frame > text.length()) frame = -1;
        } else frame--;
    }
    public void render() {
        if (frame != 0) Game.game.canvas.drawText(5, 25, frame > 0 ? text.substring(0, frame) : text);
    }

    public boolean isFinished() {
        return frame < -idleTicks;
    }
    public void setText(String text) {
        this.text = text;
        frame = text == null ? -idleTicks-1 : 0;
    }
}
