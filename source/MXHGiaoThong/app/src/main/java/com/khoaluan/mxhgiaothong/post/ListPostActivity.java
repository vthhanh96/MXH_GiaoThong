package com.khoaluan.mxhgiaothong.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.post.adapter.ListPostFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListPostActivity extends AppCompatActivity {

    @BindView(R.id.tabLayoutPost)
    TabLayout mTabLayoutPost;
    @BindView(R.id.vpPost)
    ViewPager mViewPagerPost;

    public static void start(Context context) {
        Intent intent = new Intent(context, ListPostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_post);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        initViewPager();
        initTabLayout();
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

}
