package com.celtican.pokemon.overworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;

public class Hitbox {
    public float x, y;
    public int width, height;

    public Hitbox() {}
    public Hitbox(Hitbox hitbox) {
        this(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
    public Hitbox(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        render(Color.RED);
    }
    public void render(Color color) {
        render(0, 0, color);
    }
    public void render(float xOff, float yOff) {
        render(xOff, yOff, Color.RED);
    }
    public void render(float xOff, float yOff, Color color) {
        Game.game.canvas.setColor(color);

        int x = MathUtils.round(this.x + xOff);
        int y = MathUtils.round(this.y + yOff);

        Game.game.canvas.drawRect(x, y-1, width, 1);
        Game.game.canvas.drawRect(x-1, y, 1, height);
        Game.game.canvas.drawRect(x, y+height, width, 1);
        Game.game.canvas.drawRect(x+width, y, 1, height);

        Game.game.canvas.resetColor();
    }

    public boolean contains(float x, float y) {
        return !(x < this.x || y < this.y || x > this.x + this.width || y > this.y + this.height);
    }
    public boolean overlaps(Hitbox h) {
        return !(h.x+h.width < x || h.y+h.height < y || x+width < h.x || y+height < h.y);
    }
}
