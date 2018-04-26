package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.login.LoginActivity;
import com.khoaluan.mxhgiaothong.activities.post.adapter.ListPostFragmentPagerAdapter;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_MENU;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_LOGOUT;

public class ListPostActivity extends DrawerActivity {

    @BindView(R.id.tabLayoutPost)
    TabLayout mTabLayoutPost;
    @BindView(R.id.vpPost)
    ViewPager mViewPagerPost;
    @BindView(R.id.topBar)
    TopBarView topBar;

    public static String token;
    public static int loginUserID;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        token = PreferManager.getInstance(ListPostActivity.this).getToken();
        loginUserID = PreferManager.getInstance(ListPostActivity.this).getUserId();

        if(token == null || Objects.equals(token, "")){
            startActivity(new Intent(ListPostActivity.this, LoginActivity.class));
        } else {
            Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
        }
        init();
    }

    private void init() {
        initViewPager();
        initTabLayout();
        initTopbar();
    }

    private void initTopbar() {
        topBar.setImageViewLeft(LEFT_MENU);
        topBar.setImageViewRight(RIGHT_LOGOUT);
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
            }

            @Override
            public void onImvRightClicked() {

                final QuestionDialog questionDialog = new QuestionDialog("Bạn có chắc chắn muốn đăng xuất?");
                questionDialog.setDialogActionListener(new CustomDialogActionListener() {
                    @Override
                    public void dialogCancel() {
                        questionDialog.dismissDialog();
                    }

                    @Override
                    public void dialogPerformAction() {
                        PreferManager.getInstance(ListPostActivity.this).saveToken(null);
                        PreferManager.getInstance(ListPostActivity.this).saveUser(null);

                        Intent intent = new Intent(ListPostActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                questionDialog.show(getSupportFragmentManager(), ListPostActivity.class.getName());
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
        token = PreferManager.getInstance(ListPostActivity.this).getToken();
        if(TextUtils.isEmpty(token)) {
            Intent intent = new Intent(ListPostActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            CreatePostActivity.start(ListPostActivity.this, null);
        }
    }

}
