package com.khoaluan.mxhgiaothong.customView.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.utils.FontHelper;

/**
 * Created by HieuMinh on 4/18/2018.
 */

public class QuestionDialog extends BaseCustomDialogFragment {
    private TextView tvContent,txvTitle;
    private String content;
    public QuestionDialog() {
    }

    @SuppressLint("ValidFragment")
    public QuestionDialog(String content) {
        super();
        setView(R.layout.dialog_question);
        setHasAction(true);
        this.content = content;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = super.onCreateView(inflater, container, savedInstanceState);
        txvTitle  = (TextView) mainView.findViewById(R.id.txvTitle);
        tvContent = (TextView) mainView.findViewById(R.id.tvQuestion);
        txvTitle.setTypeface(FontHelper.getInstance().getTypeface(getContext(), FontHelper.FONT_QUICKSAND_BOLD));
        tvContent.setTypeface(FontHelper.getInstance().getTypeface(getContext(), FontHelper.FONT_HIRAKAKU_W3));
        setActionName("Đồng ý");
        tvContent.setText(content);
        return mainView;
    }
}
