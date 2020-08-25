package com.celtican.pokemon.screens;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Button;
import com.celtican.pokemon.utils.data.Vector2Int;

public class MainMenuScreen extends Screen {
    public Button overworldButton;

    public MainMenuScreen() {
        String text = "Overworld";
        Vector2Int dimensions = Game.game.canvas.getBoundsOfText(text);
        overworldButton = new Button(0, 0, dimensions.x+4, dimensions.y+4) {
            @Override public void update() {
                if (isMouseJustPressed())
                    Game.game.switchScreens(new OverworldScreen());
            }
            @Override public void render() {
                super.render();
                Game.game.canvas.drawText(x + 2, y + 3, text);
            }
        };
    }

    @Override public void update() {
        overworldButton.update();
    }
    @Override public void render() {
        overworldButton.render();
    }
    @Override public void show() {
        overworldButton.show();
    }
    @Override public void hide() {
        overworldButton.hide();
    }
    @Override public void resize(int width, int height) {
        overworldButton.x = width/2 - overworldButton.width/2;
        overworldButton.y = height/2 - overworldButton.height/2;
    }
}
