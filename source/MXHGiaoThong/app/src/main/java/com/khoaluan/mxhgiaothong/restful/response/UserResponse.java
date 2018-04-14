package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.User;

/**
 * Created by HieuMinh on 4/13/2018.
 */

public class UserResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private User user;

    public UserResponse(User user) {
        this.user = user;
    }

    public UserResponse() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
