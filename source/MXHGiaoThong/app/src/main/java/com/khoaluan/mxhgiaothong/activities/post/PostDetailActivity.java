package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.dialog.InputDialog;
import com.khoaluan.mxhgiaothong.activities.profile.ProfileDetailActivity;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.eventbus.EventUpdateListPost;
import com.khoaluan.mxhgiaothong.eventbus.EventUpdatePost;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Comment;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.Reaction;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.CommentRequest;
import com.khoaluan.mxhgiaothong.restful.request.DoReactionRequest;
import com.khoaluan.mxhgiaothong.restful.request.UpdatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.CommentResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Hanh on 5/31/2018.
 */

public class PostDetailActivity extends AppCompatActivity {

    private static final String ARG_KEY_POST_ID = "ARG_KEY_POST_ID";

    public static void start(Context context, String postId) {
        Intent starter = new Intent(context, PostDetailActivity.class);
        starter.putExtra(ARG_KEY_POST_ID, postId);
        context.startActivity(starter);
    }

    @BindView(R.id.topBar)
    TopBarView mTopBar;
    @BindView(R.id.imgAvatar)
    ImageView mImgAvatar;
    @BindView(R.id.tvName)
    TextView mNameTextView;
    @BindView(R.id.tvPlace)
    TextView mPlaceTextView;
    @BindView(R.id.tvTime)
    TextView mTimeTextView;
    @BindView(R.id.tvCategory)
    TextView mCategoryTextView;
    @BindView(R.id.tvContent)
    TextView mContentTextView;
    @BindView(R.id.imgContent)
    ImageView mImgContent;
    @BindView(R.id.imgLike)
    ImageView mImgLike;
    @BindView(R.id.tvLikeAmount)
    TextView mLikeAmountTextView;
    @BindView(R.id.imgDislike)
    ImageView mImgDislike;
    @BindView(R.id.tvDislikeAmount)
    TextView mDislikeAmount;
    @BindView(R.id.txtCommentAmount)
    TextView mCommentTextView;
    @BindView(R.id.rcvComments)
    RecyclerView mCommentRecycler;
    @BindView(R.id.imgPostOptions)
    ImageView mImgPostOptions;

    private Post mPost;
    private String mToken;
    private User mUser;
    private Context mContext;
    private CommentAdapter mAdapter;
    private String mPostId;
    private CustomProgressDialog mProgressDialog;

    @OnClick(R.id.llLike)
    public void like() {
        doReaction(1);
    }

    @OnClick(R.id.llDislike)
    public void dislike() {
        doReaction(2);
    }

    @OnClick(R.id.imgAvatar)
    public void openProfile() {
        Intent intent = new Intent(this, ProfileDetailActivity.class);
        intent.putExtra("UserID", mPost.getCreator().getId());
        startActivity(intent);
    }

