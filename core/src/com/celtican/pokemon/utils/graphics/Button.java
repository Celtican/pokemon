package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.Color;
import com.celtican.pokemon.Game;

public class Button {
    public int x, y, width, height;
    public Button upButton, rightButton, downButton, leftButton;

//    private boolean mouseOver, mouseJustEntered, mouseJustLeft,
//            mousePressed, mouseJustPressed, mouseJustUnpressed, enabled;
    public boolean enabled, selected, justSelected;

    public Button() {}
    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public Button(int x, int y, int width, int height, boolean show) {
        this(x, y, width, height);
        if (show)
            show();
    }

    public void clicked() {}
    public void hover() {}
    public void leave() {}
    public void render() {
        if (!enabled)
            return;
        Game.game.canvas.drawRect(x, y, width, height, selected ? new Color(0.5f, 1, 1, 1) : Color.DARK_GRAY);
        Game.game.canvas.drawRect(x+1, y+1, width-2, height-2, justSelected ? Color.WHITE : Color.LIGHT_GRAY);
    }

    public boolean isSelected() {
        return selected;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void show() {
        if (enabled)
            return;
        Game.game.input.addButton(this);
        enabled = true;
    }
    public void hide() {
        if (!enabled)
            return;
        Game.game.input.removeButton(this);
        enabled = false;
    }
}
