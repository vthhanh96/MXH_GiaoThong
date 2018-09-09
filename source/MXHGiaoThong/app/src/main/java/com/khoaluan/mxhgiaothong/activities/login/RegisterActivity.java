package com.khoaluan.mxhgiaothong.activities.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.khoaluan.mxhgiaothong.R;

public class RegisterActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.animator_right_in, R.anim.animator_left_out);
        setUpView();
    }

    private void setUpView() {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.activity_register_viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
