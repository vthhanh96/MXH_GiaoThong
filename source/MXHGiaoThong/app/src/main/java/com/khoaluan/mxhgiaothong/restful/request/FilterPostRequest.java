package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hong Hanh on 5/10/2018.
 */

public class FilterPostRequest implements Serializable {

    @SerializedName("category")
    @Expose
    private List<Integer> category;

    @SerializedName("level")
    @Expose List<Integer> level;

    public FilterPostRequest(List<Integer> category, List<Integer> level) {
        this.category = category;
        this.level = level;
    }
}
