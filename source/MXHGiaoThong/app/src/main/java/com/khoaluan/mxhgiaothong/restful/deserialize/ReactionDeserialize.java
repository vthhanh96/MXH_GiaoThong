package com.khoaluan.mxhgiaothong.restful.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.khoaluan.mxhgiaothong.restful.ApiGenerator;
import com.khoaluan.mxhgiaothong.restful.model.Reaction;

import java.lang.reflect.Type;

/**
 * Created by Hong Hanh on 4/2/2018.
 */

public class ReactionDeserialize implements JsonDeserializer<Reaction> {
    @Override
    public Reaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Reaction reaction;
        try {
            reaction = ApiGenerator.createGson(Reaction.class).fromJson(json, Reaction.class);
        } catch (Exception e) {
            reaction = new Reaction();
            reaction.setId(json.getAsString());
        }
        return reaction;
    }
}
