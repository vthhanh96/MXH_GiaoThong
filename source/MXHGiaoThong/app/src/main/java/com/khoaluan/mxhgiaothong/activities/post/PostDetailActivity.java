package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.main.EditPostActivity;
import com.khoaluan.mxhgiaothong.activities.post.dialog.InputDialog;
import com.khoaluan.mxhgiaothong.activities.post.dialog.SelectCommentOptionsDialogFragment;
import com.khoaluan.mxhgiaothong.activities.post.dialog.SelectPostOptionsDialogFragment;
import com.khoaluan.mxhgiaothong.activities.profile.ProfileDetailActivity;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Comment;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.CommentRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.CommentResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Hong Hanh on 5/31/2018.
 */

public class PostDetailActivity extends AppCompatActivity {

    private static final String ARG_KEY_POST_ID = "ARG_KEY_POST_ID";

    public static void start(Context context, Integer postId) {
        Intent starter = new Intent(context, PostDetailActivity.class);
        starter.putExtra(ARG_KEY_POST_ID, postId);
        context.startActivity(starter);
    }

    @BindView(R.id.topBar) TopBarView mTopBar;
    @BindView(R.id.rcvComments) RecyclerView mCommentRecycler;
    @BindView(R.id.imgAvatar) ImageView mImgAvatar;
    @BindView(R.id.tvTime) TextView mTvTime;
    @BindView(R.id.tvTimeCreated) TextView mTvTimeCreated;
    @BindView(R.id.tvName) TextView mTvName;
    @BindView(R.id.rcvCategories) RecyclerView mCategoryRecycler;
    @BindView(R.id.tvAmount) TextView mTvAmount;
    @BindView(R.id.tvPlace) TextView mTvPlace;
    @BindView(R.id.tvContent) TextView mTvContent;
    @BindView(R.id.imgInterested) ImageView mImgInterested;
    @BindView(R.id.txtInterested) TextView mTvInterested;
    @BindView(R.id.txtComment) TextView mTvComment;

    private Post mPost;
    private String mToken;
    private User mUser;
    private Context mContext;
    private CommentAdapter mAdapter;
    private Integer mPostId;
    private CustomProgressDialog mProgressDialog;

    @OnClick(R.id.btnMenuPost)
    public void onMenuButtonClicked() {
        openOptionsPopup(mPost);
    }

    @OnClick(R.id.loInterest)
    public void onInterestedLayoutClicked() {
        interested();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        initTopBar();
        initProgressDialog();
        getUser();
        getExtras();
        initRecyclerView();
    }

    private void initTopBar() {
        mTopBar.setImageViewLeft(AppConstants.LEFT_BACK);
        mTopBar.setImageViewRight(AppConstants.RIGHT_COMMENT);
        mTopBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                onBackPressed();
            }

            @Override
            public void onImvRightClicked() {
                if(mUser == null || mToken == null) {
                    Toast.makeText(mContext, "Bạn phải đăng nhập để thêm bình luận", Toast.LENGTH_SHORT).show();
                    return;
                }
                openInputDialog();
            }

            @Override
            public void onTvLeftClicked() {

            }

