package com.khoaluan.mxhgiaothong.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HieuMinh on 3/30/2018.
 */

public class ForgotPassActivity extends AppCompatActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.ln_enter_mail)
    LinearLayout lnEnterMail;

    @BindView(R.id.ln_reset_pass)
    LinearLayout lnResetPass;

    @BindView(R.id.edt_code)
    EditText edtCode;

    @BindView(R.id.edt_new_password)
    EditText edtNewPass;

    @BindView(R.id.edt_confirm_new_password)
    EditText edtConfirmNewPass;

    private String codeAuth;

    @OnClick(R.id.btn_enter_mail)
    public void GetCodeAuth(){
        ApiManager.getInstance().getUserService().forgotPassword(new LoginUseRequest(edtEmail.getText().toString())).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                lnEnterMail.setVisibility(View.GONE);
                lnResetPass.setVisibility(View.VISIBLE);
                codeAuth = res.message;
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(ForgotPassActivity.this, ""+error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btn_reset_pass)
    public void ResetPass(){
        if(!Objects.equals(edtCode.getText().toString(), codeAuth)){
            Toast.makeText(this, "Mã xác thực không đúng?", Toast.LENGTH_SHORT).show();
        } else if(!Objects.equals(edtNewPass.getText().toString(), edtConfirmNewPass.getText().toString())){
            Toast.makeText(this, "Nhập lại mật khẩu không khớp?", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Xử lí đặt lại mật khẩu tại đây", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(lnEnterMail.getVisibility() == View.GONE){
            lnEnterMail.setVisibility(View.VISIBLE);
            lnResetPass.setVisibility(View.GONE);
            codeAuth = "";
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }
}
