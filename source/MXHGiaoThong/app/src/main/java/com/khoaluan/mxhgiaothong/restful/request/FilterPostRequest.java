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
    private List<String> category;

    public FilterPostRequest(List<String> category) {
        this.category = category;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
