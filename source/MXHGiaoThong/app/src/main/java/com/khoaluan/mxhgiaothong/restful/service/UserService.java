package com.khoaluan.mxhgiaothong.restful.service;

import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetUserInfoResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserLoginResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by HieuMinh on 3/31/2018.
 */

public interface UserService {
    @POST("login")
    Call<UserLoginResponse> login(@Body LoginUseRequest loginUseRequest);

    @POST("register")
    Call<BaseResponse> register(@Body LoginUseRequest registerUseRequest);

    @POST("forgotPassword")
    Call<BaseResponse> forgotPassword(@Body LoginUseRequest email);

    @GET("me")
    Call<GetUserInfoResponse> getUserInfo(@Header("Authorization") String token);

    @POST("changePass")
    Call<BaseResponse> changePassword(@Body LoginUseRequest emailPass);

    @GET("{id}")
    Call<UserResponse> getUserById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @POST("editUser")
    Call<BaseResponse> editUser(
            @Header("Authorization") String token,
            @Body User userRequest);
}
