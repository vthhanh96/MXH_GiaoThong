package com.khoaluan.mxhgiaothong.restful;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khoaluan.mxhgiaothong.restful.deserialize.CategoryDeserialize;
import com.khoaluan.mxhgiaothong.restful.deserialize.CommentDeserialize;
import com.khoaluan.mxhgiaothong.restful.deserialize.ReactionDeserialize;
import com.khoaluan.mxhgiaothong.restful.deserialize.UserDeserialize;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Comment;
import com.khoaluan.mxhgiaothong.restful.model.Reaction;
import com.khoaluan.mxhgiaothong.restful.model.User;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGenerator {

    private static Retrofit.Builder builder = createBuilder("http://10.0.2.2:3000/api/");

    private static Retrofit retrofit = builder.build();

    private ApiGenerator() {}

    private static Retrofit.Builder createBuilder(String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(createGson(null)))
                .baseUrl(baseUrl);
    }

    public static <T> T createService(Class<T> serviceClass) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public static void changeBaseUrl(String baseUrl) {
        builder = createBuilder(baseUrl);
    }

    public static Gson createGson(Class classType) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        if(classType != User.class) {
            gsonBuilder.registerTypeAdapter(User.class, new UserDeserialize());
        }
        if(classType != Reaction.class) {
            gsonBuilder.registerTypeAdapter(Reaction.class, new ReactionDeserialize());
        }
        if(classType != Comment.class) {
            gsonBuilder.registerTypeAdapter(Comment.class, new CommentDeserialize());
        }
        if(classType != Category.class) {
            gsonBuilder.registerTypeAdapter(Category.class, new CategoryDeserialize());
        }
        gsonBuilder.setLenient();
        return gsonBuilder.create();
    }
}
