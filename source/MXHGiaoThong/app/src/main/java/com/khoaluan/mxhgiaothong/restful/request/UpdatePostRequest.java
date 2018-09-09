package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hong Hanh on 4/14/2018.
 */

public class UpdatePostRequest implements Serializable {

    @SerializedName("content") @Expose private String content;
    @SerializedName("location") @Expose private Location location;
    @SerializedName("place") @Expose private String place;
    @SerializedName("categories") @Expose private List<Category> categories;
    @SerializedName("amount") @Expose private int amount;
    @SerializedName("time") @Expose private Date time;

    public UpdatePostRequest(String content, Double latitude, Double longitude, String place, List<Category> categories, int amount, Date time) {
        this.content = content;
        List<Float> coordinates = new ArrayList<>();
        coordinates.add(longitude.floatValue());
        coordinates.add(latitude.floatValue());
        this.location = new Location();
        this.location.setCoordinates(coordinates);
        this.place = place;
        this.categories = categories;
        this.amount = amount;
        this.time = time;
    }
}
