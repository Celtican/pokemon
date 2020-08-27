package com.celtican.pokemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Button;
import com.celtican.pokemon.utils.data.Vector2Int;

public class MainMenuScreen extends Screen {
    private final Array<Button> buttons;

    public MainMenuScreen() {
        buttons = new Array<>();
        addButton("Exit", () -> Gdx.app.exit());
        addButton("Edit Tileset", () -> Game.game.switchScreens(new EditTilesetScreen()));
        addButton("Edit Map", null);
        addButton("Play", () -> Game.game.switchScreens(new OverworldScreen()));
    }

    private void addButton(String text, Runnable runnable) {
        Vector2Int dimensions = Game.game.canvas.getBoundsOfText(text);
        buttons.add(new Button(0, 0, dimensions.x+4, dimensions.y+4) {
            @Override public void clicked() {
                if (runnable != null)
                    runnable.run();
            }
            @Override public void render() {
                super.render();
                Game.game.canvas.drawText(x+2, y+3, text);
            }
        });
    }

    @Override public void update() {
        for (Button button : buttons)
            button.update();
    }
    @Override public void render() {
        for (Button button : buttons)
            button.render();
    }
    @Override public void show() {
        for (Button button : buttons)
            button.show();
    }
    @Override public void hide() {
        for (Button button : buttons)
            button.hide();
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
