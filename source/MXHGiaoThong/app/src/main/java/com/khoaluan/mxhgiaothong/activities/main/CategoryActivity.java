package com.khoaluan.mxhgiaothong.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.response.GetAllCategoryResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity{

    private static final String ARG_KEY_SELECTED_CATEGORIES = "ARG_KEY_SELECTED_CATEGORIES";

    @BindView(R.id.topBar) TopBarView mTopBarView;
    @BindView(R.id.rcvCategories) RecyclerView mCategoriesRecyclerView;
    @BindView(R.id.edtSearch) AppCompatEditText mEdtSearch;

    private int mPageNumber = 0;
    private CategoryAdapter mCategoryAdapter;
    private String mQuery = "";
    private List<Category> mSelectedCategories;

    public static void startForResult(Activity activity, List<Category> selectedCategories, int requestCode) {
        Intent starter = new Intent(activity, CategoryActivity.class);
        String data = new Gson().toJson(selectedCategories);
        starter.putExtra(ARG_KEY_SELECTED_CATEGORIES, data);
        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getExtras();
        initTopBar();
        initEditText();
        initRecyclerView();
        getCategories(mQuery);
    }

    private void getExtras() {
        try {
            String data = getIntent().getStringExtra(ARG_KEY_SELECTED_CATEGORIES);
            List<Category> selected = Arrays.asList(new Gson().fromJson(data, Category[].class));
            mSelectedCategories = new ArrayList<>();
            mSelectedCategories.addAll(selected);
        } catch (Exception ex) {
            ex.printStackTrace();
            mSelectedCategories = new ArrayList<>();
        }
    }

    private void initTopBar() {
        mTopBarView.setImageViewLeft(AppConstants.LEFT_BACK);
        mTopBarView.setImageViewRight(AppConstants.RIGHT_MESSAGE);
        mTopBarView.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                finish();
            }

            @Override
            public void onImvRightClicked() {
                Intent data = getIntent();
                data.putExtra(ARG_KEY_SELECTED_CATEGORIES, new Gson().toJson(mSelectedCategories));
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onTvLeftClicked() {

            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    private void initEditText() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mQuery = editable.toString();
                mPageNumber = 0;
                mCategoryAdapter.setEnableLoadMore(true);
//                mCategoryAdapter.setNewData(new ArrayList<Category>());
                getCategories(mQuery);
            }
        });
    }

    private void initRecyclerView() {
        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Category category = (Category) adapter.getItem(position);
                if(category == null) return;

                category.setSelected(!category.isSelected());
                mCategoryAdapter.notifyDataSetChanged();
                updateSelectedList(category);
            }
        });

        mCategoriesRecyclerView.setAdapter(mCategoryAdapter);
        mCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mCategoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getCategories(mQuery);
            }
        }, mCategoriesRecyclerView);
    }

    private void getCategories(String query) {
        ApiManager.getInstance().getCategoryService().getCategories(query, mPageNumber).enqueue(new RestCallback<GetAllCategoryResponse>() {
            @Override
            public void success(GetAllCategoryResponse res) {
                if(res.getCategories() == null || res.getCategories().isEmpty()) {
                    mCategoryAdapter.loadMoreEnd();
                    return;
                }

                mCategoryAdapter.loadMoreComplete();
                mPageNumber++;

                for(Category category : res.getCategories()) {
                    for (Category selected : mSelectedCategories) {
                        if (selected.getId().equals(category.getId())) {
                            category.setSelected(true);
                        }
                    }
                }

                mCategoryAdapter.addData(res.getCategories());
            }

            @Override
            public void failure(RestError error) {
                mCategoryAdapter.loadMoreEnd();
            }
        });
    }

    private void updateSelectedList(Category category) {
        if(category.isSelected()) {
            mSelectedCategories.add(category);
        } else {
            for(Category cate : mSelectedCategories) {
                if(cate.getId().equals(category.getId())) {
                    mSelectedCategories.remove(cate);
                    break;
                }
            }
        }
    }

    private class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

        public CategoryAdapter() {
            super(R.layout.layout_select_category_item, new ArrayList<Category>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Category item) {
            if(item == null) return;
            helper.setText(R.id.tvName, item.getName());
            helper.setVisible(R.id.imgChecked, item.isSelected());
        }
    }
}
