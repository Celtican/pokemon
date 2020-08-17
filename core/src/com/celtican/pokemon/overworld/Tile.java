package com.celtican.pokemon.overworld;

import com.celtican.pokemon.utils.graphics.Texture;

public class Tile {

    public Texture texture;

    public Tile(Texture texture) {
        this.texture = texture;
    }

    public void render(int x, int y) {
        texture.render(x, y);
    }

}
