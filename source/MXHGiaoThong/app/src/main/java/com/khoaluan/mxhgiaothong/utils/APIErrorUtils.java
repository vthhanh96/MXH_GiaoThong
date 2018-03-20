package com.khoaluan.mxhgiaothong.utils;

import android.content.Context;

import com.khoaluan.mxhgiaothong.R;

/**
 * Created by Hong Hanh on 3/20/2018.
 */

public class APIErrorUtils {
    public static final int API_ERROR_NO_NETWORK = -1;
    public static final int API_ERROR_TIMED_OUT = -2;
    public static final int API_ERROR_SERVER = -3;
    public static final int API_ERROR_UNKNOWN = -4;

    public static String getErrorMessage(Context context, int errorCode) {
        switch (errorCode) {
            case API_ERROR_NO_NETWORK:
                return context.getString(R.string.api_err_no_network);
            case API_ERROR_TIMED_OUT:
                return context.getString(R.string.api_err_time_out);
            case API_ERROR_UNKNOWN:
                return context.getString(R.string.api_err_unknown_error);
        }
        return "";
    }
}
