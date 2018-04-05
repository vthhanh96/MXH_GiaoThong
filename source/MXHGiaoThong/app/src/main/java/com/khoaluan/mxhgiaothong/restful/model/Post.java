package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("creator")
    @Expose
    private User creator;

    @SerializedName("category")
    @Expose
    private Category category;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("created_date")
    @Expose
    private Date createdDate;

    @SerializedName("modify_date")
    @Expose
    private Date modifyDate;

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("latitude")
    @Expose
    private float latitude;

    @SerializedName("longitude")
    @Expose
    private float longitude;

    @SerializedName("likeAmount")
    @Expose
    private int likeAmount;

    @SerializedName("dislikeAmount")
    @Expose
    private int dislikeAmount;

//    @SerializedName("comments")
//    @Expose
//    private List<Comment> comments;

    @SerializedName("reaction")
    @Expose
    private List<Reaction> reaction;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(int likeAmount) {
        this.likeAmount = likeAmount;
    }

    public int getDislikeAmount() {
        return dislikeAmount;
    }

    public void setDislikeAmount(int dislikeAmount) {
        this.dislikeAmount = dislikeAmount;
    }

//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }

    public List<Reaction> getReaction() {
        return reaction;
    }

    public void setReaction(List<Reaction> reaction) {
        this.reaction = reaction;
    }
}
