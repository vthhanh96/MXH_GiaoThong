package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Post;

import java.util.List;

/**
 * Created by Hong Hanh on 4/12/2018.
 */

public class GetAllCategoryResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private List<Category> mCategories;

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }
}
