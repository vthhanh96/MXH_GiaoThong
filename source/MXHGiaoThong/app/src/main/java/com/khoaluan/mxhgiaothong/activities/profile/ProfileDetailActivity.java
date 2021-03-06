package com.khoaluan.mxhgiaothong.activities.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.ListPostByUserIdResquest;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserResponse;
import com.khoaluan.mxhgiaothong.view.ListPostView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_BACK;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_MESSAGE;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_SETTING;
import static com.khoaluan.mxhgiaothong.activities.post.ListPostActivity.token;

/**
 * Created by HieuMinh on 4/7/2018.
 */

public class ProfileDetailActivity   extends DrawerActivity {

    private static final String ARG_KEY_USER_ID = "ARG_KEY_USER_ID";

    public static void start(Context context, Integer userId) {
        Intent starter = new Intent(context, ProfileDetailActivity.class);
        starter.putExtra(ARG_KEY_USER_ID, userId);
        context.startActivity(starter);
    }

    @BindView(R.id.topBar) TopBarView topBar;
    @BindView(R.id.listPostView) ListPostView mListPostView;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.imvAvatar) CircleImageView imvAvatar;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.tvNumPost) TextView tvNumPost;
    @BindView(R.id.tvAge) TextView tvAge;

    private int userID;
    private User user;
    private User userLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_detail;
    }

    @Override
    protected int getNavId() {
        return AppConstants.NAV_DRAWER_PROFILE_DETAIL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        lockDrawer();
        userID = getIntent().getIntExtra(ARG_KEY_USER_ID,-1);
        userLogin = PreferManager.getInstance(ProfileDetailActivity.this).getUser();
        init();
    }

    private void init() {
        initTopbar();
    }

    private void initTopbar() {
        topBar.setImageViewLeft(LEFT_BACK);
        if(userID == userLogin.getId()) {
            topBar.setImageViewRight(RIGHT_SETTING);
        }else {
            topBar.setImageViewRight(RIGHT_MESSAGE);
        }
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                finish();
            }

            @Override
            public void onImvRightClicked() {
                if(userID == userLogin.getId()) {
                    Intent intent = new Intent(ProfileDetailActivity.this, EditProfileActivity.class);
                    intent.putExtra("userLogin",user);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProfileDetailActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTvLeftClicked() {
            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInforUser();
    }

    private void getInforUser() {
        ApiManager.getInstance().getUserService().getUserById(token, userID).enqueue(new RestCallback<UserResponse>() {
            @Override
            public void success(UserResponse res) {
                tvUserName.setText(res.getUser().getFullName());
                Glide.with(getApplicationContext()).load(res.getUser().getAvatar()).apply(RequestOptions.circleCropTransform()).into(imvAvatar);
                tvAddress.setText(res.getUser().getAddress());
                tvAge.setText(res.getUser().getBirthday());
                getListPostUser(userID);
                user = res.getUser();
            }

            @Override
            public void failure(RestError error) {
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), ProfileDetailActivity.class.getName());
            }
        });
    }

    private void getListPostUser(int id) {
        ApiManager.getInstance().getPostService().getPostByUserId(token, new ListPostByUserIdResquest(id)).enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(GetAllPostResponse res) {
                if(res.getPosts() != null) {
                    mListPostView.setData(res.getPosts());
                    tvNumPost.setText(String.valueOf(res.getPosts().size()));
                }
            }
            @Override
            public void failure(RestError error) {
                try{
                    ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                    errorDialog.setError(error.message);
                    errorDialog.show(getSupportFragmentManager(), ProfileDetailActivity.class.getName());
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}