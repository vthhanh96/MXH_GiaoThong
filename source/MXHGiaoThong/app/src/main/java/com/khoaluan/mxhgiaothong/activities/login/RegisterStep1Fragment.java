package com.khoaluan.mxhgiaothong.activities.login;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.utils.AppUtils;

public class RegisterStep1Fragment extends Fragment {

    public static User user;

    private View view;

    private ViewPager _mViewPager;
    private TextView tvVerifyCode;
    private Button btnSendCode, btnNext;
    private EditText edtCode, edtPhone;
    private TextInputLayout tilErrorCode, tilErrorPhone;
    private String codeAuth;
    private LinearLayout llRoot;

    public static Fragment newInstance() {
        return new RegisterStep1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step1, container, false);

        init();

        llRootTouch();

        sendCode();

        changeDataEdtPhone();

        rlNextClick();

        return view;
    }

    public void init() {
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step1_ll_root);

        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);

        btnSendCode = (Button) view.findViewById(R.id.fragment_register_step1_btn_send_code);
        btnNext = (Button) view.findViewById(R.id.fragment_register_step1_btn_next);

        edtPhone = (EditText) view.findViewById(R.id.fragment_register_step1_edt_phone);
        edtCode = (EditText) view.findViewById(R.id.fragment_register_step1_edt_code);

        tilErrorPhone = (TextInputLayout) view.findViewById(R.id.til_error_phone);
        tilErrorCode = (TextInputLayout) view.findViewById(R.id.til_error_code);

        tvVerifyCode = (TextView) view.findViewById(R.id.fragment_register_step1_tutorial_verify_code);

        user = new User();

    }

    public void sendCode() {
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideSoftKeyboard(getActivity());
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    tilErrorPhone.setError(getString(R.string.empty_mail));
                } else {
                    tvVerifyCode.setVisibility(View.VISIBLE);
                    edtCode.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
//                    ApiManager.getInstance().getUserService().forgotPassword(new LoginUseRequest(edtPhone.getText().toString())).enqueue(new RestCallback<BaseResponse>() {
//                        @Override
//                        public void success(BaseResponse res) {
//                            codeAuth = res.message;
//                            tvVerifyCode.setVisibility(View.VISIBLE);
//                            edtCode.setVisibility(View.VISIBLE);
//                            btnNext.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void failure(RestError error) {
//                            ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
//                            errorDialog.setError(error.message);
//                            errorDialog.show(getFragmentManager(), ForgotPassActivity.class.getName());
//                        }
//                    });
                }
            }
        });
    }

    public void changeDataEdtPhone() {
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tilErrorPhone.setError(null);
                tilErrorPhone.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvVerifyCode.setVisibility(View.INVISIBLE);
                edtCode.setVisibility(View.INVISIBLE);
                tilErrorCode.setErrorEnabled(false);
                btnNext.setVisibility(View.INVISIBLE);
            }
        });
    }

    /* hide soft keyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.touchOutsideHideSoftKeyboard(getActivity());
            }
        });
    }

    //IN HERE, HAVEN'T METHOD CHECK CODE
    private void rlNextClick() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                user.setEmail(edtPhone.getText().toString());
                _mViewPager.setCurrentItem(1, true);
//                if (TextUtils.isEmpty(edtCode.getText().toString())) {
//                    tilErrorCode.setError(getString(R.string.empty_verify));
//                }else if(!Objects.equals(edtCode.getText().toString(), codeAuth)) {
//                    Toast.makeText(getActivity(), "Mã xác thực không đúng?", Toast.LENGTH_SHORT).show();
//                } else {
//                    user.setEmail(edtPhone.getText().toString());
//                    _mViewPager.setCurrentItem(1, true);
//                }
            }
        });
    }
}
