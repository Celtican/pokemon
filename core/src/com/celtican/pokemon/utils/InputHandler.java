package com.celtican.pokemon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Button;
import com.celtican.pokemon.utils.data.Vector2Int;

public class InputHandler {

    private final Array<Button> buttons;
    private final Vector2Int mouse;

    public InputHandler() {
        buttons = new Array<>();
        mouse = new Vector2Int();
    }

    public void update() {
        mouse.x = Gdx.input.getX() / Game.PIXEL_SIZE;
        mouse.y = (Gdx.graphics.getHeight()-Gdx.input.getY())/Game.PIXEL_SIZE;
        boolean mousePressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean mouseJustPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        for (Button button : buttons)
            button.updateInput(mouse.x, mouse.y, mousePressed, mouseJustPressed);
    }

    public void addButton(Button button) {
        buttons.add(button);
    }
    public void removeButton(Button button) {
        buttons.removeValue(button, true);
    }
}
