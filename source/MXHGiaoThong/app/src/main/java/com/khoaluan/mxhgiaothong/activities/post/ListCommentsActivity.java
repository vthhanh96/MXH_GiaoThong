package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.dialog.InputDialog;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Comment;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.CommentRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.CommentResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Hanh on 4/17/2018.
 */

public class ListCommentsActivity extends AppCompatActivity{

    private static final String ARG_KEY_POST_ID = "ARG_KEY_POST_ID";

    @BindView(R.id.topBar)
    TopBarView mTopBar;
    @BindView(R.id.rcvComments)
    RecyclerView mCommentsRecyclerView;

    private Context mContext;
    private String mPostId;
    private CommentAdapter mAdapter;
    private CustomProgressDialog mProgressDialog;

    private String mToken;
    private User mUser;

    public static void start(Context context, String postId) {
        Intent intent = new Intent(context, ListCommentsActivity.class);
        intent.putExtra(ARG_KEY_POST_ID, postId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comments);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        getExtras();
        getToken();
        initProgressDialog();
        initTopBar();
        initRecyclerView();
        getPostInfo();
    }

    private void initProgressDialog() {
        mProgressDialog = new CustomProgressDialog(mContext, "Loading...");
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

    private void getExtras() {
        mPostId = getIntent().getStringExtra(ARG_KEY_POST_ID);
    }

    private void getToken() {
        mToken = PreferManager.getInstance(mContext).getToken();
        mUser = PreferManager.getInstance(mContext).getUser();
    }

    private void initTopBar() {
        mTopBar.setTextTitle("Bình luận");
        mTopBar.setImageViewLeft(AppConstants.LEFT_BACK);
        mTopBar.setTextViewRight("Thêm");
        mTopBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                finish();
            }

            @Override
            public void onImvRightClicked() {

            }

            @Override
            public void onTvLeftClicked() {

            }

            @Override
            public void onTvRightClicked() {
                openInputDialog();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CommentAdapter();
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if(mAdapter.getData().get(position) == null) return false;
                showCommentActionPopup(view, mAdapter.getData().get(position));
                return false;
            }
        });
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mCommentsRecyclerView.setAdapter(mAdapter);
    }

    private void getPostInfo() {
        showLoading();
        ApiManager.getInstance().getPostService().getPostInfo(mPostId).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                hideLoading();
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
        if(mUser == null || mUser.getId() != comment.getCreator().getId()) {
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
        if(mUser == null || mUser.getId() != comment.getCreator().getId()) {
            Toast.makeText(mContext, "Bạn không có quyền xóa bình luận này.", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        ApiManager.getInstance().getPostService().deleteComment(mToken, mPostId, comment.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
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
