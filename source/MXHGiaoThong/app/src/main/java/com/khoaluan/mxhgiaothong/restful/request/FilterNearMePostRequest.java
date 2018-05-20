package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hong Hanh on 5/20/2018.
 */

public class FilterNearMePostRequest implements Serializable {

    @SerializedName("latitude")
    @Expose Float latitude;

    @SerializedName("longitude")
    @Expose Float longitude;

    @SerializedName("category")
    @Expose
    private List<Integer> category;

    @SerializedName("level")
    @Expose List<Integer> level;

    public FilterNearMePostRequest(Float latitude, Float longitude, List<Integer> category, List<Integer> level) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.level = level;
    }
}
