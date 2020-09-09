package com.celtican.pokemon.overworld.objects.nonabstract;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Hitbox;
import com.celtican.pokemon.overworld.Tile;
import com.celtican.pokemon.overworld.objects.MapObject;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.CharacterTexture;

public class Character extends MapObject {
    public Vector2 delta = new Vector2();

    public CharacterTexture texture = new CharacterTexture();

    public Character() {
        super();
        hitbox.width = 12;
        hitbox.height = 8;
    }

    @Override public void update() {
        if (delta.isZero())
            return;
        Hitbox newHitbox = new Hitbox(hitbox);
        newHitbox.x += delta.x;
        newHitbox.y += delta.y;

        Array<MapObject> intersectingObjects = Game.game.map.getObjectsIn(newHitbox, this);
        if (intersectingObjects.notEmpty())
            for (int i = 0; i < intersectingObjects.size; i++)
                if (intersectingObjects.get(i).isSolid)
                    return;

        // check for tiles
        Vector2Int start = Game.game.map.worldPosToTilePos((int)newHitbox.x, (int)newHitbox.y);
        Vector2Int end = Game.game.map.worldPosToTilePos((int)newHitbox.x + newHitbox.width,
                (int)newHitbox.y + newHitbox.height);
        for (int x = start.x; x <= end.x; x++) {
            for (int y = start.y; y <= end.y; y++) {
                Array<Tile> tiles = Game.game.map.getTiles(x, y);
                if (tiles == null)
                    return;
                for (int i = 0; i < tiles.size; i++)
                    if (tiles.get(i).type == Tile.Type.SOLID)
                        return;
            }
        }

        moveTo(newHitbox.x, newHitbox.y);
    }
    @Override public void render() {
        Game.game.map.renderToMap(texture, (int)hitbox.x-10, (int)hitbox.y);
    }

    @Override public ArrayMap<String, Object> getValues(boolean omitIfDefault) {
        ArrayMap<String, Object> map = super.getValues(omitIfDefault);
        if (!omitIfDefault || texture.isLoaded())
            map.put("texture", texture.toString());
        return map;
    }
    @Override public boolean setValue(String key, Object value) {
        if (super.setValue(key, value))
            return true;
        if (key.equals("texture") && value instanceof String) {
            texture = CharacterTexture.fromString((String) value);
            return true;
        }
        return false;
    }

    public void changeMove(Vector2 delta) {
        changeMove(delta.x, delta.y, false);
    }
    public void changeMove(Vector2 delta, boolean isRunning) {
        changeMove(delta.x, delta.y, isRunning);
    }
    public void changeMove(float dX, float dY) {
        changeMove(dX, dY, false);
    }
    public void changeMove(float dX, float dY, boolean isRunning) {
        if (dX == delta.x && dY == delta.y)
            return;
        if (dX == 0 && dY == 0) {
            Direction dir = Direction.getDirFromDelta(delta);
            texture.setFrame(dir);
            delta.set(0, 0);
        } else {
            Direction dir = Direction.getDirFromDelta(dX, dY);
            texture.setFrame(dir, isRunning ? MovementStatus.RUNNING : MovementStatus.WALKING);
            delta.set(dX, dY);
        }
    }

    public enum Direction {
        NORTH('N'), EAST('E'), SOUTH('S'), WEST('W');

        private final char c;
        Direction(char c) {
            this.c = c;
        }

        public char asChar() {
            return c;
        }

        public static Direction getDirFromDelta(Vector2 v) {
            return getDirFromDelta(v.x, v.y);
        }
        public static Direction getDirFromDelta(float x, float y) {
            if (y < 0)
                return SOUTH;
            else if (y > 0)
                return NORTH;
            else if (x < 0)
                return WEST;
            else if (x > 0)
                return EAST;
            else
                return SOUTH;
        }
    }
    public enum MovementStatus {
        STILL, WALKING, RUNNING
    }
}
