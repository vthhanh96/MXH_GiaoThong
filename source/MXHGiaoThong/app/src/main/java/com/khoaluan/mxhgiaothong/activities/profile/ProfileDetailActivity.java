package com.khoaluan.mxhgiaothong.activities.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.khoaluan.mxhgiaothong.activities.post.fragments.ListSelectionPostFragment;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.ListPostByUserIdResquest;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_MENU;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_SETTING;
import static com.khoaluan.mxhgiaothong.activities.post.ListPostActivity.loginUserID;
import static com.khoaluan.mxhgiaothong.activities.post.ListPostActivity.token;

/**
 * Created by HieuMinh on 4/7/2018.
 */

public class ProfileDetailActivity   extends DrawerActivity {

    @BindView(R.id.topBar)
    TopBarView topBar;
    @BindView(R.id.rcvSelectionPost)
    RecyclerView mSelectionPostRecyclerView;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    private int userID;
    private User user;
    ListSelectionPostFragment.SelectionPostAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_detail;
    }

    @Override
    protected int getNavId() {
        return AppConstants.NAV_DRAWER_PROFILE_EDIT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        userID = getIntent().getIntExtra("UserID",-1);
        initTopbar();
        init();
    }

    private void initTopbar() {
        topBar.setTextTitle("Hạnh Văn");
        topBar.setImageViewLeft(LEFT_MENU);
        if(userID == loginUserID) {
            topBar.setImageViewRight(RIGHT_SETTING);
        }
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
            }

            @Override
            public void onImvRightClicked() {
                Intent intent = new Intent(ProfileDetailActivity.this, EditProfileActivity.class);
                intent.putExtra("userLogin",user);
                startActivity(intent);
            }

            @Override
            public void onTvLeftClicked() {
            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    private void init() {
        getInforUser();
        initRecyclerView();
    }

    private void getInforUser() {
        ApiManager.getInstance().getUserService().getUserById(token, userID).enqueue(new RestCallback<UserResponse>() {
            @Override
            public void success(UserResponse res) {
                tvUserName.setText(res.getUser().getFullName());
                getListPostUser(userID);
                user = res.getUser();
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(ProfileDetailActivity.this, ""+error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new ListSelectionPostFragment.SelectionPostAdapter();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.llLike) {

                } else if(view.getId() == R.id.llDislike){

                }
            }
        });
        mSelectionPostRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mSelectionPostRecyclerView.setAdapter(mAdapter);
    }

    private void getListPostUser(int id) {
        ApiManager.getInstance().getPostService().getPostByUserId(token, new ListPostByUserIdResquest(id)).enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(GetAllPostResponse res) {
                if(res.getPosts() != null) {
                    mAdapter.addData(res.getPosts());
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void failure(RestError error) {
                Toast.makeText(ProfileDetailActivity.this, ""+error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}