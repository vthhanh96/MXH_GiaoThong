package com.khoaluan.mxhgiaothong.restful.service;

import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hong Hanh on 3/19/2018.
 */

public interface PostService {
    @GET("post")
    Call<GetAllPostResponse> getAllPost();
}
