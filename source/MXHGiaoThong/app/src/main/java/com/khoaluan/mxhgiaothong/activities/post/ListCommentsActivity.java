package com.khoaluan.mxhgiaothong.activities.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.RestResponse;
import com.khoaluan.mxhgiaothong.restful.model.Comment;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        initTopBar();
        initRecyclerView();
        getPostInfo();
    }

    private void getExtras() {
        mPostId = getIntent().getStringExtra(ARG_KEY_POST_ID);
    }

    private void initTopBar() {
        mTopBar.setTextTitle("Bình luận");
        mTopBar.setImageViewLeft(AppConstants.LEFT_BACK);
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

            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CommentAdapter();
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mCommentsRecyclerView.setAdapter(mAdapter);
    }

    private void getPostInfo() {
        ApiManager.getInstance().getPostService().getPostInfo(mPostId).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                mAdapter.setNewData(res.getPost().getComments());
            }

            @Override
            public void failure(RestError error) {

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
