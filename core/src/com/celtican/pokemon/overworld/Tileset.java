package com.celtican.pokemon.overworld;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Texture;

public class Tileset implements Json.Serializable {
    private ArrayMap<String, Tile> tiles;
    private String fileName;

    private Tileset() {}
    public Tileset(String fileName) {
        this(fileName, Game.game.assets.get(fileName, TextureAtlas.class));
    }
    public Tileset(String fileName, TextureAtlas atlas) {
        this.fileName = fileName;
        tiles = new ArrayMap<>();
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        for (int i = 0; i < regions.size; i++) {
            TextureAtlas.AtlasRegion region = regions.get(i);
            tiles.put(region.name + ":" + region.index, new Tile(new Texture(fileName, region)), i);
        }
    }
    public Tileset(String fileName, Array<Tile> tiles) {
        this.fileName = fileName;
        this.tiles = new ArrayMap<>();
        for (int i = 0; i < tiles.size; i++) {
            Tile tile = tiles.get(i);
            String key = tile.texture.toString(true);
            if (key.split(":").length < 2)
                key += ":-1";
            this.tiles.put(key, tile, i);
        }
    }

    public Tile get(int i) {
        return tiles.getValueAt(i);
    }
    public Tile get(String string) {
        return tiles.get(string);
    }
    public Tile get(String string, int index) {
        return get(string + ":" + index);
    }
    public int size() {
        return tiles.size;
    }

    @Override public void write(Json json) {
//        json.writeObjectStart("name", Tileset.class, Tileset.class);
        json.writeObjectStart(fileName);
        tiles.forEach(entry -> json.writeValue(entry.key, entry.value.type.toString()));
        json.writeObjectEnd();
//        json.writeObjectEnd();
    }
    @Override public void read(Json json, JsonValue jsonData) {
        fileName = jsonData.child.name;
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        tiles = new ArrayMap<>();
        final int[] i = {0}; // if it's just an int, the iterator doesn't cooperate
        jsonData.child.forEach(jsonValue -> {
            String[] split = jsonValue.name.split(":");
            tiles.put(jsonValue.name, new Tile(new Texture(fileName, atlas.findRegion(split[0], Integer.parseInt(split[1]))),
                    Tile.Type.valueOf(jsonValue.asString())), i[0]++);
        });
    }
}
