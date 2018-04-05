package com.khoaluan.mxhgiaothong.activities.post.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.khoaluan.mxhgiaothong.activities.post.fragments.ListNearMePostFragment;
import com.khoaluan.mxhgiaothong.activities.post.fragments.ListSelectionPostFragment;

import java.util.ArrayList;
import java.util.List;

public class ListPostFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public ListPostFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        init();
    }

    private void init() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ListSelectionPostFragment());
        mFragmentList.add(new ListNearMePostFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
