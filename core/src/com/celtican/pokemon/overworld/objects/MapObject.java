package com.celtican.pokemon.overworld.objects;

import com.badlogic.gdx.utils.ArrayMap;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Chunk;
import com.celtican.pokemon.overworld.Hitbox;
import com.celtican.pokemon.overworld.editor.Configurable;

public abstract class MapObject implements Configurable {
    public Hitbox hitbox;
    public Chunk chunk;
    public boolean isSolid = true;

    public MapObject() {
        hitbox = new Hitbox(0, 0, 16, 16);
        chunk = Game.game.map.chunks[0];
        chunk.objects.add(this);
    }

    public void update() {}
    public void render() {}

    public void moveTo(float x, float y) {
        if ((x == hitbox.x && y == hitbox.y) || x < 0 || y < 0 ||
                x >= Game.game.map.chunksX*Game.CHUNK_SIZE ||
                y >= Game.game.map.chunksY*Game.CHUNK_SIZE)
            return;
        int curChunkX = (int)hitbox.x/Game.CHUNK_SIZE;
        int curChunkY = (int)hitbox.y/Game.CHUNK_SIZE;
        int newChunkX = (int)x/Game.CHUNK_SIZE;
        int newChunkY = (int)y/Game.CHUNK_SIZE;
        hitbox.x = x;
        hitbox.y = y;
        if (curChunkX != newChunkX || curChunkY != newChunkY) {
            Chunk chunk = Game.game.map.chunks[newChunkY*Game.game.map.chunksX + newChunkX];
            this.chunk.objects.removeValue(this, true);
            this.chunk = chunk;
            this.chunk.objects.add(this);
        }
    }

    @Override public ArrayMap<String, Object> getValues(boolean omitIfDefault) {
        ArrayMap<String, Object> values = new ArrayMap<>();
        if (hitbox == null)
            return values;
        if (!omitIfDefault || !isSolid) values.put("isSolid", isSolid);
        values.put("x", (int)hitbox.x);
        values.put("y", (int)hitbox.y);
        if (!omitIfDefault || hitbox.width != 16) values.put("width", hitbox.width);
        if (!omitIfDefault || hitbox.height != 16) values.put("height", hitbox.height);
        return values;
    }
    @Override public boolean setValue(String key, Object value) {
        switch (key) {
            case "isSolid": if (value instanceof Boolean) {isSolid = (Boolean)value; return true;} return false;
            case "x": if (value instanceof Integer) {moveTo((Integer)value, hitbox.y); return true;} return false;
            case "y": if (value instanceof Integer) {moveTo(hitbox.x, (Integer)value); return true;} return false;
            case "width": if (value instanceof Integer) {hitbox.width = (Integer)value; return true;} return false;
            case "height": if (value instanceof Integer) {hitbox.height = (Integer)value; return true;} return false;
        }
        return false;
    }
}
