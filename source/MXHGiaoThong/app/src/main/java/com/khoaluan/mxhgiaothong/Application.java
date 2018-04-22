package com.khoaluan.mxhgiaothong;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cloudinary.android.MediaManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.khoaluan.mxhgiaothong.backgroundService.BackgroundService;


public class Application extends android.app.Application {

    private static Context mContext;
    private static BackgroundService mBackgroundService;

    public static Context getContext() {
        return mContext;
    }

    public static BackgroundService getBackgroundService() {
        return mBackgroundService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        init();
    }

    @Override
    public void onTerminate() {
        unbindService(mServiceConnection);
        super.onTerminate();
    }

    private void init() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        MediaManager.init(this);
        startBackgroundService();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundService.BackgroundBinder binder = (BackgroundService.BackgroundBinder) service;
            mBackgroundService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void startBackgroundService() {
        Intent intentService = new Intent(this, BackgroundService.class);
        startService(intentService);
        bindService(intentService, mServiceConnection, Service.BIND_AUTO_CREATE);
    }
}
