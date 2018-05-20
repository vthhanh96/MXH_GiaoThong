package com.khoaluan.mxhgiaothong.restful.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.khoaluan.mxhgiaothong.restful.ApiGenerator;
import com.khoaluan.mxhgiaothong.restful.model.Location;

import java.lang.reflect.Type;

/**
 * Created by Hong Hanh on 5/20/2018.
 */

public class LocationDeserialize implements JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Location location;
        try {
            location = ApiGenerator.createGson(Location.class).fromJson(json, Location.class);
        } catch (Exception e) {
            location = new Location();
        }
        return location;
    }
}
