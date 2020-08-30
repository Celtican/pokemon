package com.celtican.pokemon.overworld;

import com.badlogic.gdx.graphics.Color;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Texture;

public class Tile {

    public final Tileset tileset;
    public final String name;
    public Texture texture;
    public Type type;

    public Tile(Tileset tileset, String name, Texture texture) {
        this(tileset, name, texture, Type.NONE);
    }
    public Tile(Tileset tileset, String name, Texture texture, Type type) {
        this.tileset = tileset;
        this.name = name;
        this.texture = texture;
        this.type = type;
    }

    public void render(int x, int y) {
        texture.render(x, y);
    }
    public void renderDebug(int x, int y) {
        render(x, y);
        type.drawCharacter(x + 1, y + 2);
    }

    public enum Type {
        NONE,
        SOLID('S', Color.WHITE),
        WATER('W', Color.BLUE),
        GRASS('G', Color.GREEN);

        private final Character character;
        private final Color color;

        Type() {
            this(null, null);
        }
        Type(Character character, Color color) {
            this.character = character;
            this.color = color;
        }

        public void drawCharacter(int x, int y) {
            if (character != null)
                Game.game.canvas.drawSmallText(x, y, character.toString(), color);
        }
    }
}
