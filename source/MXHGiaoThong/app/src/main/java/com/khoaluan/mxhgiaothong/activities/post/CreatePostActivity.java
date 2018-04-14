package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.khoaluan.mxhgiaothong.Application;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.CreatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.CreatePostResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetUserInfoResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Hanh on 4/4/2018.
 */

public class CreatePostActivity extends AppCompatActivity{

    @BindView(R.id.topBar)
    TopBarView mTopBar;
    @BindView(R.id.imgAvatar)
    ImageView mImgAvatar;
    @BindView(R.id.edt_content)
    EditText mEdtContent;
    @BindView(R.id.tvName)
    TextView mTvName;

    private Context mContext;
    private String token;
    private Location mLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public static void start(Context context) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        getToken();
        initTopBar();
        getUserInfo();
        getCurrentLocation();
    }

    private void getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("data_token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
    }

    private void initTopBar() {
        mTopBar.setImageViewLeft(R.drawable.ic_back);
        mTopBar.setTextViewRight("");
        mTopBar.setTextTitle("Đăng bài viết");
        mTopBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                finish();
            }

            @Override
            public void onImvRightClicked() {

            }

            @Override
            public void onTvLeftClicked() {

            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    private void getUserInfo() {
        ApiManager.getInstance().getUserService().getUserInfo(token).enqueue(new RestCallback<GetUserInfoResponse>() {
            @Override
            public void success(GetUserInfoResponse res) {
                Glide.with(mContext).load(res.getUser().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
                mTvName.setText(res.getUser().getFullName());
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }

    private void getCurrentLocation() {
        mLocation = Application.getBackgroundService().getCurrentLocation();
        if(mLocation != null) {
            Toast.makeText(mContext, mLocation.getLatitude() + ";" + mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Can not get current location", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tvPostAction)
    public void createPost() {
        CreatePostRequest request = new CreatePostRequest(mEdtContent.getText().toString(),
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
