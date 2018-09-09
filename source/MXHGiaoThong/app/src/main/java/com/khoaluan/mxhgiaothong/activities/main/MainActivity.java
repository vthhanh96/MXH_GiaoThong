package com.khoaluan.mxhgiaothong.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.khoaluan.mxhgiaothong.customView.PostRecyclerView;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_MENU;
import static com.khoaluan.mxhgiaothong.AppConstants.NAV_DRAWER_MAIN;

public class MainActivity extends DrawerActivity {

    @BindView(R.id.rcvPosts) PostRecyclerView mPostRecyclerView;
    @BindView(R.id.topBar) TopBarView mTopBarView;
    @BindView(R.id.btnCreatePost) FloatingActionButton mCreatePost;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;

    private String mToken;
    private int mPageNumber = 0;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @OnClick(R.id.btnCreatePost)
    public void createPost() {
        AddPostActivity.start(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getNavId() {
        return NAV_DRAWER_MAIN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTopBar();
        initRecyclerView();
        initRefreshLayout();
        getToken();
        getPosts();
    }

    private void initTopBar() {
        mTopBarView.setImageViewLeft(LEFT_MENU);
        mTopBarView.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
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
        mPostRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy<0 && !mCreatePost.isShown())
                    mCreatePost.show();
                else if(dy>0 && mCreatePost.isShown())
                    mCreatePost.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        mPostRecyclerView.setLoadMoreEnable(new PostRecyclerView.OnPostRecyclerViewLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getPosts();
            }
        });
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNumber = 0;
                mPostRecyclerView.setNewData(new ArrayList<Post>());
                getPosts();
            }
        });
    }

    private void getToken() {
        mToken = PreferManager.getInstance(this).getToken();
    }

    private void getPosts() {
        ApiManager.getInstance().getPostService().getAllPost(mToken, mPageNumber).enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(GetAllPostResponse res) {
                mRefreshLayout.setRefreshing(false);
                if(res.getPosts().isEmpty()) {
                    mPostRecyclerView.loadMoreEnd();
                    return;
                }
                mPageNumber++;
                mPostRecyclerView.loadMoreComplete();
                mPostRecyclerView.addData(res.getPosts());
            }

            @Override
            public void failure(RestError error) {
                mRefreshLayout.setRefreshing(false);
                mPostRecyclerView.loadMoreFail();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final QuestionDialog questionDialog = new QuestionDialog("Bạn có chắc chắn muốn thoát ứng dụng?");
        questionDialog.setDialogActionListener(new CustomDialogActionListener() {
            @Override
            public void dialogCancel() {
                questionDialog.dismissDialog();
            }

            @Override
            public void dialogPerformAction() {
                finish();
            }
        });
        questionDialog.show(getSupportFragmentManager(), ListPostActivity.class.getName());
    }


}
