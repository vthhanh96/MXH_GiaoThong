package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreatePostRequest implements Serializable {

    @SerializedName("content")
    @Expose
    private String mContent;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("level")
    @Expose
    private Integer level;

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public CreatePostRequest(String content, Double latitude, Double longitude, String place, String category, Integer level, Boolean isActive, String imageUrl) {
        mContent = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
        this.category = category;
        this.level = level;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }
}
