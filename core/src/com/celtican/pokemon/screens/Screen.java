package com.celtican.pokemon.screens;

public abstract class Screen {

    public void show() {}
    public void hide() {}
    public void resize(int width, int height) {}

    public void render() {}
    public void update() {}

    public void dispose() {}
}
