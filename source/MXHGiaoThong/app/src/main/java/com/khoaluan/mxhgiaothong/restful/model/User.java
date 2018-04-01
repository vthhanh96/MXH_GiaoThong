package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HieuMinh on 3/31/2018.
 */

public class User implements Serializable {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;
}
