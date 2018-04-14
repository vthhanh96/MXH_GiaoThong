package com.khoaluan.mxhgiaothong.activities.profile;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;

/**
 * Created by HieuMinh on 4/13/2018.
 */

public class EditProfileActivity extends DrawerActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected int getNavId() {
        return AppConstants.NAV_DRAWER_PROFILE_EDIT;
    }

}
