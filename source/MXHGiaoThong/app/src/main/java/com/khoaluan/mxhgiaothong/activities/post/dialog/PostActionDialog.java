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

public class PostActionDialog extends Dialog {

    @BindView(R.id.tvEditPost) TextView mTvEditPost;
    @BindView(R.id.tvDeletePost) TextView mTvDeletePost;

    private IChooseActionListener mListener;

    private boolean isEnableEdit;
    private boolean isEnableDelete;

    public void setOnIChooseActionListener(IChooseActionListener listener) {
        mListener = listener;
    }

    public void setEnableEditAction(boolean isEnable) {
        isEnableEdit = isEnable;
    }

    public void setEnableDeleteAction(boolean isEnable) {
        isEnableDelete = isEnable;
    }

    public PostActionDialog(@NonNull Context context) {
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
        mTvEditPost.setEnabled(isEnableEdit);
        mTvDeletePost.setEnabled(isEnableDelete);
    }

    @OnClick(R.id.tvEditPost)
    public void onEditPost() {
        if(mListener != null) {
            mListener.onEditPostClick();
        }
        dismiss();
    }

    @OnClick(R.id.tvHidePost)
    public void onHidePost() {
        if(mListener != null) {
            mListener.onHidePostClick();
        }
        dismiss();
    }

    @OnClick(R.id.tvDeletePost)
    public void onDeletePost() {
        if(mListener != null) {
            mListener.onDeletePostClick();
        }
        dismiss();
    }

    public static interface IChooseActionListener {
        void onEditPostClick();
        void onHidePostClick();
        void onDeletePostClick();
    }
}
