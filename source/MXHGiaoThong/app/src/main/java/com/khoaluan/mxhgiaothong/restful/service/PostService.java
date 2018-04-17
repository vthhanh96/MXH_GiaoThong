package com.khoaluan.mxhgiaothong.restful.service;

import com.khoaluan.mxhgiaothong.restful.RestResponse;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.request.CreatePostRequest;
import com.khoaluan.mxhgiaothong.restful.request.DoReactionRequest;
import com.khoaluan.mxhgiaothong.restful.request.ListPostByUserIdResquest;
import com.khoaluan.mxhgiaothong.restful.request.UpdatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.CreatePostResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Hong Hanh on 3/19/2018.
 */

public interface PostService {
    @GET("post")
    Call<GetAllPostResponse> getAllPost();

    @POST("post")
    Call<CreatePostResponse> createPost(
            @Header("Authorization") String token,
            @Body CreatePostRequest request);

    @POST("post/listUserPost")
    Call<GetAllPostResponse> getPostByUserId(
            @Header("Authorization") String token,
            @Body ListPostByUserIdResquest request
    );

    @PUT("post/{postId}")
    Call<BaseResponse> updatePost(
            @Header("Authorization") String token,
            @Path("postId") String id,
            @Body UpdatePostRequest request
    );

    @DELETE("post/{postId}")
    Call<BaseResponse> deletePost(
            @Header("Authorization") String token,
            @Path("postId") String id
    );

    @GET("post/{postId}")
    Call<PostResponse> getPostInfo(
            @Path("postId") String id
    );

    @POST("post/{postId}/reaction")
    Call<PostResponse> doReaction(
            @Header("Authorization") String token,
            @Path("postId") String id,
            @Body DoReactionRequest request
    );
}
