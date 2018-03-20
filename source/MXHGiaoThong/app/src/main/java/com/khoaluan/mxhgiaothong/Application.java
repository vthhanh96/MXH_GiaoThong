package com.khoaluan.mxhgiaothong;

import android.content.Context;


public class Application extends android.app.Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        init();
    }

    private void init() {
    }

}
