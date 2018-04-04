package com.khoaluan.mxhgiaothong.restful.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.khoaluan.mxhgiaothong.restful.ApiGenerator;
import com.khoaluan.mxhgiaothong.restful.model.Category;

import java.lang.reflect.Type;

/**
 * Created by Hong Hanh on 4/2/2018.
 */

public class CategoryDeserialize implements JsonDeserializer<Category> {
    @Override
    public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Category category;
        try {
            category = ApiGenerator.createGson(Category.class).fromJson(json, Category.class);
        } catch (Exception e) {
            category = new Category();
            category.setId(json.getAsString());
        }
        return category;
    }
}
