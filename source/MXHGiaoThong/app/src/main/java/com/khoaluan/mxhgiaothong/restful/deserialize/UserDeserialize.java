package com.khoaluan.mxhgiaothong.restful.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.khoaluan.mxhgiaothong.restful.ApiGenerator;
import com.khoaluan.mxhgiaothong.restful.model.User;

import java.lang.reflect.Type;

/**
 * Created by Hong Hanh on 4/2/2018.
 */

public class UserDeserialize implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User user;
        try {
            user = ApiGenerator.createGson(User.class).fromJson(json, User.class);
        } catch (Exception e) {
            user = new User();
            user.setId(json.getAsString());
        }
        return user;
    }
}
