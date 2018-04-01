package com.khoaluan.mxhgiaothong.post.fragments;

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
import com.khoaluan.mxhgiaothong.restful.model.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListSelectionPostFragment extends Fragment {

    @BindView(R.id.rcvSelectionPost)
    RecyclerView mSelectionPostRecyclerView;

    private Context mContext;

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
    }

    private void initRecyclerView() {
        List<Post> list = new ArrayList<>();
        list.add(new Post("Content", "Hồng Hạnh", "Tân Sơn Nhất", "2 giờ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTY21TZYP6MHF8xIcMbswfaWjaNNR5wEwn0yWj9Q0ysC1WoWdaF", "https://www.petmd.com/sites/default/files/petmd-cat-happy-10.jpg"));
        list.add(new Post("Another content", "Hồng Hạnh", "Quận 9", "10 giờ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTY21TZYP6MHF8xIcMbswfaWjaNNR5wEwn0yWj9Q0ysC1WoWdaF", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Felis_catus-cat_on_snow.jpg/220px-Felis_catus-cat_on_snow.jpg"));

        SelectionPostAdapter adapter = new SelectionPostAdapter();
        adapter.addData(list);

        mSelectionPostRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSelectionPostRecyclerView.setAdapter(adapter);
    }

    public class SelectionPostAdapter extends BaseQuickAdapter<Post, BaseViewHolder>{

        public SelectionPostAdapter() {
            super(R.layout.item_post, new ArrayList<Post>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Post item) {
            helper.setText(R.id.tvName, item.mName);
            helper.setText(R.id.tvPlace, item.mPlace);
            helper.setText(R.id.tvTime, item.mTime);
            helper.setText(R.id.tvContent, item.mPostContent);

            ImageView imgAvatar = helper.getView(R.id.imgAvatar);
            ImageView imgContent = helper.getView(R.id.imgContent);

            Glide.with(mContext).load(item.mUrlAvatar).apply(RequestOptions.circleCropTransform()).into(imgAvatar);
            Glide.with(mContext).load(item.mUrlImageContent).into(imgContent);
        }
    }
}
