package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hong Hanh on 5/20/2018.
 */

public class GetNearMePostRequest implements Serializable {
    @SerializedName("latitude")
    @Expose Float latitude;

    @SerializedName("longitude")
    @Expose Float longitude;

    public GetNearMePostRequest(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