            @Override
            public void onTvRightClicked() {
            }
        });
    }

    private void initProgressDialog() {
        mProgressDialog = new CustomProgressDialog(this, "Loading...");
    }

    private void showLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    private void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void getUser() {
        mToken = PreferManager.getInstance(this).getToken();
        mUser = PreferManager.getInstance(this).getUser();
    }

    private void getExtras() {
        mPostId = getIntent().getIntExtra(ARG_KEY_POST_ID, -1);
        getPostInfo();
    }

    private void initRecyclerView() {
        mAdapter = new CommentAdapter();
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getData().get(position) == null) return false;
                showCommentActionPopup(mAdapter.getData().get(position));
                return false;
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.imgAvatar) {
                    ProfileDetailActivity.start(mContext, mAdapter.getData().get(position).getCreator().getId());
                }
            }
        });
        mCommentRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mCommentRecycler.setAdapter(mAdapter);
        mCommentRecycler.setNestedScrollingEnabled(false);
    }

    private void getPostInfo() {
        ApiManager.getInstance().getPostService().getPostInfo(mToken, mPostId).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                if(res.getPost() == null) return;
                mPost = res.getPost();
                onGetPostSuccess();
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(PostDetailActivity.this, error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onGetPostSuccess() {
        if(mPost.getCreator() != null) {
            Glide.with(mContext.getApplicationContext()).load(mPost.getCreator().getAvatar()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
            mTvName.setText(mPost.getCreator().getFullName());
        }
        mTvTimeCreated.setText(DateUtils.getTimeAgo(mContext, mPost.getCreatedDate()));

        if(mPost.getCategories() != null && !mPost.getCategories().isEmpty()) {
            FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
            flowLayoutManager.setAutoMeasureEnabled(true);

            mCategoryRecycler.setVisibility(VISIBLE);
            mCategoryRecycler.setAdapter(new CategoryAdapter(mPost.getCategories()));
            mCategoryRecycler.setLayoutManager(flowLayoutManager);
        } else {
            mCategoryRecycler.setVisibility(GONE);
        }

        mTvAmount.setText(String.valueOf(mPost.getAmount()));
        mTvPlace.setText(mPost.getPlace());
        mTvContent.setText(mPost.getContent());

        mImgInterested.setImageResource(isInterested(mPost.getInterestedPeople()) ? R.drawable.ic_interested_color : R.drawable.ic_interested);

        if(mPost.getInterestedPeople() != null) {
            mTvInterested.setText(mContext.getString(R.string.interested, mPost.getInterestedPeople().size()));
        } else {
            mTvInterested.setText(mContext.getString(R.string.interested, 0));
        }

        if(mPost.getComments() != null) {
            mTvComment.setText(mContext.getString(R.string.comment_amount, mPost.getComments().size()));
        } else {
            mTvComment.setText(mContext.getString(R.string.comment_amount, 0));
        }

        if(mPost.getTime() != null) {
            mTvTime.setText(DateFormat.format("dd/MM/yyyy hh:mm", mPost.getTime()));
        }

        mAdapter.setNewData(mPost.getComments());
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

    private void openOptionsPopup(final Post post) {
        boolean isCreator = !(TextUtils.isEmpty(mToken) || mUser == null || mUser.getId() != post.getCreator().getId());
        SelectPostOptionsDialogFragment dialogFragment = SelectPostOptionsDialogFragment.getNewInstance(isCreator);
        dialogFragment.setPostOptionsListener(new SelectPostOptionsDialogFragment.SelectPostOptionsListener() {
            @Override
            public void editPost() {
                EditPostActivity.start(PostDetailActivity.this, post);
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
        dialogFragment.show(getSupportFragmentManager(), "");
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
                deletePost();
            }
        });
        questionDialog.show(getSupportFragmentManager(), "");
    }

    private void deletePost() {
        showLoading();
        ApiManager.getInstance().getPostService().deletePost(mToken, mPost.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
                finish();
                Toast.makeText(mContext, "Xóa bài viết thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void interested() {
        ApiManager.getInstance().getPostService().interested(mToken, mPost.getId()).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                mImgInterested.setImageResource(isInterested(res.getPost().getInterestedPeople()) ? R.drawable.ic_interested_color : R.drawable.ic_interested);
                if(res.getPost().getInterestedPeople() != null) {
                    mTvInterested.setText(mContext.getString(R.string.interested, res.getPost().getInterestedPeople().size()));
                } else {
                    mTvInterested.setText(mContext.getString(R.string.interested, 0));
                }
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }

    private void openInputDialog() {
        InputDialog inputDialog = new InputDialog(mContext);
        inputDialog.setListener(new InputDialog.InputDialogListener() {
            @Override
            public void onCancelClick() {

            }

            @Override
            public void onDoneClick(String content) {
                createComment(content);
            }
        });
        inputDialog.show();
    }

    private void showCommentActionPopup(final Comment comment) {
        if(mUser != null && mUser.getId() == comment.getCreator().getId()) {
            SelectCommentOptionsDialogFragment dialogFragment = new SelectCommentOptionsDialogFragment();
            dialogFragment.setCommentOptionsListener(new SelectCommentOptionsDialogFragment.SelectCommentOptionsListener() {
                @Override
                public void editComment() {
                    showEditCommentDialog(comment);
                }

                @Override
                public void deleteComment() {
                    onDeleteComment(comment);
                }
            });
            dialogFragment.show(getSupportFragmentManager(), ListCommentsActivity.class.getName());
        }
    }

    private void showEditCommentDialog(final Comment comment) {
        InputDialog inputDialog = new InputDialog(mContext);
        inputDialog.setListener(new InputDialog.InputDialogListener() {
            @Override
            public void onCancelClick() {

            }

            @Override
            public void onDoneClick(String content) {
                editComment(content, comment);
            }
        });
        inputDialog.setContentInput(comment.getContent());
        inputDialog.show();
    }

    private void editComment(String content, final Comment comment) {
        showLoading();
        ApiManager.getInstance().getPostService().editComment(mToken, mPostId, comment.getId(), new CommentRequest(content)).enqueue(new RestCallback<CommentResponse>() {
            @Override
            public void success(CommentResponse res) {
                hideLoading();
                int position = mAdapter.getData().indexOf(comment);
                mAdapter.getData().remove(comment);
                mAdapter.getData().add(position, res.getComment());
                mAdapter.setNewData(mAdapter.getData());
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
            }
        });
    }

    private void onDeleteComment(final Comment comment) {
        showLoading();
        ApiManager.getInstance().getPostService().deleteComment(mToken, mPostId, comment.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
                mAdapter.remove(mAdapter.getData().indexOf(comment));
                mTvComment.setText(getString(R.string.comment_amount, mAdapter.getData().size()));
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
            }
        });
    }

    private void createComment(String content) {
        showLoading();
        ApiManager.getInstance().getPostService().createComment(mToken, mPostId, new CommentRequest(content)).enqueue(new RestCallback<CommentResponse>() {
            @Override
            public void success(CommentResponse res) {
                hideLoading();
                mAdapter.addData(res.getComment());
                mTvComment.setText(getString(R.string.comment_amount, mAdapter.getData().size()));
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show();
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

    private class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

        public CommentAdapter() {
            super(R.layout.item_comment, new ArrayList<Comment>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Comment item) {
            helper.setText(R.id.tvName, item.getCreator().getFullName());
            helper.setText(R.id.tvTime, DateUtils.getTimeAgo(mContext, item.getCreatedDate()));
            helper.setText(R.id.tvContent, item.getContent());

            ImageView imgAvatar = helper.getView(R.id.imgAvatar);
            Glide.with(mContext).load(item.getCreator().getAvatar()).apply(RequestOptions.circleCropTransform()).into(imgAvatar);

            helper.addOnClickListener(R.id.imgAvatar);
        }
    }

}