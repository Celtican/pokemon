package com.celtican.pokemon.overworld;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.celtican.pokemon.Game;

public class Chunk implements Json.Serializable {
    private Tile[][] tiles;

    private Chunk() {}
    public Chunk(int layers) {
        tiles = new Tile[layers][64];
    }
    public Chunk(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public void render(int x, int y, int layer) {
        int i = -1;
        for (int tileY = 0; tileY < 8; tileY++)
            for (int tileX = 0; tileX < 8; tileX++)
                if (tiles[layer][++i] != null)
                    tiles[layer][i].render(tileX * Game.TILE_SIZE + x, tileY * Game.TILE_SIZE + y);
    }

    public void setTile(int x, int y, int layer, Tile tile) {
        tiles[layer][y*8 + x] = tile;
    }
    public Tile getTile(int x, int y, int layer) {
        return tiles[layer][y*8 + x];
    }
    public Array<Tile> getTiles(int x, int y) {
        Array<Tile> tileArray = new Array<>();
        int tileI = y*8 + x;
        for (Tile[] layer : tiles)
            if (layer[tileI] != null)
                tileArray.add(layer[tileI]);
        return tileArray;
    }

    @Override public void write(Json json) {
        json.writeArrayStart("Tiles");
        for (Tile[] layer : tiles) {
            json.writeArrayStart();
            for (Tile tile : layer) {
                json.writeValue(tile == null ? "null" :
                        Game.game.map.tilesets.indexOf(tile.tileset, true) + ":" + tile.name);
            }
            json.writeArrayEnd();
        }
        json.writeArrayEnd();
    }
    @Override public void read(Json json, JsonValue jsonData) {
        tiles = new Tile[Game.game.map.layers][64];
        JsonValue tilesJson = jsonData.get("tiles");
        for (int layer = 0; layer < Game.game.map.layers; layer++) {
            String[] tilesStrings = tilesJson.get(layer).asStringArray();
            for (int i = 0; i < 64; i++) {
                if (tilesStrings[i].equals("null"))
                    continue;
                String[] parts = tilesStrings[i].split(":", 2);
                Tileset tileset = Game.game.map.tilesets.get(Integer.parseInt(parts[0]));
                tiles[layer][i] = tileset.get(parts[1]);
            }
        }
    }
}
