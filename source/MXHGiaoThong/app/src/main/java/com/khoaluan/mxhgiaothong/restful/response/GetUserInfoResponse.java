package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.User;

/**
 * Created by Hong Hanh on 4/9/2018.
 */

public class GetUserInfoResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
