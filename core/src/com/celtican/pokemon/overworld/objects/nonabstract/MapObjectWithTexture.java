package com.celtican.pokemon.overworld.objects.nonabstract;

import com.badlogic.gdx.utils.ArrayMap;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.objects.MapObject;
import com.celtican.pokemon.utils.graphics.Texture;

public class MapObjectWithTexture extends MapObject {
    public Texture texture;

    public MapObjectWithTexture() {
        texture = new Texture();
    }

    @Override public void render() {
        Game.logInfo("hmmmmm");
        Game.game.map.renderToMap(texture, hitbox.x, hitbox.y);
    }

    @Override public ArrayMap<String, Object> getValues() {
        ArrayMap<String, Object> map = super.getValues();
        map.put("texture", texture.toString());
        return map;
    }
    @Override public boolean setValue(String key, Object value) {
        if (super.setValue(key, value))
            return true;
        if (key.equals("texture") && value instanceof String) {
            texture.setFromString((String) value);
            return true;
        }
        return false;
    }
}
