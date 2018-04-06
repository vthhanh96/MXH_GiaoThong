package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Post;

/**
 * Created by Hong Hanh on 4/5/2018.
 */

public class CreatePostResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private Post mPost;

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }
}
