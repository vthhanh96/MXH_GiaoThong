package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Category;

import java.io.Serializable;

/**
 * Created by Hong Hanh on 4/14/2018.
 */

public class UpdatePostRequest implements Serializable {

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("latitude")
    @Expose
    private Float latitude;

    @SerializedName("longitude")
    @Expose
    private Float longitude;

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("category")
    @Expose
    private Category category;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public UpdatePostRequest(String content, Float latitude, Float longitude, String place, Category category, int level, String imageUrl) {
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
        this.category = category;
        this.level = level;
        this.imageUrl = imageUrl;
    }
}
