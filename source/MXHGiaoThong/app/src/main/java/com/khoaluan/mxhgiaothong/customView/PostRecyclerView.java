package com.khoaluan.mxhgiaothong.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.main.EditPostActivity;
import com.khoaluan.mxhgiaothong.activities.post.CreatePostActivity;
import com.khoaluan.mxhgiaothong.activities.post.ListCommentsActivity;
import com.khoaluan.mxhgiaothong.activities.post.PostDetailActivity;
import com.khoaluan.mxhgiaothong.activities.post.dialog.SelectPostOptionsDialogFragment;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class PostRecyclerView extends RecyclerView {

    private PostAdapter mPostAdapter;
    private String mToken;
    private User mUser;

    public interface OnPostRecyclerViewLoadMoreListener {
        void onLoadMore();
    }

    public PostRecyclerView(Context context) {
        this(context, null, 0);
    }

    public PostRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PostRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getData();
        initRecyclerView();
    }

    private void getData() {
        mToken = PreferManager.getInstance(getContext()).getToken();
        mUser = PreferManager.getInstance(getContext()).getUser();
    }

    private void initRecyclerView() {
        mPostAdapter = new PostAdapter();
        mPostAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Post post = (Post) adapter.getItem(position);
                if(post == null) return;
                PostDetailActivity.start(getContext(), post.getId());
            }
        });

        mPostAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Post post = (Post) adapter.getItem(position);
                if(post == null) return;

                switch (view.getId()) {
                    case R.id.loInterest:
                        interested(post, position);
                        break;
                    case R.id.loComment:
                        ListCommentsActivity.start(getContext(), post.getId());
                        break;
                    case R.id.btnMenuPost:
                        openOptionsPopup(post, view);
                        break;
                    case R.id.imgAvatar:
                        break;
                }
            }
        });

        this.setAdapter(mPostAdapter);
        this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void interested(Post post, final int position) {
        ApiManager.getInstance().getPostService().interested(mToken, post.getId()).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                mPostAdapter.getData().get(position).setInterestedPeople(res.getPost().getInterestedPeople());
                mPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }

    public void setLoadMoreEnable(final OnPostRecyclerViewLoadMoreListener listener) {
        mPostAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(listener != null) {
                    listener.onLoadMore();
                }
            }
        }, this);
    }

    public void setNewData(List<Post> posts) {
        mPostAdapter.setNewData(posts);
    }

    public void addData(List<Post> posts) {
        mPostAdapter.addData(posts);
    }

    public void loadMoreFail() {
        mPostAdapter.loadMoreFail();
    }

    public void loadMoreComplete() {
        mPostAdapter.loadMoreComplete();
    }

    public void loadMoreEnd() {
        mPostAdapter.loadMoreEnd();
    }

    private class PostAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {

        public PostAdapter() {
            super(R.layout.layout_post_item, new ArrayList<Post>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Post item) {
            if(item == null) return;

            if(item.getCreator() != null) {
                ImageView imgAvatar = helper.getView(R.id.imgAvatar);
                Glide.with(mContext.getApplicationContext()).load(item.getCreator().getAvatar()).apply(RequestOptions.circleCropTransform()).into(imgAvatar);
                helper.setText(R.id.tvName, item.getCreator().getFullName());
            }
            helper.setText(R.id.tvTimeCreated, DateUtils.getTimeAgo(mContext, item.getCreatedDate()));

            RecyclerView rcvCategories = helper.getView(R.id.rcvCategories);
            if(item.getCategories() != null && !item.getCategories().isEmpty()) {
                FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                flowLayoutManager.setAutoMeasureEnabled(true);

                rcvCategories.setVisibility(VISIBLE);
                rcvCategories.setAdapter(new CategoryAdapter(item.getCategories()));
                rcvCategories.setLayoutManager(flowLayoutManager);
            } else {
                rcvCategories.setVisibility(GONE);
            }

            helper.setText(R.id.tvAmount, String.valueOf(item.getAmount()));
            helper.setText(R.id.tvPlace, item.getPlace());

            if(item.getTime() != null) {
                helper.setText(R.id.tvTime, DateFormat.format("dd/MM/yyyy hh:mm", item.getTime()));
            }

            helper.setText(R.id.tvContent, item.getContent());

            helper.setImageResource(R.id.imgInterested, isInterested(item.getInterestedPeople()) ? R.drawable.ic_interested_color : R.drawable.ic_interested);

            if(item.getInterestedPeople() != null) {
                helper.setText(R.id.txtInterested, mContext.getString(R.string.interested, item.getInterestedPeople().size()));
            } else {
                helper.setText(R.id.txtInterested, mContext.getString(R.string.interested, 0));
            }

            if(item.getComments() != null) {
                helper.setText(R.id.txtComment, mContext.getString(R.string.comment_amount, item.getComments().size()));
            } else {
                helper.setText(R.id.txtComment, mContext.getString(R.string.comment_amount, 0));
            }

            helper.addOnClickListener(R.id.loInterest);
            helper.addOnClickListener(R.id.loComment);
            helper.addOnClickListener(R.id.btnMenuPost);
            helper.addOnClickListener(R.id.imgAvatar);
        }
    }

    private boolean isInterested(List<User> users) {
        if(mUser == null || users == null || users.isEmpty()) return false;
        for (User user : users) {
            if(user.getId() == mUser.getId()) {
                return true;
            }
        }
        return false;
    }

    private void openOptionsPopup(final Post post, View view) {
        boolean isCreator = !(TextUtils.isEmpty(mToken) || mUser == null || mUser.getId() != post.getCreator().getId());
        SelectPostOptionsDialogFragment dialogFragment = SelectPostOptionsDialogFragment.getNewInstance(isCreator);
        dialogFragment.setPostOptionsListener(new SelectPostOptionsDialogFragment.SelectPostOptionsListener() {
            @Override
            public void editPost() {
                EditPostActivity.start(getContext(), post);
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
        dialogFragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "");
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
        questionDialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "");
    }

    private void deletePost(final Post post) {
//        showLoading();
        ApiManager.getInstance().getPostService().deletePost(mToken, post.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
//                hideLoading();
                mPostAdapter.getData().remove(post);
                mPostAdapter.setNewData(mPostAdapter.getData());
                Toast.makeText(getContext(), "Xóa bài viết thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RestError error) {
//                hideLoading();
                Toast.makeText(getContext(), error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

        public CategoryAdapter(List<Category> categories) {
            super(R.layout.layout_category_item, categories);
        }

        @Override
        protected void convert(BaseViewHolder helper, Category item) {
            helper.setText(R.id.txtCategoryName, item.getName());
        }
    }
}
