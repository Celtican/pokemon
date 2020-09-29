package com.celtican.pokemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Button;

public class TitleScreen extends Screen {
    private final Array<Button> buttons;

    public TitleScreen() {
        buttons = new Array<>();
        addButton("Exit", () -> Gdx.app.exit());
        if (Game.game.isDebug) {
            addButton("Edit Tileset", () -> Game.game.switchScreens(new EditTilesetScreen()));
            addButton("Edit Map", () -> Game.game.switchScreens(new EditMapScreen()));
            addButton("Battle!", () -> Game.game.switchScreens(new BattleScreen()));
        }
        addButton("Play", () -> Game.game.switchScreens(new OverworldScreen()));
    }

    private void addButton(String text, Runnable runnable) {
        Vector2Int dimensions = Game.game.canvas.getBoundsOfText(text);
        Button button = new Button(0, 0, dimensions.x+4, dimensions.y+4) {
            @Override public void clicked() {
                if (runnable != null)
                    runnable.run();
            }
            @Override public void render() {
                super.render();
                Game.game.canvas.drawText(x+2, y+3, text);
            }
        };
        if (buttons.notEmpty()) {
            button.downButton = buttons.peek();
            buttons.peek().upButton = button;
        }
        buttons.add(button);
    }

//    @Override public void update() {
//        buttons.forEach(Button::update);
//    }
    @Override public void render() {
        buttons.forEach(Button::render);
    }
    @Override public void show() {
        buttons.forEach(Button::show);
        Game.game.audio.stopMusic();
    }
    @Override public void hide() {
        buttons.forEach(Button::hide);
    }
    @Override public void resize(int width, int height) {
        if (buttons.size == 0)
            return;
        int totalButtonHeight = buttons.get(0).height;
        for (int i = 1; i < buttons.size; i++)
            totalButtonHeight += buttons.get(i).height + 1;
        int middleCanvas = Game.game.canvas.getWidth()/2;
        int y = Game.game.canvas.getHeight()/2 - totalButtonHeight/2;
        for (int i = 0; i < buttons.size; i++) {
            Button button = buttons.get(i);
            button.x = middleCanvas - button.width/2;
            button.y = y;
            y += button.height + 1;
        }
    }
}
