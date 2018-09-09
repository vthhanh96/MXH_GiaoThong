package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreatePostRequest implements Serializable {

    @SerializedName("content") @Expose private String mContent;
    @SerializedName("location") @Expose private Location location;
    @SerializedName("place") @Expose private String place;
    @SerializedName("categories") @Expose private List<Category> categories;
    @SerializedName("amount") @Expose private int amount;
    @SerializedName("time") @Expose private Date time;

    public CreatePostRequest(String content, Double latitude, Double longitude, String place, List<Category> categories, Date time, int amount) {
        mContent = content;
        List<Float> coordinates = new ArrayList<>();
        coordinates.add(longitude.floatValue());
        coordinates.add(latitude.floatValue());
        this.location = new Location();
        this.location.setCoordinates(coordinates);
        this.place = place;
        this.categories = categories;
        this.time = time;
        this.amount = amount;
    }
}
