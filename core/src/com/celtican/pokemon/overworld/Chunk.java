package com.celtican.pokemon.overworld;

import com.celtican.pokemon.Game;

public class Chunk {

    private final Tile[] tiles;

    public Chunk(Tile[] tiles) {
        this.tiles = tiles;
    }

    public void render(int x, int y) {
        int i = 0;
        for (int tileX = 0; tileX < 8; tileX++)
            for (int tileY = 0; tileY < 8; tileY++)
                tiles[i++].render(tileX * Game.TILE_SIZE + x, tileY * Game.TILE_SIZE + y);
    }

}
