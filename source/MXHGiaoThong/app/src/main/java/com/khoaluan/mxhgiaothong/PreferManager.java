package com.khoaluan.mxhgiaothong;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.khoaluan.mxhgiaothong.restful.model.User;

/**
 * Created by Hong Hanh on 4/15/2018.
 */

public class PreferManager {
    private static PreferManager preferManager;
    private static User mUser;
    private static String mToken;

    public static PreferManager getInstance(Context context) {
        if(preferManager == null) {
            preferManager = new PreferManager(context);
        }
        return preferManager;
    }

    private SharedPreferences mPreferences;

    private PreferManager(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    ////////////////////Token//////////////////////
    private static String KEY_TOKEN = "KEY_TOKEN";

    public void saveToken(String token) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if(TextUtils.isEmpty(token)) {
            editor.remove(KEY_TOKEN);
        } else {
            editor.putString(KEY_TOKEN, token);
        }
        editor.apply();
    }

    public String getToken() {
        if(mToken != null) return mToken;
        mToken = mPreferences.getString(KEY_TOKEN, null);
        return mToken;
    }

    ///////////////////User_ID////////////////////
    private static String KEY_USER_ID = "KEY_USER_ID";

    public void saveUserId(int userId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return mPreferences.getInt(KEY_USER_ID, 0);
    }

    ///////////////////User//////////////////////
    private static String KEY_USER = "KEY_USER";

    public void saveUser(User user) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if(user == null) {
            editor.remove(KEY_USER);
        } else {
            editor.putString(KEY_USER, new Gson().toJson(user));
        }
        editor.apply();
    }

    public User getUser() {
        if(mUser != null) return mUser;
        if (mPreferences.contains(KEY_USER)) {
            try {
                mUser = new Gson().fromJson(mPreferences.getString(KEY_USER, new Gson().toJson(new User())), User.class);
            } catch (Exception ignored) {
                mUser = null;
            }
        }
        return mUser;
    }
}
