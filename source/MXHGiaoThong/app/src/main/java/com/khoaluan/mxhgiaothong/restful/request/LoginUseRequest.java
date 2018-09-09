package com.khoaluan.mxhgiaothong.restful.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HieuMinh on 3/31/2018.
 */

public class LoginUseRequest implements Serializable {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("phone")
    @Expose
    private String phone;

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

    public LoginUseRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginUseRequest(String email, String password, String fullName, String avatar, String address, String gender, String birthday, String phone, String latlngAdress, String myCharacter, String myStyle, String targetCharacter, String targetStyle, String targetFood) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.latlngAdress = latlngAdress;
        this.myCharacter = myCharacter;
        this.myStyle = myStyle;
        this.targetCharacter = targetCharacter;
        this.targetStyle = targetStyle;
        this.targetFood = targetFood;
    }

    public LoginUseRequest(String email) {
        this.email = email;
    }
}
