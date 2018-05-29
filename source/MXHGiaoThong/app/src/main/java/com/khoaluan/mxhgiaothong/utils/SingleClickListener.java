package com.khoaluan.mxhgiaothong.utils;

/**
 * Created by HieuMinh on 5/29/2018.
 */

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;


public abstract class SingleClickListener implements View.OnClickListener, Runnable{
    private static final long DELAY_INTERVAL = 600;
    private long mLastClickTime;
    private static boolean isClickable = true;

    private Handler handler = new Handler();

    @Override
    public void onClick(View v) {
        if(isClickable) {
            isClickable = false;
            startTimer();
        } else {
            return;
        }

        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if (elapsedTime <= DELAY_INTERVAL) {
            return;
        }
        onSingleClick(v);
    }

    private void startTimer() {
        handler.postDelayed(this, DELAY_INTERVAL);
    }

    @Override
    public void run() {
        isClickable = true;
    }

    public abstract void onSingleClick(View v);
}