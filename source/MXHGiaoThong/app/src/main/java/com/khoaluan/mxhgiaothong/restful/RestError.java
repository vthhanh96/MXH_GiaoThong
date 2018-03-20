package com.khoaluan.mxhgiaothong.restful;

import org.json.JSONObject;

import java.io.Serializable;

public class RestError {

    public int code;

    public String message;

    public RestError(int statusCode) {
        this.code = statusCode;
    }

    public RestError(int statusCode, String message) {
        this.code = statusCode;
        this.message = message;
    }
}
