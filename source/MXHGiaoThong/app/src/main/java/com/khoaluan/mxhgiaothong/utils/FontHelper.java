package com.khoaluan.mxhgiaothong.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by HieuMinh on 4/17/2018.
 */

public class FontHelper {
    private static final FontHelper ourInstance = new FontHelper();

    public static final String FONT_HIRAKAKU_W3 = "hiragino-kaku-gothic-pro-w3.otf";
    public static final String FONT_HIRAKAKU_W6 = "hiragino-kaku-gothic-pro-w6.otf";
    public static final String FONT_QUICKSAND = "Quicksand-Regular.ttf";
    public static final String FONT_QUICKSAND_BOLD = "Quicksand-Bold.ttf";

    protected final Hashtable<String, Typeface> mTypefaceCache = new Hashtable<>();

    public static FontHelper getInstance() {
        return ourInstance;
    }
    private FontHelper() {
    }

    public Typeface getTypeface(Context context, String typefaceName) {
        if (!mTypefaceCache.containsKey(typefaceName)) {
            Typeface typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/" + typefaceName);
            if (typeface != null) {
                mTypefaceCache.put(typefaceName, typeface);
            }
        }
        if (mTypefaceCache.size() > 0) {
            return mTypefaceCache.get(typefaceName);
        } else {
            return null;
        }
    }
}