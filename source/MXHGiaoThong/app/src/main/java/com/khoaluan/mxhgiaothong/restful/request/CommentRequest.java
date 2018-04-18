package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hong Hanh on 4/17/2018.
 */

public class CommentRequest implements Serializable {

    @SerializedName("content")
    @Expose
    private String content;

    public CommentRequest(String content) {
        this.content = content;
    }
}
