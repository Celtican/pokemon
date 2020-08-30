package com.celtican.pokemon.overworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.graphics.Texture;

public class Tileset implements Json.Serializable {
    private ArrayMap<String, Tile> tiles;
    private String atlasFileName;
    private String fileName;

    private Tileset() {}
    public Tileset(String atlasFileName, TextureAtlas atlas) {
        this.atlasFileName = atlasFileName;
        tiles = new ArrayMap<>();
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        for (int i = 0; i < regions.size; i++) {
            TextureAtlas.AtlasRegion region = regions.get(i);
            String name = region.name + ":" + region.index;
            tiles.put(name, new Tile(this, name, new Texture(atlasFileName, region)), i);
        }
    }
    public static Tileset fromFileName(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        if (file.exists()) {
            Tileset tileset = new Json().fromJson(Tileset.class, file.readString());
            tileset.fileName = fileName;
            return tileset;
        }
        return null;
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
    public String getName(boolean omitDirectory) {
        if (fileName == null)
            return "null";
        if (!omitDirectory)
            return fileName;
        String[] splits = fileName.split("\\\\");
        return splits[splits.length-1].split("\\.")[0];
    }
    public String getAtlasName(boolean omitDirectory) {
        if (atlasFileName == null)
            return "null";
        if (!omitDirectory)
            return atlasFileName;
        String[] splits = atlasFileName.split("/");
        return splits[splits.length-1].split("\\.")[0];
    }

    @Override public void write(Json json) {
//        json.writeObjectStart("name", Tileset.class, Tileset.class);
        json.writeObjectStart(atlasFileName);
        tiles.forEach(entry -> json.writeValue(entry.key, entry.value.type.toString()));
        json.writeObjectEnd();
//        json.writeObjectEnd();
    }
    @Override public void read(Json json, JsonValue jsonData) {
        atlasFileName = jsonData.child.name;
        TextureAtlas atlas = Game.game.assets.get(atlasFileName, TextureAtlas.class);
        tiles = new ArrayMap<>();
        final int[] i = {0}; // if it's just an int, the iterator doesn't cooperate
        jsonData.child.forEach(jsonValue -> {
            String[] split = jsonValue.name.split(":");
            tiles.put(jsonValue.name, new Tile(this, jsonValue.name, new Texture(atlasFileName,
                    atlas.findRegion(split[0], Integer.parseInt(split[1]))),
                    Tile.Type.valueOf(jsonValue.asString())), i[0]++);
        });
    }
}
