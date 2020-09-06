package com.celtican.pokemon.overworld;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.objects.MapObject;

public class Chunk implements Json.Serializable {
    private Tile[][] tiles;
    public final Array<MapObject> objects = new Array<>();

    public Chunk() {
        tiles = new Tile[Game.game.map.layers][64];
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
        json.writeArrayStart("Objects");
        objects.forEach(json::writeValue);
        json.writeArrayEnd();
        json.writeArrayStart("Tiles");
        for (Tile[] layer : tiles) {
            json.writeArrayStart();
            for (Tile tile : layer)
                json.writeValue(tile == null ? "null" :
                        Game.game.map.tilesets.indexOf(tile.tileset, true) + ":" + tile.name);
            json.writeArrayEnd();
        }
        json.writeArrayEnd();
    }
    @SuppressWarnings("unchecked") @Override public void read(Json json, JsonValue jsonData) {
        for (int i = 0; i < Game.game.map.chunks.length; i++)
            if (Game.game.map.chunks[i] == null) {
                Game.game.map.chunks[i] = this;
                break;
            }
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
        JsonValue objectsJson = jsonData.get("objects");
        if (objectsJson != null) {
            objectsJson.forEach(jsonValue -> {
                try {
                    String s = "com.celtican.pokemon.overworld.objects.nonabstract." + jsonValue.get("type").asString();
                    Class<?> c = Class.forName(s).asSubclass(MapObject.class);
                    json.fromJson((Class<MapObject>)c, jsonValue.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
