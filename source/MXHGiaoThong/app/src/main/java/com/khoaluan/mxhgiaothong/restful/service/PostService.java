package com.khoaluan.mxhgiaothong.restful.service;

import com.khoaluan.mxhgiaothong.restful.request.CommentRequest;
import com.khoaluan.mxhgiaothong.restful.request.CreatePostRequest;
import com.khoaluan.mxhgiaothong.restful.request.DoReactionRequest;
import com.khoaluan.mxhgiaothong.restful.request.FilterNearMePostRequest;
import com.khoaluan.mxhgiaothong.restful.request.FilterPostRequest;
import com.khoaluan.mxhgiaothong.restful.request.GetNearMePostRequest;
import com.khoaluan.mxhgiaothong.restful.request.ListPostByUserIdResquest;
import com.khoaluan.mxhgiaothong.restful.request.UpdatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.CommentResponse;
import com.khoaluan.mxhgiaothong.restful.response.CreatePostResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hong Hanh on 3/19/2018.
 */

public interface PostService {
    @GET("post")
    Call<GetAllPostResponse> getAllPost(
            @Header("Authorization") String token,
            @Query("page") int pageNumber
    );

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
            @Path("postId") Integer id,
            @Body UpdatePostRequest request
    );

    @DELETE("post/{postId}")
    Call<BaseResponse> deletePost(
            @Header("Authorization") String token,
            @Path("postId") Integer id
    );

    @GET("post/{postId}")
    Call<PostResponse> getPostInfo(
            @Header("Authorization") String token,
            @Path("postId") Integer id
    );

    @POST("post/{postId}/reaction")
    Call<PostResponse> doReaction(
            @Header("Authorization") String token,
            @Path("postId") Integer id,
            @Body DoReactionRequest request
    );

    @POST("post/{postId}/comment")
    Call<CommentResponse> createComment(
            @Header("Authorization") String token,
            @Path("postId") Integer id,
            @Body CommentRequest request
    );

    @PUT("post/{postId}/comment/{commentId}")
    Call<CommentResponse> editComment(
            @Header("Authorization") String token,
            @Path("postId") Integer postId,
            @Path("commentId") String commentId,
            @Body CommentRequest request
    );

    @DELETE("post/{postId}/comment/{commentId}")
    Call<BaseResponse> deleteComment(
            @Header("Authorization") String token,
            @Path("postId") Integer postId,
            @Path("commentId") String commentId
    );

    @POST("post/filter")
    Call<GetAllPostResponse> getPostFilter(
            @Body FilterPostRequest request
    );

    @POST("post/nearme")
    Call<GetAllPostResponse> getPostNearMe(
            @Body GetNearMePostRequest request
    );

    @POST("post/nearme/filter")
    Call<GetAllPostResponse> getPostNearMeFilter(
            @Body FilterNearMePostRequest request
    );

    @POST("post/{postId}/interested")
    Call<PostResponse> interested(
            @Header("Authorization") String token,
            @Path("postId") int postId);
}
