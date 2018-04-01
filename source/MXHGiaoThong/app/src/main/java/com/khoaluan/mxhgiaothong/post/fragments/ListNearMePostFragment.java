package com.khoaluan.mxhgiaothong.post.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khoaluan.mxhgiaothong.R;

import butterknife.ButterKnife;

public class ListNearMePostFragment extends Fragment {

    private Context mContext;

    public ListNearMePostFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_near_me_post, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        init();
        return view;
    }

    private void init() {

    }
}
