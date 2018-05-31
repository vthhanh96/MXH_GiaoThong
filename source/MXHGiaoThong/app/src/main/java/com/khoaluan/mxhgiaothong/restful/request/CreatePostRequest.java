package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreatePostRequest implements Serializable {

    @SerializedName("content")
    @Expose
    private String mContent;

    @SerializedName("location")
    @Expose
    private Location location;

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
        List<Float> coordinates = new ArrayList<>();
        coordinates.add(latitude.floatValue());
        coordinates.add(longitude.floatValue());
        this.location = new Location();
        this.location.setCoordinates(coordinates);
        this.place = place;
        this.category = category;
        this.level = level;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }
}
