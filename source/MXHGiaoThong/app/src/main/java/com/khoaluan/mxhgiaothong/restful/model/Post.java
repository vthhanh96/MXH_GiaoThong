package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {

    @SerializedName("post_content")
    public String mPostContent;

    public String mName;
    public String mPlace;
    public String mTime;
    public String mUrlImageContent;
    public String mUrlAvatar;

    public Post() {
    }

    public Post(String postContent, String name, String place, String time, String urlImageContent, String urlAvatar) {
        mPostContent = postContent;
        mName = name;
        mPlace = place;
        mTime = time;
        mUrlImageContent = urlImageContent;
        mUrlAvatar = urlAvatar;
    }
}
