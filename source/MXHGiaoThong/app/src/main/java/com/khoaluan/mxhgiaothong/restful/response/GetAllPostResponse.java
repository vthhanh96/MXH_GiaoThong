package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Post;

import java.util.List;

/**
 * Created by Hong Hanh on 3/20/2018.
 */

public class GetAllPostResponse extends BaseResponse{

    @SerializedName("data")
    @Expose
    private List<Post> mPosts;

    public List<Post> getPosts() {
        return mPosts;
    }

    public void setPosts(List<Post> posts) {
        mPosts = posts;
    }
}
