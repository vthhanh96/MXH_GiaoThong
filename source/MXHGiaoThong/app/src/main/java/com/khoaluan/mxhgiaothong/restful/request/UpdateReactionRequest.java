package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hong Hanh on 4/16/2018.
 */

public class UpdateReactionRequest implements Serializable{
    @SerializedName("status_reaction")
    @Expose
    private int statusReaction;

    public UpdateReactionRequest(int statusReaction) {
        this.statusReaction = statusReaction;
    }
}
