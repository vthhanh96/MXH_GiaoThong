package com.khoaluan.mxhgiaothong.restful.deserialize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.khoaluan.mxhgiaothong.restful.ApiGenerator;
import com.khoaluan.mxhgiaothong.restful.model.Comment;

import java.lang.reflect.Type;

/**
 * Created by Hong Hanh on 4/2/2018.
 */

public class CommentDeserialize implements JsonDeserializer<Comment> {
    @Override
    public Comment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Comment comment;
        try {
            comment = ApiGenerator.createGson(Comment.class).fromJson(json, Comment.class);
        } catch (Exception e) {
            comment = new Comment();
            comment.setId(json.getAsString());
        }
        return comment;
    }
}
