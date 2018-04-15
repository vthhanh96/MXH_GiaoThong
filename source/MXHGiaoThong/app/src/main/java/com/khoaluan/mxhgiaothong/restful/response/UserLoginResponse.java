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

    @SerializedName("_id")
    @Expose
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
