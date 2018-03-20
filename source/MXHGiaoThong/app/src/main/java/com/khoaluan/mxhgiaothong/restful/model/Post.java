package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {

    @SerializedName("post_content")
    public String mPostContent;
}
