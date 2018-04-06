package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.CreatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.CreatePostResponse;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.tvPostAction)
    public void createPost() {
        CreatePostRequest request = new CreatePostRequest("Chiều 7/2 (22 tháng Chạp), khu vực Xa lộ Hà Nội kẹt cứng, rối loạn nhiều giờ. Trong đó nặng nhất là đoạn từ cầu Rạch Chiếc đến ngã tư Bình Thái, theo hướng từ quận 2 về quận 9.",
                123.12,
                145.24,
                "Ngã tư Bình Thái",
                4,
                3,
                true,
                "https://i-vnexpress.vnecdn.net/2018/02/07/ketxeXLHN-2-1518005765_680x0.jpg");
        ApiManager.getInstance().getPostService().createPost("", request).enqueue(new RestCallback<CreatePostResponse>() {
            @Override
            public void success(CreatePostResponse res) {
                Toast.makeText(CreatePostActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(CreatePostActivity.this, error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
