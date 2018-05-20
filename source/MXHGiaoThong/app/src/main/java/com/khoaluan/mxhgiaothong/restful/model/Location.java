package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hong Hanh on 5/20/2018.
 */

public class Location implements Serializable {
    @SerializedName("coordinates")
    @Expose
    private List<Float> coordinates;

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Float> coordinates) {
        this.coordinates = coordinates;
    }
}
