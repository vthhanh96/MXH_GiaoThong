package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable {

    @SerializedName("_id") @Expose private Integer id;
    @SerializedName("creator") @Expose private User creator;
    @SerializedName("categories") @Expose private List<Category> categories;
    @SerializedName("level") @Expose private int level;
    @SerializedName("isActive") @Expose private Boolean isActive;
    @SerializedName("content") @Expose private String content;
    @SerializedName("imageUrl") @Expose private String imageUrl;
    @SerializedName("created_date") @Expose private Date createdDate;
    @SerializedName("modified_date") @Expose private Date modifyDate;
    @SerializedName("place") @Expose private String place;
    @SerializedName("location") @Expose private Location location;
    @SerializedName("amount") @Expose private int amount;
    @SerializedName("comments") @Expose private List<Comment> comments;
    @SerializedName("reaction") @Expose private List<Reaction> reaction;
    @SerializedName("time") @Expose private Date mTime;
    @SerializedName("interested_people") @Expose private List<User> mInterestedPeople;
    @SerializedName("joined_people") @Expose private List<User> mJoinedPeople;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return new Category();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Reaction> getReaction() {
        return reaction;
    }

    public void setReaction(List<Reaction> reaction) {
        this.reaction = reaction;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public List<User> getInterestedPeople() {
        return mInterestedPeople;
    }

    public void setInterestedPeople(List<User> interestedPeople) {
        mInterestedPeople = interestedPeople;
    }

    public List<User> getJoinedPeople() {
        return mJoinedPeople;
    }

    public void setJoinedPeople(List<User> joinedPeople) {
        mJoinedPeople = joinedPeople;
    }
}
