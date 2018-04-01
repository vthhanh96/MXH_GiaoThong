package com.khoaluan.mxhgiaothong.restful;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGenerator {

    private static Retrofit.Builder builder = createBuilder("http://10.0.2.2:3000/api/");

    private static Retrofit retrofit = builder.build();

    private ApiGenerator() {}

    private static Retrofit.Builder createBuilder(String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }

    public static <T> T createService(Class<T> serviceClass) {
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static void changeBaseUrl(String baseUrl) {
        builder = createBuilder(baseUrl);
    }
}
