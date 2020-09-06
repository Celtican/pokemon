package com.celtican.pokemon.overworld.editor;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public interface Configurable extends Json.Serializable {

    ArrayMap<String, Object> getValues();
    boolean setValue(String key, Object value);

    @Override default void write(Json json) {
        json.writeValue("type", getClass().getSimpleName());
        getValues().forEach(entry -> json.writeValue(entry.key, entry.value));
    }
    @Override default void read(Json json, JsonValue jsonData) {
        jsonData.forEach(jsonValue -> {
            if (!jsonValue.name().equals("type")) {
                switch (jsonValue.type()) {
                    case stringValue: setValue(jsonValue.name(), jsonValue.asString()); break;
                    case longValue: setValue(jsonValue.name(), jsonValue.asInt()); break;
                    case doubleValue: setValue(jsonValue.name(), jsonValue.asFloat()); break;
                    case booleanValue: setValue(jsonValue.name(), jsonValue.asBoolean()); break;
//                    case object:
//                    case array:
//                    case nullValue:
                }
            }
        });
    }
}
