package com.khoaluan.mxhgiaothong.activities.post.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.profile.ProfileDetailActivity;
import com.khoaluan.mxhgiaothong.activities.post.CreatePostActivity;
import com.khoaluan.mxhgiaothong.activities.post.dialog.PostActionDialog;
import com.khoaluan.mxhgiaothong.adapter.PostAdapter;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.Reaction;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.DoReactionRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListSelectionPostFragment extends Fragment {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.rcvSelectionPost)
    RecyclerView mSelectionPostRecyclerView;

    private Context mContext;
    PostAdapter mAdapter;
    private User mUser;
    private String token;

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
        getUser();
        initRefresh();
        initRecyclerView();
        getAllPost();
    }

    private void getUser() {
        mUser = PreferManager.getInstance(mContext).getUser();
        token = PreferManager.getInstance(mContext).getToken();
    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPost();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new PostAdapter(mUser);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.llLike) {
                    doReaction(mAdapter.getItem(position), 1);

                } else if(view.getId() == R.id.llDislike){
                    doReaction(mAdapter.getItem(position), 2);

                } else if(view.getId() == R.id.imgAvatar){
                    Intent intent = new Intent(getActivity(),ProfileDetailActivity.class);
                    intent.putExtra("UserID",mAdapter.getItem(position).getCreator().getId());
                    startActivity(intent);

                } else if(view.getId() == R.id.imgPostOptions) {
                    openOptionsDialog(mAdapter.getData().get(position));

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
                mRefreshLayout.setRefreshing(false);
                if(res.getPosts() != null) {
                    mAdapter.setNewData(res.getPosts());
                }
            }

            @Override
            public void failure(RestError error) {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void openOptionsDialog(final Post post) {
        PostActionDialog dialog = new PostActionDialog(mContext);
        if(TextUtils.isEmpty(token) || mUser == null || mUser.getId() != post.getCreator().getId()) {
            dialog.setEnableEditAction(false);
            dialog.setEnableDeleteAction(false);
        } else {
            dialog.setEnableEditAction(true);
            dialog.setEnableDeleteAction(true);
        }
        dialog.setOnIChooseActionListener(new PostActionDialog.IChooseActionListener() {
            @Override
            public void onEditPostClick() {
                CreatePostActivity.start(mContext, post);
            }

            @Override
            public void onHidePostClick() {

            }

            @Override
            public void onDeletePostClick() {
                String token = PreferManager.getInstance(mContext).getToken();
                ApiManager.getInstance().getPostService().deletePost(token, post.getId()).enqueue(new RestCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse res) {
                        mAdapter.getData().remove(post);
                        mAdapter.setNewData(mAdapter.getData());
                        Toast.makeText(mContext, "Xóa bài viết thành công.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RestError error) {
                        Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void doReaction(final Post post, int typeReaction) {
        ApiManager.getInstance().getPostService().doReaction(token, post.getId(), new DoReactionRequest(typeReaction)).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                getAllPost();
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }
}
