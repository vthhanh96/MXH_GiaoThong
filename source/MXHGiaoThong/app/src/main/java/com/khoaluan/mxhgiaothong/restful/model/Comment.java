package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hong Hanh on 4/1/2018.
 */

public class Comment implements Serializable{

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("creator")
    @Expose
    private User creator;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("created_date")
    @Expose
    private Date createdDate;

    @SerializedName("modify_date")
    @Expose
    private Date modifyDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
