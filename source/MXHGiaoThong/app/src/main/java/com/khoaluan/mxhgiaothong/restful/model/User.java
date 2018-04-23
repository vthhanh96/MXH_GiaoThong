package com.khoaluan.mxhgiaothong.restful.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HieuMinh on 3/31/2018.
 */

public class User implements Serializable {

    @SerializedName("_id")
    @Expose
    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("birthDate")
    @Expose
    private String birthDate;

    @SerializedName("id_fb_gg")
    @Expose
    private String id_fb_gg;

    public String getId_fb_gg() {
        return id_fb_gg;
    }

    public void setId_fb_gg(String id_fb_gg) {
        this.id_fb_gg = id_fb_gg;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User() {
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.avatarUrl = builder.avatarUrl;
        this.fullName = builder.fullName;
        this.gender = builder.gender;
        this.address = builder.address;
        this.birthDate = builder.birthDate;
        this.id_fb_gg = builder.id_fb_gg;
    }

    public static class Builder {
        private int id;
        private String email;
        private String password;
        private String avatarUrl;
        private String fullName;
        private String gender;
        private String address;
        private String birthDate;
        private String id_fb_gg;

        public String getId_fb_gg() {
            return id_fb_gg;
        }

        public Builder setId_fb_gg(String id_fb_gg) {
            this.id_fb_gg = id_fb_gg;
            return this;
        }

        public Builder() {
            this.email = null;
            this.password = null;
            this.avatarUrl = null;
            this.fullName = null;
            this.gender = null;
            this.address = null;
            this.birthDate = null;
            this.id_fb_gg = null;
        }
        public User build(){
            return  new User(this);
        }

        public int getId() {
            return id;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public Builder setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public String getFullName() {
            return fullName;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public String getGender() {
            return gender;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public Builder setBirthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }
    }
}
