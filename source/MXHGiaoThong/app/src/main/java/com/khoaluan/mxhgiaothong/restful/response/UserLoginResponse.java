package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HieuMinh on 3/31/2018.
 */

public class UserLoginResponse extends BaseResponse{
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
