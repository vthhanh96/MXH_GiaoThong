package com.khoaluan.mxhgiaothong.activities.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.utils.AppUtils;

import static com.khoaluan.mxhgiaothong.activities.login.RegisterStep1Fragment.user;


public class RegisterStep3Fragment extends Fragment {

    private View view;
//    private StorageReference storageRefImage;
    private ImageView imgWomanAvatar, imgManAvatar, imgAvatar;
    private RelativeLayout rltWomanAvatar, rltManAvatar, rltAvatar;
    private Button btnNext;
    private String gender;
    private ViewPager _mViewPager;
    private TextView tvTutorial, tvTitile;
    private LinearLayout llRoot;
    private boolean isChosen = false;

    public static Fragment newInstance() {
        return new RegisterStep3Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step3, container, false);
        init();
        llRootTouch();
        setClickGender();
        setClickAvatar();
        clickButtonNextStep();
        return view;
    }

    //Khoi tao gia tri cho cac bien
    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rltManAvatar = (RelativeLayout) view.findViewById(R.id.fragment_register_step3_rlt_man_avatar);
        rltWomanAvatar = (RelativeLayout) view.findViewById(R.id.fragment_register_step3_rlt_woman_avatar);
        rltAvatar = (RelativeLayout) view.findViewById(R.id.fragment_register_step3_rlt_avatar);
        imgManAvatar = (ImageView) view.findViewById(R.id.fragment_register_step3_img_avatar_man);
        imgWomanAvatar = (ImageView) view.findViewById(R.id.fragment_register_step3_img_avatar_woman);
        imgAvatar = (ImageView) view.findViewById(R.id.fragment_register_step3_img_avatar);
        btnNext = (Button) view.findViewById(R.id.fragment_register_step3_btn_next);
        tvTutorial = (TextView) view.findViewById(R.id.fragment_register_step3_tv_tutorial);
        tvTitile = (TextView) view.findViewById(R.id.fragment_register_step3_tv_title);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step3_ll_root);
        rltAvatar.setVisibility(View.GONE);
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.touchOutsideHideSoftKeyboard(getActivity());
            }
        });
    }

    private void setClickGender() {
        imgManAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltManAvatar, R.drawable.avatar_man);
                gender = getString(R.string.man);
                tvTitile.setText(getString(R.string.register_step_3_title_2));
                if (tvTutorial.getText().toString().equals(getResources().getString(R.string.empty_gender))) {
                    tvTutorial.setText("");
                } else tvTutorial.setText(getString(R.string.register_step_3_suggest_2));
            }
        });
        imgWomanAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltWomanAvatar, R.drawable.avatar_woman);
                gender = getString(R.string.woman);
                tvTitile.setText(getString(R.string.register_step_3_title_2));
                if (tvTutorial.getText().toString().equals(getResources().getString(R.string.empty_gender))) {
                    tvTutorial.setText("");
                } else tvTutorial.setText(getString(R.string.register_step_3_suggest_2));
            }
        });
    }

    private void animationImageAvatar(RelativeLayout re, int sourceImg) {
        rltManAvatar.setVisibility(View.GONE);
        rltWomanAvatar.setVisibility(View.GONE);
        rltAvatar.setVisibility(View.VISIBLE);
        imgAvatar.setImageResource(sourceImg);
        TranslateAnimation animation = new TranslateAnimation(imgManAvatar.getX(), imgAvatar.getX(), re.getY(), re.getY());
        animation.setDuration(100000);
        animation.setFillAfter(false);
        rltAvatar.startAnimation(animation);
    }


    private void setClickAvatar() {
        rltAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void clickButtonNextStep() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender == null) {
                } else {
                    user.setGender((gender));
                    _mViewPager.setCurrentItem(3, true);
                }
            }
        });
    }
}
