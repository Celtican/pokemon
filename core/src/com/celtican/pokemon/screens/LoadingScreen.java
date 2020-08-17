package com.celtican.pokemon.screens;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Texture;

public class LoadingScreen extends Screen {

    private final Texture tree;

    public LoadingScreen() {
        tree = new Texture("misc/Tree.png");
    }

    @Override public void show() {
        Game.game.canvas.setClearColor(1, 1, 1, 1);
    }
    @Override public void hide() {

    }
    @Override public void resize(int width, int height) {

    }
    @Override public void render() {
        float color = MathUtils.sin(Game.game.frame * MathUtils.PI2/60)/2 + 0.5f;
        Game.game.canvas.setClearColor(color, 0, 0, 1);
        tree.render(Game.game.canvas.getWidth()/2 - tree.getWidth()/2, Game.game.canvas.getHeight()/2 - tree.getHeight()/2);
    }
    @Override public void update() {

    }
}
