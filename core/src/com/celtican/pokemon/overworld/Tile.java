package com.celtican.pokemon.overworld;

import com.celtican.pokemon.utils.graphics.Texture;

public class Tile {

    public Texture texture;
    public Type type;

    public Tile(Texture texture) {
        this.texture = texture;
        type = Type.NONE;
    }
    public Tile(Texture texture, Type type) {
        this.texture = texture;
        this.type = type;
    }

    public void render(int x, int y) {
        texture.render(x, y);
    }

    public enum Type {
        NONE, SOLID, WATER, GRASS
    }
}
