package com.khoaluan.mxhgiaothong.restful.service;

import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserLoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}
