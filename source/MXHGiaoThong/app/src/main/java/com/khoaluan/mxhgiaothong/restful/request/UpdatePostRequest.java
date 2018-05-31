package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hong Hanh on 4/14/2018.
 */

public class UpdatePostRequest implements Serializable {

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("location")
    @Expose
    private Location location;

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
        List<Float> coordinates = new ArrayList<>();
        coordinates.add(latitude.floatValue());
        coordinates.add(longitude.floatValue());
        this.location = new Location();
        this.location.setCoordinates(coordinates);
        this.place = place;
        this.category = category;
        this.level = level;
        this.imageUrl = imageUrl;
    }
}
