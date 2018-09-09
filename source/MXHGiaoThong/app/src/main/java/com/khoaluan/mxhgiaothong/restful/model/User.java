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

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("latlngAdress")
    @Expose
    private String latlngAdress;

    @SerializedName("myCharacter")
    @Expose
    private String myCharacter;

    @SerializedName("myStyle")
    @Expose
    private String myStyle;

    @SerializedName("targetCharacter")
    @Expose
    private String targetCharacter;

    @SerializedName("targetStyle")
    @Expose
    private String targetStyle;

    @SerializedName("targetFood")
    @Expose
    private String targetFood;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getLatlngAdress() {
        return latlngAdress;
    }

    public void setLatlngAdress(String latlngAdress) {
        this.latlngAdress = latlngAdress;
    }

    public String getMyCharacter() {
        return myCharacter;
    }

    public void setMyCharacter(String myCharacter) {
        this.myCharacter = myCharacter;
    }

    public String getMyStyle() {
        return myStyle;
    }

    public void setMyStyle(String myStyle) {
        this.myStyle = myStyle;
    }

    public String getTargetCharacter() {
        return targetCharacter;
    }

    public void setTargetCharacter(String targetCharacter) {
        this.targetCharacter = targetCharacter;
    }

    public String getTargetStyle() {
        return targetStyle;
    }

    public void setTargetStyle(String targetStyle) {
        this.targetStyle = targetStyle;
    }

    public String getTargetFood() {
        return targetFood;
    }

    public void setTargetFood(String targetFood) {
        this.targetFood = targetFood;
    }

    public User() {
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.phone = builder.phone;
        this.fullName = builder.fullName;
        this.password = builder.password;
        this.avatar = builder.avatar;
        this.birthday = builder.birthday;
        this.gender = builder.gender;
        this.address = builder.address;
        this.latlngAdress = builder.latlngAdress;
        this.myCharacter = builder.myCharacter;
        this.myStyle = builder.myStyle;
        this.targetCharacter = builder.targetCharacter;
        this.targetStyle = builder.targetStyle;
        this.targetFood = builder.targetFood;
    }

    public static class Builder {
        private int id;
        private String phone;
        private String fullName;
        private String password;
        private String avatar;
        private String birthday;
        private String gender;
        private String address;
        private String latlngAdress;
        private String myCharacter;
        private String myStyle;
        private String targetCharacter;
        private String targetStyle;
        private String targetFood;


        public Builder() {
            this.phone = null;
            this.fullName = null;
            this.password = null;
            this.avatar = null;
            this.birthday = null;
            this.gender = null;
            this.address = null;
            this.latlngAdress = null;
            this.myCharacter = null;
            this.myStyle = null;
            this.targetCharacter = null;
            this.targetStyle = null;
            this.targetFood = null;
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

        public String getPhone() {
            return phone;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String getFullName() {
            return fullName;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getAvatar() {
            return avatar;
        }

        public Builder setAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public String getBirthday() {
            return birthday;
        }

        public Builder setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getLatlngAdress() {
            return latlngAdress;
        }

        public Builder setLatlngAdress(String latlngAdress) {
            this.latlngAdress = latlngAdress;
            return this;
        }

        public String getMyCharacter() {
            return myCharacter;
        }

        public Builder setMyCharacter(String myCharacter) {
            this.myCharacter = myCharacter;
            return this;
        }

        public String getMyStyle() {
            return myStyle;
        }

        public Builder setMyStyle(String myStyle) {
            this.myStyle = myStyle;
            return this;
        }

        public String getTargetCharacter() {
            return targetCharacter;
        }

        public Builder setTargetCharacter(String targetCharacter) {
            this.targetCharacter = targetCharacter;
            return this;
        }

        public String getTargetStyle() {
            return targetStyle;
        }

        public Builder setTargetStyle(String targetStyle) {
            this.targetStyle = targetStyle;
            return this;
        }

        public String getTargetFood() {
            return targetFood;
        }

        public Builder setTargetFood(String targetFood) {
            this.targetFood = targetFood;
            return this;
        }
    }
}
