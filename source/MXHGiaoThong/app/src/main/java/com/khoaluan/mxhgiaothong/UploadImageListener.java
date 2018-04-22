package com.khoaluan.mxhgiaothong;

/**
 * Created by Hong Hanh on 4/22/2018.
 */

public interface UploadImageListener {
    public void uploadSuccess(String url);
    public void uploadFailure(String err);
}