    @OnClick(R.id.imgPostOptions)
    public void openOptions() {
        openOptionsPopup(mPost, mImgPostOptions);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBalance(EventUpdatePost eventUpdatePost) {
        getPostInfo();
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initTopBar() {
        mTopBar.setImageViewLeft(AppConstants.LEFT_BACK);
        mTopBar.setTextViewRight("Thêm bình luận");
        mTopBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                onBackPressed();
            }

            @Override
            public void onImvRightClicked() {

            }

            @Override
            public void onTvLeftClicked() {

            }

            @Override
            public void onTvRightClicked() {
                if(mUser == null || mToken == null) {
                    Toast.makeText(mContext, "Bạn phải đăng nhập để thêm bình luận", Toast.LENGTH_SHORT).show();
                    return;
                }
                openInputDialog();
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
        mPostId = getIntent().getStringExtra(ARG_KEY_POST_ID);
        getPostInfo();
    }

    private void initData() {
        if (mPost == null) return;
        mNameTextView.setText(mPost.getCreator().getFullName());
        mPlaceTextView.setText(mPost.getPlace());
        mTimeTextView.setText(DateUtils.getTimeAgo(this, mPost.getCreatedDate()));
        mContentTextView.setText(mPost.getContent());
        mLikeAmountTextView.setText(String.valueOf(mPost.getLikeAmount()));
        mDislikeAmount.setText(String.valueOf(mPost.getDislikeAmount()));
        mCommentTextView.setText(getString(R.string.comment_amount, mPost.getComments().size()));

        int typeReaction = isReaction(mPost.getReaction());

        if (typeReaction == 1) {
            mImgLike.setImageResource(R.drawable.ic_like_color);
            mImgDislike.setImageResource(R.drawable.ic_dislike);
        } else if (typeReaction == 2) {
            mImgDislike.setImageResource(R.drawable.ic_dislike_color);
            mImgLike.setImageResource(R.drawable.ic_like);
        } else {
            mImgDislike.setImageResource(R.drawable.ic_dislike);
            mImgLike.setImageResource(R.drawable.ic_like);
        }

        Glide.with(this.getApplicationContext()).load(mPost.getCreator().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
        Glide.with(this.getApplicationContext()).load(mPost.getImageUrl()).into(mImgContent);

        mCategoryTextView.setText(mPost.getCategory().getName());

        switch (mPost.getLevel()) {
            case 0:
                mCategoryTextView.setBackgroundResource(R.drawable.bg_corner_solid_green);
                break;
            case 1:
                mCategoryTextView.setBackgroundResource(R.drawable.bg_corner_solid_yellow);
                break;
            case 2:
                mCategoryTextView.setBackgroundResource(R.drawable.bg_corner_solid_orange);
                break;
            case 3:
                mCategoryTextView.setBackgroundResource(R.drawable.bg_corner_solid_red);
                break;


        }
    }

    private int isReaction(List<Reaction> reactions) {
        if (reactions == null || mUser == null) return 0;
        for (Reaction reaction : reactions) {
            if (reaction.getCreator().getId() == mUser.getId()) {
                return reaction.getStatus_reaction();
            }
        }
        return 0;
    }

    public void openOptionsPopup(final Post post, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_post_action, popupMenu.getMenu());
        if (TextUtils.isEmpty(mToken) || mUser == null || mUser.getId() != post.getCreator().getId()) {
            popupMenu.getMenu().findItem(R.id.edit_post).setEnabled(false);
            popupMenu.getMenu().findItem(R.id.delete_post).setEnabled(false);
        } else {
            popupMenu.getMenu().findItem(R.id.edit_post).setEnabled(true);
            popupMenu.getMenu().findItem(R.id.delete_post).setEnabled(true);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit_post:
                        CreatePostActivity.start(PostDetailActivity.this, post);
                        break;
                    case R.id.delete_post:
                        showDialogConfirmDeletePost(post);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private Reaction getReaction(List<Reaction> reactions) {
        if (reactions == null || mUser == null) return null;
        for (Reaction reaction : reactions) {
            if (reaction.getCreator().getId() == mUser.getId()) {
                return reaction;
            }
        }
        return null;
    }

    private void doReaction(int typeReaction) {
        if (mUser == null || mToken == null) {
            Toast.makeText(this, "Bạn phải đăng nhập trước khi thực hiện hành động này", Toast.LENGTH_SHORT).show();
            return;
        }

        Reaction reaction = getReaction(mPost.getReaction());
        if (reaction == null) {
            reaction = new Reaction();
            reaction.setCreator(mUser);
            reaction.setStatus_reaction(typeReaction);
            mPost.getReaction().add(reaction);
            if (typeReaction == 1) {
                mPost.setLikeAmount(mPost.getLikeAmount() + 1);
            } else {
                mPost.setDislikeAmount(mPost.getDislikeAmount() + 1);
            }
        } else {
            if (reaction.getStatus_reaction() == typeReaction) {
                mPost.getReaction().remove(reaction);
                if (typeReaction == 1) {
                    mPost.setLikeAmount(mPost.getLikeAmount() - 1);
                } else {
                    mPost.setDislikeAmount(mPost.getDislikeAmount() - 1);
                }
            } else {
                int reactionPosition = mPost.getReaction().indexOf(reaction);
                reaction.setStatus_reaction(typeReaction);
                if (typeReaction == 1) {
                    mPost.setLikeAmount(mPost.getLikeAmount() + 1);
                    mPost.setDislikeAmount(mPost.getDislikeAmount() - 1);
                } else {
                    mPost.setLikeAmount(mPost.getLikeAmount() - 1);
                    mPost.setDislikeAmount(mPost.getDislikeAmount() + 1);
                }
                mPost.getReaction().set(reactionPosition, reaction);
            }
        }
        initData();

        ApiManager.getInstance().getPostService().doReaction(mToken, mPost.getId(), new DoReactionRequest(typeReaction)).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                EventBus.getDefault().post(new EventUpdateListPost());
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
        questionDialog.show(getSupportFragmentManager(), "");
    }

    private void deletePost(final Post post) {
        showLoading();
        ApiManager.getInstance().getPostService().deletePost(mToken, post.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
                finish();
                EventBus.getDefault().post(new EventUpdateListPost());
                Toast.makeText(PostDetailActivity.this, "Xóa bài viết thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                Toast.makeText(PostDetailActivity.this, error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CommentAdapter();
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getData().get(position) == null) return false;
                showCommentActionPopup(view, mAdapter.getData().get(position));
                return false;
            }
        });
        mCommentRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mCommentRecycler.setAdapter(mAdapter);
        mCommentRecycler.setNestedScrollingEnabled(false);
        mCommentRecycler.setHasFixedSize(true);
    }

    private void getPostInfo() {
        showLoading();
        ApiManager.getInstance().getPostService().getPostInfo(mPostId).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                hideLoading();
                mPost = res.getPost();
                initData();
                mAdapter.setNewData(res.getPost().getComments());
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
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

    private void showCommentActionPopup(View view, final Comment comment) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_comment_action, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit_comment:
                        showEditCommentDialog(comment);
                        break;
                    case R.id.delete_comment:
                        deleteComment(comment);
                        break;
                    case R.id.cancel:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void showEditCommentDialog(final Comment comment) {
        if (mUser == null || mUser.getId() != comment.getCreator().getId()) {
            Toast.makeText(mContext, "Bạn không có quyền chỉnh sửa bình luận này.", Toast.LENGTH_SHORT).show();
            return;
        }
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

    private void deleteComment(final Comment comment) {
        if (mUser == null || mUser.getId() != comment.getCreator().getId()) {
            Toast.makeText(mContext, "Bạn không có quyền xóa bình luận này.", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        ApiManager.getInstance().getPostService().deleteComment(mToken, mPostId, comment.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
                EventBus.getDefault().post(new EventUpdateListPost());
                mAdapter.getData().remove(comment);
                mAdapter.setNewData(mAdapter.getData());
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
                EventBus.getDefault().post(new EventUpdateListPost());
                mAdapter.getData().add(res.getComment());
                mAdapter.setNewData(mAdapter.getData());
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

        public CommentAdapter() {
            super(R.layout.item_comment, new ArrayList<Comment>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Comment item) {
            helper.setText(R.id.tvName, item.getCreator().getFullName());
            helper.setText(R.id.tvTime, DateUtils.getTimeAgo(mContext, item.getCreatedDate()));
            helper.setText(R.id.tvContent, item.getContent());

            ImageView imgAvatar = helper.getView(R.id.imgAvatar);
            Glide.with(mContext).load(item.getCreator().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(imgAvatar);
        }
    }

}
