package com.khoaluan.mxhgiaothong.utils;

import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.khoaluan.mxhgiaothong.UploadImageListener;

import java.util.Map;

/**
 * Created by Hong Hanh on 4/22/2018.
 */

public class UploadImageUtils {
    public static void uploadImage(Uri uri, final UploadImageListener callback) {
        MediaManager.get().upload(uri).unsigned("post_preset").callback(new UploadCallback() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onProgress(String s, long l, long l1) {

            }

            @Override
            public void onSuccess(String s, Map map) {
                String imageUrl = "";
                if(map.containsKey("url")) {
                    imageUrl = map.get("url").toString();
                }
                callback.uploadSuccess(imageUrl);
            }

            @Override
            public void onError(String s, ErrorInfo errorInfo) {
                callback.uploadFailure(errorInfo.getDescription());
            }

            @Override
            public void onReschedule(String s, ErrorInfo errorInfo) {

            }
        }).dispatch();
    }
}
