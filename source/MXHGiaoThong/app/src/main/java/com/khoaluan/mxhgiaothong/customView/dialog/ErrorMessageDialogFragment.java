package com.khoaluan.mxhgiaothong.customView.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.utils.FontHelper;

/**
 * Created by HieuMinh on 4/17/2018.
 */

public class ErrorMessageDialogFragment extends BaseCustomDialogFragment {
    private TextView txvTitle;
    private TextView txvMessage;
    private ErrorMessageDialogListener errorMessageDialogListener;

    private String error;

    public ErrorMessageDialogFragment() {
        super();
        setView(R.layout.dialog_error_message);
        setHasAction(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView =  super.onCreateView(inflater, container, savedInstanceState);
        txvTitle  = (TextView) mainView.findViewById(R.id.txvTitle);
        txvMessage = (TextView) mainView.findViewById(R.id.txvErrorMessage);

        setFont();
        return mainView;
    }

    private void setFont() {
        txvTitle.setTypeface(FontHelper.getInstance().getTypeface(getContext(), FontHelper.FONT_QUICKSAND_BOLD));
        txvMessage.setTypeface(FontHelper.getInstance().getTypeface(getContext(), FontHelper.FONT_HIRAKAKU_W3));
        txvMessage.setText(error);
    }

    public void setError(String error) {
        this.error = error;
        if (txvMessage != null) {
            txvMessage.setText(this.error);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (errorMessageDialogListener != null){
            errorMessageDialogListener.cancel();
        }
    }

    public void setErrorMessageDialogListener(ErrorMessageDialogListener errorMessageDialogListener) {
        this.errorMessageDialogListener = errorMessageDialogListener;
    }

    public interface ErrorMessageDialogListener{
        void cancel();
    }
}