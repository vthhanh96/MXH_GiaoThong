package com.khoaluan.mxhgiaothong.restful;

import android.util.Log;

import com.khoaluan.mxhgiaothong.utils.APIErrorUtils;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hong Hanh on 3/20/2018.
 */

public abstract class RestCallback<T extends BaseResponse> implements Callback<T> {

    public abstract void success(T res);

    public abstract void failure(RestError error);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d("Response", response.message());

        if (response.isSuccessful()) {
            final T bodyResponse = response.body();
            if (bodyResponse.success) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        success(bodyResponse);
                    }
                }, 1000);
            } else {
                RestError error = new RestError(0);
                if (bodyResponse.message != null && !bodyResponse.message.equals("")) {
                    error = new RestError(bodyResponse.status, bodyResponse.message);
                } else if (bodyResponse.error != null && !bodyResponse.error.equals("")) {
                    error = new RestError(bodyResponse.status, bodyResponse.error);
                }
                failure(error);
            }
        } else {
            RestError error = new RestError(APIErrorUtils.API_ERROR_UNKNOWN, "Unknown error");
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        Log.e("OnFailure", throwable.toString());

        RestError error = null;
        if (throwable instanceof IOException) {
            if (throwable instanceof SocketTimeoutException) {
                error = new RestError(APIErrorUtils.API_ERROR_TIMED_OUT, "Request time out");
            }
        }
        if (error == null) {
            error = new RestError(APIErrorUtils.API_ERROR_UNKNOWN, "Unknown error");
        }
        failure(error);
    }

}