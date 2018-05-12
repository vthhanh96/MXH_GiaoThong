package com.khoaluan.mxhgiaothong.customView.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;

public class CustomProgressDialog extends ProgressDialog {

    private String title;

    public CustomProgressDialog(Context context, String title) {
        super(context, R.style.CustomDialog);
        this.title = title;
    }

    public CustomProgressDialog(Context context, int theme, String title) {
        super(context, theme);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_custom_progress);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }
}