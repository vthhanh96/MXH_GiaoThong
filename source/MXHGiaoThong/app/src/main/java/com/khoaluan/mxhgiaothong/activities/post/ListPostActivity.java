package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.login.LoginActivity;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.activities.post.adapter.ListPostFragmentPagerAdapter;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListPostActivity extends DrawerActivity {

    @BindView(R.id.tabLayoutPost)
    TabLayout mTabLayoutPost;
    @BindView(R.id.vpPost)
    ViewPager mViewPagerPost;
    @BindView(R.id.topBar)
    TopBarView topBar;

    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_post;
    }

    @Override
    protected int getNavId() {
        return AppConstants.NAV_DRAWER_LIST_POST;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ListPostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();

        init();
    }

    private void init() {
        initViewPager();
        initTabLayout();
        initTopbar();
    }

    private void initTopbar() {
        topBar.setTextTitle("NetFic");
        topBar.setImageViewLeft(1);
        topBar.setImageViewRight(1);
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
            }

            @Override
            public void onImvRightClicked() {
                Toast.makeText(ListPostActivity.this, "Logout Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTvLeftClicked() {
                Toast.makeText(ListPostActivity.this, "Back", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    private void initViewPager() {
        mViewPagerPost.setAdapter(new ListPostFragmentPagerAdapter(getSupportFragmentManager()));
    }

    private void initTabLayout() {
        mTabLayoutPost.setupWithViewPager(mViewPagerPost);
        String[] tabNamesIds = {"Chọn lọc", "Gần tôi"};

        for (int i = 0; i < tabNamesIds.length; i++) {
            TabLayout.Tab tab = mTabLayoutPost.getTabAt(i);
            if (tab == null) {
                return;
            }
            tab.setText(tabNamesIds[i]);
        }
    }

    @OnClick(R.id.btnCreatePost)
    public void createPost() {
        SharedPreferences sharedPreferences = getSharedPreferences("data_token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        if(TextUtils.isEmpty(token)) {
            Intent intent = new Intent(ListPostActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            CreatePostActivity.start(ListPostActivity.this);
        }
    }

}
