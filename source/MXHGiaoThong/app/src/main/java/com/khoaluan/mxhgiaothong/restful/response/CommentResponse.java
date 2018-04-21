package com.khoaluan.mxhgiaothong.restful.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khoaluan.mxhgiaothong.restful.model.Comment;

/**
 * Created by Hong Hanh on 4/21/2018.
 */

public class CommentResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private Comment mComment;

    public Comment getComment() {
        return mComment;
    }

    public void setComment(Comment comment) {
        mComment = comment;
    }
}
