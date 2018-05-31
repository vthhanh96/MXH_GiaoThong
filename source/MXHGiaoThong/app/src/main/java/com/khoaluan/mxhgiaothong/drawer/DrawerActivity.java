package com.khoaluan.mxhgiaothong.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.login.LoginActivity;
import com.khoaluan.mxhgiaothong.activities.map.view.MapActivity;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.khoaluan.mxhgiaothong.activities.profile.EditProfileActivity;
import com.khoaluan.mxhgiaothong.activities.profile.ProfileDetailActivity;
import com.khoaluan.mxhgiaothong.customView.dialog.QuestionDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.drawer.adapter.MenuAdapter;
import com.khoaluan.mxhgiaothong.drawer.dto.BodyDto;
import com.khoaluan.mxhgiaothong.drawer.dto.FooterDto;
import com.khoaluan.mxhgiaothong.drawer.dto.HeaderDto;
import com.khoaluan.mxhgiaothong.restful.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HieuMinh on 4/7/2018.
 */

abstract public class DrawerActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.rv_menu)
    RecyclerView mRvMenu;

    MenuAdapter mMenuAdapter;
    private User userLogin;
    private List<Object> mMenuList;
    private Handler mHandler;

    abstract protected int getLayoutId();

    abstract protected int getNavId();

    private static final int DRAWER_LAUNCH_DELAY = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
//        MainApplication.getAppComponent().inject(this);
//        mEventManager.register(this);
        userLogin = PreferManager.getInstance(DrawerActivity.this).getUser();
        mMenuList = new ArrayList<>();
        setupListDrawer();
        setupNavDrawer();

    }

    private void setupNavDrawer() {
        mHandler = new Handler();
        if (mDrawerLayout == null) {
            return;
        }

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mRvMenu.scrollToPosition(mMenuAdapter.getCurrentPosition());
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
        });

        mRvMenu.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRvMenu.setLayoutManager(llm);

        mMenuAdapter = new MenuAdapter(this, mMenuList);
        mRvMenu.setAdapter(mMenuAdapter);
        mRvMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mMenuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onHeaderClicked(boolean isSelected) {
                if (!isSelected && userLogin != null) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                    Intent intent = new Intent(DrawerActivity.this, ProfileDetailActivity.class);
                    intent.putExtra("UserID",userLogin.getId());
                    startActivity(intent);
//                    finish();
                }else if(userLogin == null) {
                    Toast.makeText(DrawerActivity.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBodyClick(int position) {
                Object item = mMenuList.get(position);

                if (item instanceof BodyDto) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                    final BodyDto body = (BodyDto) item;
                    if (!body.isSelected) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                goToDrawerItem(body);
                            }
                        }, DRAWER_LAUNCH_DELAY);
                    }
                }
            }

            @Override
            public void onFooterClick() {
                if (userLogin != null) {
                    final QuestionDialog questionDialog = new QuestionDialog("Bạn có chắc chắn muốn đăng xuất?");
                    questionDialog.setDialogActionListener(new CustomDialogActionListener() {
                        @Override
                        public void dialogCancel() {
                            questionDialog.dismissDialog();
                        }

                        @Override
                        public void dialogPerformAction() {
                            PreferManager.getInstance(DrawerActivity.this).saveUser(null);
                            PreferManager.getInstance(DrawerActivity.this).saveToken(null);
                            mDrawerLayout.closeDrawer(Gravity.START);
                            Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    questionDialog.show(getSupportFragmentManager(), ListPostActivity.class.getName());
                } else {
                    Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void goToDrawerItem(BodyDto item) {
        Intent intent;
        switch (item.key) {
            // region -- MENU ---
            case AppConstants.NAV_DRAWER_LIST_POST: {
                intent = new Intent(this, ListPostActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case AppConstants.NAV_DRAWER_NOTI: {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            }
            case AppConstants.NAV_DRAWER_SEARCH: {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            }
            case AppConstants.NAV_DRAWER_MAP: {
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    private void setupListDrawer() {
        mMenuList.clear();
        // Header
        mMenuList.add(
                new HeaderDto(
                        100,
                        getNavId()
                )
        );

        // Body
        mMenuList.add(new BodyDto(
                AppConstants.NAV_DRAWER_LIST_POST,
                R.drawable.ic_home,
                "Trang chủ",
                getNavId())
        );

        mMenuList.add(new BodyDto(
                AppConstants.NAV_DRAWER_MAP,
                R.drawable.ic_map,
                "Chỉ đường",
                getNavId())
        );

        mMenuList.add(new BodyDto(
                AppConstants.NAV_DRAWER_NOTI,
                R.drawable.ic_alarm,
                "Thông báo",
                getNavId())
        );

        mMenuList.add(new BodyDto(
                AppConstants.NAV_DRAWER_SEARCH,
                R.drawable.ic_search,
                "Tìm kiếm",
                getNavId())
        );

        // Footer
        mMenuList.add(new FooterDto(AppConstants.MENU_FOOTER));
        if (mMenuAdapter != null) {
            mMenuAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    protected void closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    protected void openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    protected void lockDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    protected void unLockDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


}
