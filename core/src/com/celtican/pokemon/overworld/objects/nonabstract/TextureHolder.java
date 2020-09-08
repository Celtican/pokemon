package com.celtican.pokemon.overworld.objects.nonabstract;

import com.badlogic.gdx.utils.ArrayMap;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.objects.MapObject;
import com.celtican.pokemon.utils.graphics.Texture;

public class TextureHolder extends MapObject {
    public Texture texture = new Texture();

    @Override public void render() {
        Game.game.map.renderToMap(texture, (int)hitbox.x, (int)hitbox.y);
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
            texture.setFromString((String) value);
            return true;
        }
        return false;
    }
}
