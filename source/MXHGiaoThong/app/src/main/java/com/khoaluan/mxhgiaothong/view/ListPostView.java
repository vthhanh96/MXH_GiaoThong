package com.khoaluan.mxhgiaothong.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.CreatePostActivity;
import com.khoaluan.mxhgiaothong.activities.post.ListCommentsActivity;
import com.khoaluan.mxhgiaothong.activities.post.PostDetailActivity;
import com.khoaluan.mxhgiaothong.activities.post.dialog.SelectPostOptionsDialogFragment;
import com.khoaluan.mxhgiaothong.activities.profile.ProfileDetailActivity;
import com.khoaluan.mxhgiaothong.adapter.PostAdapter;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.Reaction;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.DoReactionRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Hanh on 5/27/2018.
 */

public class ListPostView extends FrameLayout{

    @BindView(R.id.rcvPosts) RecyclerView mPostsRecycler;

    private Context mContext;
    private PostAdapter mAdapter;
    private User mUser;
    private String token;
    private CustomProgressDialog mProgressDialog;

    public ListPostView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ListPostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPostView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initView();
        getUser();
        initProgressDialog();
        initRecyclerView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_list_post, this);
        ButterKnife.bind(this, view);
    }

    private void getUser() {
        mUser = PreferManager.getInstance(getContext()).getUser();
        token = PreferManager.getInstance(getContext()).getToken();
    }

    private void initProgressDialog() {
        mProgressDialog = new CustomProgressDialog(getContext(), "Loading...");
    }

    private void showLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    private void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    private void initRecyclerView() {
        mAdapter = new PostAdapter(mUser);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItem(position) == null) return;
                if (view.getId() == R.id.llLike) {
                    doReaction(position, 1);

                } else if (view.getId() == R.id.llDislike) {
                    doReaction(position, 2);

                } else if (view.getId() == R.id.imgAvatar) {
                    ProfileDetailActivity.start(getContext(), mAdapter.getItem(position).getCreator().getId());

                } else if (view.getId() == R.id.imgPostOptions) {
                    openOptionsPopup(mAdapter.getData().get(position), view);

                } else if (view.getId() == R.id.llComments) {
                    ListCommentsActivity.start(getContext(), mAdapter.getItem(position).getId());
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostDetailActivity.start(mContext, mAdapter.getItem(position).getId());
            }
        });
        mPostsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mPostsRecycler.setAdapter(mAdapter);
    }

    private void openOptionsPopup(final Post post, View view) {
        boolean isCreator = !(TextUtils.isEmpty(token) || mUser == null || mUser.getId() != post.getCreator().getId());
        SelectPostOptionsDialogFragment dialogFragment = SelectPostOptionsDialogFragment.getNewInstance(isCreator);
        dialogFragment.setPostOptionsListener(new SelectPostOptionsDialogFragment.SelectPostOptionsListener() {
            @Override
            public void editPost() {
                CreatePostActivity.start(getContext(), post);
            }

            @Override
            public void deletePost() {
                showDialogConfirmDeletePost(post);
            }

            @Override
            public void judgePost() {

            }

            @Override
            public void reportPost() {

            }
        });
        dialogFragment.show(((FragmentActivity)mContext).getSupportFragmentManager(), "");
    }

    private Reaction getReaction(List<Reaction> reactions) {
        if(reactions == null || mUser == null) return null;
        for(Reaction reaction : reactions) {
            if(reaction.getCreator().getId() == mUser.getId()) {
                return reaction;
            }
        }
        return null;
    }

    private void doReaction(int position, int typeReaction) {
        if(mUser == null || token == null) {
            Toast.makeText(mContext, "Bạn phải đăng nhập trước khi thực hiện hành động này", Toast.LENGTH_SHORT).show();
            return;
        }

        Post post = mAdapter.getItem(position);
        Reaction reaction = getReaction(post.getReaction());
        if(reaction == null) {
            reaction = new Reaction();
            reaction.setCreator(mUser);
            reaction.setStatus_reaction(typeReaction);
            post.getReaction().add(reaction);
            if(typeReaction == 1) {
                post.setLikeAmount(post.getLikeAmount() + 1);
            } else {
                post.setDislikeAmount(post.getDislikeAmount() + 1);
            }
        } else {
            if(reaction.getStatus_reaction() == typeReaction) {
                post.getReaction().remove(reaction);
                if(typeReaction == 1) {
                    post.setLikeAmount(post.getLikeAmount() - 1);
                } else {
                    post.setDislikeAmount(post.getDislikeAmount() - 1);
                }
            } else {
                int reactionPosition = post.getReaction().indexOf(reaction);
                reaction.setStatus_reaction(typeReaction);
                if(typeReaction == 1) {
                    post.setLikeAmount(post.getLikeAmount() + 1);
                    post.setDislikeAmount(post.getDislikeAmount() - 1);
                } else {
                    post.setLikeAmount(post.getLikeAmount() - 1);
                    post.setDislikeAmount(post.getDislikeAmount() + 1);
                }
                post.getReaction().set(reactionPosition, reaction);
            }

        }
        mAdapter.getData().set(position, post);
        mAdapter.notifyItemChanged(position);

        ApiManager.getInstance().getPostService().doReaction(token, post.getId(), new DoReactionRequest(typeReaction)).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }

    private void showDialogConfirmDeletePost(final Post post) {
        final QuestionDialog questionDialog = new QuestionDialog("Bạn có chắc chắn muốn xóa bài viết này?");
        questionDialog.setDialogActionListener(new CustomDialogActionListener() {
            @Override
            public void dialogCancel() {
                questionDialog.dismissDialog();
            }

            @Override
            public void dialogPerformAction() {
                questionDialog.dismissDialog();
                deletePost(post);
            }
        });
        questionDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(), "");
    }

    private void deletePost(final Post post) {
        showLoading();
        ApiManager.getInstance().getPostService().deletePost(token, post.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
                mAdapter.getData().remove(post);
                mAdapter.setNewData(mAdapter.getData());
                Toast.makeText(getContext(), "Xóa bài viết thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(List<Post> posts) {
        mAdapter.setNewData(posts);
    }
}
