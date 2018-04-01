package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HieuMinh on 3/31/2018.
 */

public class LoginUseRequest implements Serializable {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    public LoginUseRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginUseRequest(String email, String password, String fullName, String avatarUrl) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
    }
}
