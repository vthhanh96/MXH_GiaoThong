package com.khoaluan.mxhgiaothong.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HieuMinh on 3/30/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;

    @BindView(R.id.edt_full_name)
    EditText edtFullName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btn_register)
    public void Register(){
        if(!Objects.equals(edtPassword.getText().toString(), edtConfirmPassword.getText().toString())){
            Toast.makeText(this, "Nhập lại mật khẩu không khớp !", Toast.LENGTH_SHORT).show();
        } else {
            ApiManager.getInstance().getUserService().register(new LoginUseRequest(edtEmail.getText().toString(),edtPassword.getText().toString(),edtFullName.getText().toString(),"")).enqueue(new RestCallback<BaseResponse>() {
                @Override
                public void success(BaseResponse res) {
                    Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void failure(RestError error) {
                    Toast.makeText(RegisterActivity.this, ""+error.message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
