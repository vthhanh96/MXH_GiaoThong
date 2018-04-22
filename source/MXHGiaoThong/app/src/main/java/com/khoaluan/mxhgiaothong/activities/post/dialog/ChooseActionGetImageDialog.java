package com.khoaluan.mxhgiaothong.activities.post.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Hanh on 4/14/2018.
 */

public class ChooseActionGetImageDialog extends Dialog {

    @BindView(R.id.tvCamera) TextView mTvCamera;
    @BindView(R.id.tvLibrary) TextView mTvLibrary;

    private IChooseActionListener mListener;

    public void setOnIChooseActionListener(IChooseActionListener listener) {
        mListener = listener;
    }

    public ChooseActionGetImageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.dialog_post_action);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

    }

    @OnClick(R.id.tvCamera)
    public void onEditPost() {
        if(mListener != null) {
            mListener.onCameraClick();
        }
        dismiss();
    }

    @OnClick(R.id.tvLibrary)
    public void onHidePost() {
        if(mListener != null) {
            mListener.onLibraryClick();
        }
        dismiss();
    }

    public static interface IChooseActionListener {
        void onCameraClick();
        void onLibraryClick();
    }
}
