package com.celtican.pokemon.overworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.objects.MapObject;
import com.celtican.pokemon.overworld.objects.nonabstract.Player;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Renderable;

public class Map implements Json.Serializable {

    public Array<Tileset> tilesets;
    public Chunk[] chunks;
    public int chunksX, chunksY;
    public int layers;
    public Vector2Int camera;
    public Vector2Int renderOffset = new Vector2Int();
    public Player player;

    private final Array<MapObject> objects = new Array<>();

    private Map() {
        Game.game.map = this;
        tilesets = new Array<>();
        camera = new Vector2Int();
    }
    public Map(int chunksX, int chunksY, int layers) {
        Game.game.map = this;
        tilesets = new Array<>();
        this.chunksX = chunksX;
        this.chunksY = chunksY;
        this.layers = layers;
        chunks = new Chunk[chunksX * chunksY];
        for (int i = 0; i < chunks.length; i++)
            chunks[i] = new Chunk();
        camera = new Vector2Int();
    }
    public static Map fromFileLocation(String fileLocation) {
        FileHandle file = Gdx.files.internal(fileLocation);
        return file.exists() ? new Json().fromJson(Map.class, file.readString()) : null;
    }

    public void update() {
        update(true);
    }
    public void update(boolean updateObjects) {
        objects.clear();
        for (Chunk chunk : chunks) objects.addAll(chunk.objects);
        if (updateObjects) objects.forEach(MapObject::update);
    }
    public void render() {
        objects.sort((o1, o2) -> (int)o2.hitbox.y - (int)o1.hitbox.y);

        renderOffset.x = Game.game.canvas.getWidth()/2 - camera.x;
        renderOffset.y = Game.game.canvas.getHeight()/2 - camera.y;

        for (int l = 0; l < layers; l++) {
            int i = 0;
            for (int chunkY = 0; chunkY < chunksY; chunkY++)
                for (int chunkX = 0; chunkX < chunksX; chunkX++)
                    chunks[i++].render(chunkX * Game.CHUNK_SIZE + renderOffset.x,
                            chunkY * Game.CHUNK_SIZE + renderOffset.y, l);
        }
        objects.forEach(MapObject::render);
    }

    public void renderToMap(Renderable renderable, int x, int y) {
        renderable.render(x + renderOffset.x, y + renderOffset.y);
    }

    @Override public void write(Json json) {
        json.writeValue("chunksX", chunksX);
        json.writeValue("chunksY", chunksY);
        json.writeValue("layers", layers);
        json.writeArrayStart("tilesets");
        tilesets.forEach(tileset -> json.writeValue(tileset.getName(false)));
        json.writeArrayEnd();
        json.writeArrayStart("chunks");
        for (Chunk chunk : chunks) json.writeValue(chunk);
        json.writeArrayEnd();
    }
    @Override public void read(Json json, JsonValue jsonData) {
        chunksX = jsonData.getInt("chunksX");
        chunksY = jsonData.getInt("chunksY");
        layers = jsonData.getInt("layers");
        for (String s : jsonData.get("tilesets").asStringArray()) tilesets.add(Tileset.fromFileName(s));
        JsonValue chunksJson = jsonData.get("chunks");
        chunks = new Chunk[chunksJson.size];
        for (int i = 0; i < chunksJson.size; i++)
            json.fromJson(Chunk.class, chunksJson.get(i).toString());
    }

    public void setTile(int x, int y, int layer, Tile tile) {
        if (x < 0 || y < 0)
            return;
        int chunkX = x/8;
        int chunkY = y/8;
        if (chunkX >= chunksX || chunkY >= chunksY)
            return;
        chunks[chunkY*chunksX + chunkX].setTile(x%8, y%8, layer, tile);
    }
    public Tile getTile(int x, int y, int layer) {
        if (x < 0 || y < 0)
            return null;
        int chunkX = x/8;
        int chunkY = y/8;
        if (chunkX >= chunksX || chunkY >= chunksY)
            return null;
        return chunks[chunkY*chunksX + chunkX].getTile(chunkX + x%8, chunkY + y%8, layer);
    }
    public Array<Tile> getTiles(Vector2Int pos) {
        return getTiles(pos.x, pos.y);
    }
    public Array<Tile> getTiles(int x, int y) {
        if (x < 0 || y < 0)
            return null;
        int chunkX = x/8;
        int chunkY = y/8;
        if (chunkX >= chunksX || chunkY >= chunksY)
            return null;
        return chunks[chunkY*chunksX + chunkX].getTiles(x%8, y%8);
    }
    public MapObject getObjectAt(float x, float y) {
        for (int i = 0; i < objects.size; i++)
            if (objects.get(i).hitbox.contains(x, y))
                return objects.get(i);
        return null;
    }
    public Array<MapObject> getObjectsAt(float x, float y) {
        Array<MapObject> array = new Array<>();
        for (int i = 0; i < objects.size; i++)
            if (objects.get(i).hitbox.contains(x, y))
                array.add(objects.get(i));
        return array;
    }
    public MapObject getObjectIn(Hitbox h) {
        return getObjectIn(h, null);
    }
    public MapObject getObjectIn(Hitbox h, MapObject omit) {
        for (int i = 0; i < objects.size; i++)
            if (objects.get(i).hitbox.overlaps(h) && objects.get(i) != omit)
                return objects.get(i);
        return null;
    }
    public Array<MapObject> getObjectsIn(Hitbox h) {
        return getObjectsIn(h, null);
    }
    public Array<MapObject> getObjectsIn(Hitbox h, MapObject omit) {
        Array<MapObject> array = new Array<>();
        for (int i = 0; i < objects.size; i++)
            if (objects.get(i).hitbox.overlaps(h) && objects.get(i) != omit)
                array.add(objects.get(i));
        return array;
    }

    public void addTileset(Tileset tileset) {
        for (int i = 0; i < tilesets.size; i++)
            if (tilesets.get(i).getName(false).equals(tileset.getName(false)))
                return;
        tilesets.add(tileset);
    }

    public Vector2Int screenPosToTilePos(Vector2Int screenPos) {
        return screenPosToTilePos(screenPos.x, screenPos.y);
    }
    public Vector2Int screenPosToTilePos(int screenX, int screenY) {
        return new Vector2Int((screenX + camera.x - Game.game.canvas.getWidth()/2)/Game.TILE_SIZE,
                (screenY + camera.y - Game.game.canvas.getHeight()/2)/Game.TILE_SIZE);
    }
    public Vector2Int screenPosToWorldPos(Vector2Int screenPos) {
        return screenPosToWorldPos(screenPos.x, screenPos.y);
    }
    public Vector2Int screenPosToWorldPos(int screenX, int screenY) {
        return new Vector2Int((screenX + camera.x - Game.game.canvas.getWidth()/2),
                (screenY + camera.y - Game.game.canvas.getHeight()/2));
    }
    public Vector2Int worldPosToScreenPos(Vector2Int worldPos) {
        return worldPosToScreenPos(worldPos.x, worldPos.y);
    }
    public Vector2Int worldPosToScreenPos(int worldX, int worldY) {
        return new Vector2Int((worldX - camera.x + Game.game.canvas.getWidth()/2),
                (worldY - camera.y + Game.game.canvas.getHeight()/2));
    }
    public Vector2Int worldPosToTilePos(Vector2Int worldPos) {
        return worldPosToTilePos(worldPos.x, worldPos.y);
    }
    public Vector2Int worldPosToTilePos(int worldX, int worldY) {
        return new Vector2Int(worldX/Game.TILE_SIZE, worldY/Game.TILE_SIZE);
    }
}
