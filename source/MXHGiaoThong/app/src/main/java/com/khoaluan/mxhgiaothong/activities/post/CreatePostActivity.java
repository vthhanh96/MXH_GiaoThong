package com.khoaluan.mxhgiaothong.activities.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.Application;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.request.CreatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.CreatePostResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetUserInfoResponse;
import com.khoaluan.mxhgiaothong.utils.GeoHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Hanh on 4/4/2018.
 */

public class CreatePostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_CATEGORY = 10;

    @BindView(R.id.topBar)
    TopBarView mTopBar;
    @BindView(R.id.imgAvatar)
    ImageView mImgAvatar;
    @BindView(R.id.edt_content)
    EditText mEdtContent;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvPlace)
    TextView mTvPlace;
    @BindView(R.id.tvCategory)
    TextView mTvCategory;

    private Context mContext;
    private String token;
    private Location mLocation;
    private String mImageUrl;
    private Category mCategory;
    private int mLevel;

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
        token = sharedPreferences.getString("token", "");
    }

    private void initTopBar() {
        mTopBar.setImageViewLeft(AppConstants.LEFT_BACK);
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
        if (mLocation != null) {
            mTvPlace.setText(GeoHelper.getAddress(mLocation));
            Toast.makeText(mContext, mLocation.getLatitude() + ";" + mLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            mTvPlace.setText("Chưa xác định");
            Toast.makeText(mContext, "Can not get current location", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tvPostAction)
    public void createPost() {
        if(mCategory == null) return;
        CreatePostRequest request = new CreatePostRequest(
                mEdtContent.getText().toString(),
                mLocation.getLatitude(),
                mLocation.getLongitude(),
                mTvPlace.getText().toString(),
                mCategory.getId(),
                mLevel,
                true,
                mImageUrl);

        ApiManager.getInstance().getPostService().createPost(token, request).enqueue(new RestCallback<CreatePostResponse>() {
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

    private void updateUICategory() {
        mTvCategory.setVisibility(View.VISIBLE);
        mTvCategory.setText(mCategory.getName());
        switch (mLevel) {
            case 0:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_green);
                break;
            case 1:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_yellow);
                break;
            case 2:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_orange);
                break;
            case 3:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_red);
                break;
        }
    }

    @OnClick(R.id.imgCategory)
    public void chooseCategory() {
        ChooseCategoryActivity.start(this, REQUEST_CODE_CHOOSE_CATEGORY, mCategory, mLevel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CHOOSE_CATEGORY) {
            mCategory = (Category) data.getSerializableExtra(AppConstants.ARG_KEY_CATEGORY_ID);
            mLevel = data.getIntExtra(AppConstants.ARG_KEY_LEVEL, 0);
            updateUICategory();
        }
    }
}
