package com.celtican.pokemon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Button;

public class InputHandler {

    public final Array<Button> buttons;
    private final Vector2Int mouse;

    public InputHandler() {
        buttons = new Array<>();
        mouse = new Vector2Int();
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) Game.game.isFastForward = !Game.game.isFastForward;

        Array<Button> buttons = new Array<>(this.buttons);
        int newX = Gdx.input.getX() / Game.game.pixelSize;
        int newY = (Gdx.graphics.getHeight()-Gdx.input.getY())/Game.game.pixelSize;
        boolean mousePressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (newX != mouse.x || newY != mouse.y || mousePressed) {
            // update buttons with mouse pos
            mouse.x = newX;
            mouse.y = newY;
            buttons.forEach(button -> {
                boolean isMouseOver = !(newX < button.x || newY < button.y ||
                        newX >= button.x + button.width || newY >= button.y + button.height);
                if (button.justSelected)   button.justSelected = false;
                if (button.justUnselected) button.justUnselected = false;
                if (isMouseOver) {
                    if (!button.isSelected()) {
                        button.justSelected = true;
                        button.selected = true;
                        button.hover();
                    }
                    if (mousePressed)
                        button.clicked();
                } else if (button.isSelected()) {
                    button.justUnselected = true;
                    button.selected = false;
                    button.leave();
                }
            });
        } else {
            // update buttons with keyboard buttons
            Button selectedButton = null;
            for (int i = 0; i < buttons.size; i++) {
                Button button = buttons.get(i);
                if (button.justSelected)   button.justSelected = false;
                if (button.justUnselected) button.justUnselected = false;
                if (button.isSelected()) selectedButton = button;
            }
            if (selectedButton != null) {
                Button newButton = Gdx.input.isKeyJustPressed(Input.Keys.W) ? selectedButton.upButton :
                        Gdx.input.isKeyJustPressed(Input.Keys.A) ? selectedButton.leftButton :
                        Gdx.input.isKeyJustPressed(Input.Keys.S) ? selectedButton.downButton :
                        Gdx.input.isKeyJustPressed(Input.Keys.D) ? selectedButton.rightButton : null;
                if (newButton != null) {
                    selectedButton.selected = false;
                    selectedButton.justUnselected = true;
                    newButton.justSelected = true;
                    newButton.selected = true;
                    selectedButton.leave();
                    newButton.hover();
                }
            } else if ((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A) ||
                        Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D) ||
                        Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) &&
                        buttons.notEmpty()) {
                selectedButton = buttons.first();
                selectedButton.justSelected = true;
                selectedButton.selected = true;
                selectedButton.hover();
            }
            if (selectedButton != null && Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                selectedButton.clicked();
        }
    }

    public int getX() {
        return mouse.x;
    }
    public int getY() {
        return mouse.y;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }
    public void removeButton(Button button) {
        buttons.removeValue(button, true);
    }
}
