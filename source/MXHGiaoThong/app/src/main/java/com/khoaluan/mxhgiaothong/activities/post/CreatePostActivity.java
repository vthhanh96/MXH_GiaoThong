package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.khoaluan.mxhgiaothong.R;

import butterknife.ButterKnife;

/**
 * Created by Hong Hanh on 4/4/2018.
 */

public class CreatePostActivity extends AppCompatActivity{

    public static void start(Context context) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

    }
}
