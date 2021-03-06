package com.khoaluan.mxhgiaothong.activities.post.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.items.CategoryFilter;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.eventbus.EventUpdateListPost;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.FilterPostRequest;
import com.khoaluan.mxhgiaothong.restful.response.GetAllPostResponse;
import com.khoaluan.mxhgiaothong.view.ActionSheet.BottomSheet;
import com.khoaluan.mxhgiaothong.view.ListPostView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListSelectionPostFragment extends Fragment {

    @BindView(R.id.listPostView)
    ListPostView mListPostView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Context mContext;

    private BottomSheet<CategoryFilter> mCategoryFilterBottomSheet;

    private List<Integer> mCategoryFilterId = new ArrayList<>();
    private List<Integer> mLevelList = new ArrayList<>();
    private List<CategoryFilter> mCategoryFilters;
    private CustomProgressDialog mProgressDialog;


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBalance(EventUpdateListPost eventUpdatePost) {
        filterPost();
    }

    public void chooseCategory() {
        if (mCategoryFilterBottomSheet != null) {
            mCategoryFilterBottomSheet.show();
        }
    }

    private void init() {
        initBottomSheet();
        initProgressDialog();
        initRefresh();
        getAllPost();
        EventBus.getDefault().register(this);
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
            mProgressDialog.dismiss();
        }
    }

    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                getAllPost();
            }
        });
    }

    private void initBottomSheet() {
        mCategoryFilterBottomSheet = new BottomSheet<>(mContext);
        mCategoryFilterBottomSheet.addDefaultHeaderView("Lọc theo thể loại");
        mCategoryFilterBottomSheet.initRecyclerView(R.layout.item_category_filter, new BottomSheet.RecyclerViewInterface<CategoryFilter>() {
            @Override
            public void onBind(BaseViewHolder helper, CategoryFilter item) {
                if (item == null) return;
                helper.setText(R.id.tvCategoryFilter, item.mName);
                helper.setBackgroundRes(R.id.tvCategoryFilter, item.isSelected() ? R.drawable.bg_corner_solid_blue : R.drawable.bg_corner_blue);
                helper.setTextColor(R.id.tvCategoryFilter, item.isSelected() ? getResources().getColor(R.color.white) : getResources().getColor(R.color.black));
            }
        });

        mCategoryFilters = new ArrayList<>();
        mCategoryFilters.add(new CategoryFilter(4, "Kẹt xe nhẹ", 1));
        mCategoryFilters.add(new CategoryFilter(4, "Kẹt xe trung bình", 2));
        mCategoryFilters.add(new CategoryFilter(4, "Kẹt xe cao", 3));
        mCategoryFilters.add(new CategoryFilter(5, "Công trình xây dựng", 0));
        mCategoryFilters.add(new CategoryFilter(6, "Tai nạn giao thông", 0));

        mCategoryFilterBottomSheet.addItems(mCategoryFilters);

        mCategoryFilterBottomSheet.setOnActionButtonClicked(new BottomSheet.ActionButtonInterface<CategoryFilter>() {
            @Override
            public void onClicked(CategoryFilter selectedItem) {
                mCategoryFilters = mCategoryFilterBottomSheet.getSelectedItems();
                getListPostFilter(mCategoryFilterBottomSheet.getSelectedItems());
            }
        });
    }

    private void getListPostFilter(List<CategoryFilter> categoryFilters) {
        showLoading();
        mCategoryFilterId.clear();
        mLevelList.clear();
        for (CategoryFilter item : categoryFilters) {
            mCategoryFilterId.add(item.mId);
            if (!mLevelList.contains(item.mLevel)) {
                mLevelList.add(item.mLevel);
            }
        }
        filterPost();
    }

    private void filterPost() {
        ApiManager.getInstance().getPostService().getPostFilter(new FilterPostRequest(mCategoryFilterId, mLevelList)).enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(final GetAllPostResponse res) {
                hideLoading();
                if (res.getPosts() != null) {
                    mListPostView.setData(res.getPosts());
                    if (res.getPosts().isEmpty()) {
                        Toast.makeText(mContext, "Không tìm thấy bài viết thỏa điều kiện lọc", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
            }
        });
    }

    private void getAllPost() {
        showLoading();
        ApiManager.getInstance().getPostService().getPostFilter(new FilterPostRequest(mCategoryFilterId, mLevelList)).enqueue(new RestCallback<GetAllPostResponse>() {
            @Override
            public void success(final GetAllPostResponse res) {
                hideLoading();
                if (res.getPosts() != null) {
                    mListPostView.setData(res.getPosts());
                }
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
            }
        });
    }
}
