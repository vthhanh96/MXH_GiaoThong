package com.khoaluan.mxhgiaothong.activities.post.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListSelectionPostFragment extends Fragment {

    @BindView(R.id.rcvSelectionPost)
    RecyclerView mSelectionPostRecyclerView;

    private Context mContext;
    SelectionPostAdapter mAdapter;

    public ListSelectionPostFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_selection_post, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        init();
        return view;
    }

    private void init() {
        initRecyclerView();
        getAllPost();
    }

    private void initRecyclerView() {
        mAdapter = new SelectionPostAdapter();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.llLike) {

                } else if(view.getId() == R.id.llDislike){

                }
            }
        });
        mSelectionPostRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSelectionPostRecyclerView.setAdapter(mAdapter);
    }

    private void getAllPost() {
        ApiManager.getInstance().getPostService().getAllPost().enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(GetAllPostResponse res) {
                if(res.getPosts() != null) {
                    mAdapter.addData(res.getPosts());
                }
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }

    public class SelectionPostAdapter extends BaseQuickAdapter<Post, BaseViewHolder>{

        public SelectionPostAdapter() {
            super(R.layout.item_post, new ArrayList<Post>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Post item) {
            helper.setText(R.id.tvName, item.getCreator().getFullName());
            helper.setText(R.id.tvPlace, item.getPlace());
            helper.setText(R.id.tvTime, DateUtils.getTimeAgo(mContext, item.getCreatedDate()));
            helper.setText(R.id.tvContent, item.getContent());
            helper.setText(R.id.tvLikeAmount, String.valueOf(item.getLikeAmount()));
            helper.setText(R.id.tvDislikeAmount, String.valueOf(item.getDislikeAmount()));

            ImageView imgAvatar = helper.getView(R.id.imgAvatar);
            ImageView imgContent = helper.getView(R.id.imgContent);

            Glide.with(mContext).load(item.getCreator().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(imgAvatar);
            Glide.with(mContext).load(item.getImageUrl()).into(imgContent);

            helper.addOnClickListener(R.id.llLike);
            helper.addOnClickListener(R.id.llDislike);
        }
    }
}
