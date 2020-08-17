package com.celtican.pokemon.overworld;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Texture;

public class Map {

    private final Chunk[] chunks;

    private final int chunkWidth, chunkHeight;

    public Map() {
        chunkWidth = 2;
        chunkHeight = 2;

        Tile grass = new Tile(new Texture("spritesheets/overworld.atlas", "Grass"));
        Tile dirt = new Tile(new Texture("spritesheets/overworld.atlas", "Dirt"));
        chunks = new Chunk[chunkWidth*chunkHeight];
        for (int i = 0; i < chunks.length; i++) {
            Tile[] tiles = new Tile[8*8];
            for (int j = 0; j < 64; j++)
                tiles[j] = MathUtils.randomBoolean() ? grass : dirt;
            chunks[i] = new Chunk(tiles);
        }
    }

    public void update() {

    }
    public void render() {
        int i = 0;
        for (int chunkX = 0; chunkX < chunkWidth; chunkX++)
            for (int chunkY = 0; chunkY < chunkHeight; chunkY++)
                chunks[i++].render(chunkX * Game.CHUNK_SIZE, chunkY * Game.CHUNK_SIZE);
    }

}
