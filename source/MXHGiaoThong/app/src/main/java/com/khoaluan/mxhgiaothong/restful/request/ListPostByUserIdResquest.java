package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HieuMinh on 4/13/2018.
 */

public class ListPostByUserIdResquest implements Serializable {

    @SerializedName("post_user_id")
    @Expose
    private int postUserId;

    public ListPostByUserIdResquest() {
    }

    public ListPostByUserIdResquest(int postUserId) {
        this.postUserId = postUserId;
    }

    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }
}
