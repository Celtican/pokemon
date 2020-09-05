package com.celtican.pokemon.overworld.editor;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public interface Configurable extends Json.Serializable {

    ArrayMap<String, Object> getValues();
    boolean setValue(String key, Object value);

    @Override default void write(Json json) {
        json.writeValue("class", getClass().getName());
        getValues().forEach(entry -> json.writeValue(entry.key, entry.value));
    }
    @Override default void read(Json json, JsonValue jsonData) {}
}
