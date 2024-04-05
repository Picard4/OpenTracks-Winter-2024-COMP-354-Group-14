package de.dennisguse.opentracks.data.adapters;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class FireStoreAdapter {

    /**
     * Converts a Gson object to a Map.
     *
     * @param jsonObject The JsonObject object to convert
     * @return The Map representation of the Gson object
     */
    public static Map<String, Object> toMap(JsonObject jsonObject) {
        // Convert JsonObject to Map using Gson's built-in functionality
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, Map.class);
    }

    /**
     * Converts a Map to a JsonObject.
     *
     * @param map The Map to convert
     * @return The Gson representation of the Map
     */
    public static JsonObject toJson(Map<String, Object> map) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                // Recursively convert nested map to JsonObject
                JsonObject nestedObject = toJson((Map<String, Object>) value);
                jsonObject.add(key, nestedObject);
            } else {
                // Convert non-nested value to JSON element
                jsonObject.addProperty(key, String.valueOf(value));
            }
        }
        return jsonObject;
    }
}
