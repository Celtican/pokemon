package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.graphics.Color;
import com.celtican.pokemon.Game;

public class Button {
    public int x, y, width, height;

    private boolean mouseOver, mouseJustEntered, mouseJustLeft,
            mousePressed, mouseJustPressed, mouseJustUnpressed, enabled;

    public Button() {}
    public Button(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Button(int x, int y, int width, int height) {
        this(x, y);
        this.width = width;
        this.height = height;
    }
    public Button(int x, int y, int width, int height, boolean enable) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (enable)
            show();
    }

    public void updateInput(int mouseX, int mouseY, boolean isMouseDown, boolean isMouseJustDown) {
        boolean oldIsMouseOver = mouseOver;
        mouseOver = !(mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height);
        if (oldIsMouseOver != mouseOver) {
            mouseJustEntered = mouseOver;
            mouseJustLeft = !mouseOver;
        } else {
            mouseJustEntered = false;
            mouseJustLeft = false;
        }
        if (mouseOver) {
            if (isMouseDown) {
                mouseJustPressed = isMouseJustDown;
                mouseJustUnpressed = false;
                mousePressed = true;
            } else {
                mouseJustPressed = false;
                mouseJustUnpressed = mousePressed;
                mousePressed = false;
            }
        } else {
            mousePressed = false;
            mouseJustPressed = false;
            mouseJustUnpressed = false;
        }
    }
    public boolean isMouseOver() {
        return mouseOver;
    }
    public boolean isMouseJustEntered() {
        return mouseJustEntered;
    }
    public boolean isMouseJustLeft() {
        return mouseJustLeft;
    }
    public boolean isMousePressed() {
        return mousePressed;
    }
    public boolean isMouseJustPressed() {
        return mouseJustPressed;
    }
    public boolean isMouseJustUnpressed() {
        return mouseJustUnpressed;
    }
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * update() and render() are not automatically called.
     * They need to be called manually by the screen.
     */
    public void update() {
        // todo add sounds here
    }
    public void render() {
        Game.game.canvas.drawRect(x, y, width, height, Color.DARK_GRAY);
        Game.game.canvas.drawRect(x+1, y+1, width-2, height-2, Color.LIGHT_GRAY);
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
