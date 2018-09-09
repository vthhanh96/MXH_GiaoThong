package com.khoaluan.mxhgiaothong.restful.service;

import com.khoaluan.mxhgiaothong.restful.response.GetAllCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hong Hanh on 4/12/2018.
 */

public interface CategoryService {
    @GET("category")
    Call<GetAllCategoryResponse> getAllCategory();

    @GET("category")
    Call<GetAllCategoryResponse> getCategories(@Query("q") String query, @Query("page") Integer pageNumber);
}
