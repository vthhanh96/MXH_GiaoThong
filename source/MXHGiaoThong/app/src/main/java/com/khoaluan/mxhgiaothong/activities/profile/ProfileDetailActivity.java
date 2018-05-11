package com.khoaluan.mxhgiaothong.activities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.CreatePostActivity;
import com.khoaluan.mxhgiaothong.activities.post.ListCommentsActivity;
import com.khoaluan.mxhgiaothong.adapter.PostAdapter;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.DoReactionRequest;
import com.khoaluan.mxhgiaothong.restful.request.ListPostByUserIdResquest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.restful.response.PostResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_MENU;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_SETTING;
import static com.khoaluan.mxhgiaothong.activities.post.ListPostActivity.loginUserID;
import static com.khoaluan.mxhgiaothong.activities.post.ListPostActivity.token;

/**
 * Created by HieuMinh on 4/7/2018.
 */

public class ProfileDetailActivity   extends DrawerActivity {

    @BindView(R.id.topBar)
    TopBarView topBar;
    @BindView(R.id.rcvSelectionPost)
    RecyclerView mSelectionPostRecyclerView;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.imvAvatar)
    CircleImageView imvAvatar;
    @BindView(R.id.tvAddress)
    TextView tvAddress;

    private int userID;
    private User user;
    PostAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_detail;
    }

    @Override
    protected int getNavId() {
        return AppConstants.NAV_DRAWER_PROFILE_DETAIL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        userID = getIntent().getIntExtra("UserID",-1);
        initTopbar();
        init();
    }

    private void initTopbar() {
        topBar.setImageViewLeft(LEFT_MENU);
        if(userID == loginUserID) {
            topBar.setImageViewRight(RIGHT_SETTING);
        }
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
            }

            @Override
            public void onImvRightClicked() {
                Intent intent = new Intent(ProfileDetailActivity.this, EditProfileActivity.class);
                intent.putExtra("userLogin",user);
                startActivity(intent);
            }

            @Override
            public void onTvLeftClicked() {
            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInforUser();
    }

    private void init() {
        initRecyclerView();
    }

    private void getInforUser() {
        ApiManager.getInstance().getUserService().getUserById(token, userID).enqueue(new RestCallback<UserResponse>() {
            @Override
            public void success(UserResponse res) {
                tvUserName.setText(res.getUser().getFullName());
                Glide.with(getApplicationContext()).load(res.getUser().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(imvAvatar);
                tvAddress.setText(res.getUser().getAddress());
                getListPostUser(userID);
                user = res.getUser();
            }

            @Override
            public void failure(RestError error) {
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), ProfileDetailActivity.class.getName());
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new PostAdapter(user);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(mAdapter.getItem(position) == null) return;
                if(view.getId() == R.id.llLike) {
                    doReaction(mAdapter.getItem(position), 1);

                } else if(view.getId() == R.id.llDislike){
                    doReaction(mAdapter.getItem(position), 2);

                } else if(view.getId() == R.id.imgAvatar){
                    Intent intent = new Intent(ProfileDetailActivity.this,ProfileDetailActivity.class);
                    intent.putExtra("UserID",mAdapter.getItem(position).getCreator().getId());
                    startActivity(intent);

                } else if(view.getId() == R.id.imgPostOptions) {
                    openOptionsPopup(mAdapter.getData().get(position), view);

                } else if(view.getId() == R.id.llComments) {
                    ListCommentsActivity.start(ProfileDetailActivity.this, mAdapter.getItem(position).getId());
                }
            }
        });
        mSelectionPostRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mSelectionPostRecyclerView.setAdapter(mAdapter);
    }

    private void openOptionsPopup(final Post post, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_post_action, popupMenu.getMenu());
        if(TextUtils.isEmpty(token) || user == null || user.getId() != post.getCreator().getId()) {
            popupMenu.getMenu().findItem(R.id.edit_post).setEnabled(false);
            popupMenu.getMenu().findItem(R.id.delete_post).setEnabled(false);
        } else {
            popupMenu.getMenu().findItem(R.id.edit_post).setEnabled(true);
            popupMenu.getMenu().findItem(R.id.delete_post).setEnabled(true);
            popupMenu.getMenu().findItem(R.id.delete_post).setEnabled(true);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.edit_post:
                        CreatePostActivity.start(ProfileDetailActivity.this, post);
                        break;
                    case R.id.delete_post:
                        deletePost(post);
                        break;
                    case R.id.hide_post:
                        mAdapter.getData().remove(post);
                        mAdapter.setNewData(mAdapter.getData());
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void deletePost(final Post post) {
        String token = PreferManager.getInstance(this).getToken();
        ApiManager.getInstance().getPostService().deletePost(token, post.getId()).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                mAdapter.getData().remove(post);
                mAdapter.setNewData(mAdapter.getData());
                Toast.makeText(ProfileDetailActivity.this, "Xóa bài viết thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(ProfileDetailActivity.this, error.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doReaction(final Post post, int typeReaction) {
        ApiManager.getInstance().getPostService().doReaction(token, post.getId(), new DoReactionRequest(typeReaction)).enqueue(new RestCallback<PostResponse>() {
            @Override
            public void success(PostResponse res) {
                getListPostUser(userID);
            }

            @Override
            public void failure(RestError error) {

            }
        });
    }

    private void getListPostUser(int id) {
        ApiManager.getInstance().getPostService().getPostByUserId(token, new ListPostByUserIdResquest(id)).enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(GetAllPostResponse res) {
                if(res.getPosts() != null) {
                    mAdapter.addData(res.getPosts());
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void failure(RestError error) {
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), ProfileDetailActivity.class.getName());
            }
        });
    }
}