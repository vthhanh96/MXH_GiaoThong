package com.khoaluan.mxhgiaothong.activities.post.items;

import com.khoaluan.mxhgiaothong.view.ActionSheet.BottomSheet;

/**
 * Created by Hong Hanh on 5/10/2018.
 */

public class CategoryFilter extends BottomSheet.BottomSheetItem {

    public String mId;
    public String mName;
    public int mLevel;

    public CategoryFilter(String id, String name, int level) {
        mId = id;
        mName = name;
        mLevel = level;
    }
}
