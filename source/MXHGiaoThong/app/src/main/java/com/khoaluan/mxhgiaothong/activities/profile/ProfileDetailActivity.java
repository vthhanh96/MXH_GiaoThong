package com.khoaluan.mxhgiaothong.activities.profile;

import android.os.Bundle;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HieuMinh on 4/7/2018.
 */

public class ProfileDetailActivity   extends DrawerActivity {

    @BindView(R.id.topBar)
    TopBarView topBar;

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
        initTopbar();
    }

    private void initTopbar() {
        topBar.setTextTitle("Nguyễn Minh Hiếu");
        topBar.setImageViewLeft(1);
//        topBar.setImageViewRight(1);
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
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


}