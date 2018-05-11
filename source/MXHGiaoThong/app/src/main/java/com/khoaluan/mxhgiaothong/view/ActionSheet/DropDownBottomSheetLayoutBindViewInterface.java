package com.khoaluan.mxhgiaothong.view.ActionSheet;

import android.view.View;

public interface DropDownBottomSheetLayoutBindViewInterface<K extends BottomSheet.BottomSheetItem> {
    void onBindContentView(View view, K item);
}
