package com.khoaluan.mxhgiaothong.activities.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.items.LevelItem;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.response.GetAllCategoryResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hong Hanh on 4/12/2018.
 */

public class ChooseCategoryActivity extends AppCompatActivity {

    private static final String ARG_KEY_CATEGORY = "ARG_KEY_CATEGORY";
    private static final String ARG_KEY_LEVEL = "ARG_KEY_LEVEL";
    @BindView(R.id.topBar)
    TopBarView mTopBar;
    @BindView(R.id.rcvCategory)
    RecyclerView mCategoryRecyclerView;
    @BindView(R.id.tvLevel)
    TextView mTvLevel;
    @BindView(R.id.rcvLevel)
    RecyclerView mLevelRecyclerView;

    private CategoryAdapter mCategoryAdapter;
    private LevelAdapter mLevelAdapter;
    private Category mSelectedCategory;
    private int mSelectedLevel = 3;
    private CustomProgressDialog mProgressDialog;

    public static void start(Activity activity, int requestCode, Category category, int level) {
        Intent starter = new Intent(activity, ChooseCategoryActivity.class);
        starter.putExtra(ARG_KEY_CATEGORY, category);
        starter.putExtra(ARG_KEY_LEVEL, level);
        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getExtras();
        initTopBar();
        initProgressDialog();
        initRecyclerView();
        getListCategory();
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
            mProgressDialog.hide();
        }
    }

    private void getExtras() {
        mSelectedCategory = (Category) getIntent().getSerializableExtra(ARG_KEY_CATEGORY);
        mSelectedLevel = getIntent().getIntExtra(ARG_KEY_LEVEL, 0);
    }

    private void initTopBar() {
        mTopBar.setImageViewLeft(R.drawable.ic_back);
        mTopBar.setTextViewRight("Lưu");
        mTopBar.setTextTitle("Chọn loại bài viết");
        mTopBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                setResult(Activity.RESULT_CANCELED);
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
                if(mSelectedCategory == null) {
                    Toast.makeText(ChooseCategoryActivity.this, "Bạn phải chọn một chủ đề cho bài viết.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mSelectedCategory.getId().equals("4") && mSelectedLevel == 0) {
                    Toast.makeText(ChooseCategoryActivity.this, "Bạn phải chọn một mức độ kẹt xe.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent dataIntent = new Intent();
                dataIntent.putExtra(AppConstants.ARG_KEY_CATEGORY_ID, mSelectedCategory);
                dataIntent.putExtra(AppConstants.ARG_KEY_LEVEL, mSelectedLevel);
                setResult(Activity.RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSelectedCategory = (Category) adapter.getData().get(position);
                setCheckCategory();
                if (!mSelectedCategory.getId().equals("4")) {
                    mSelectedLevel = 0;
                    showChooseLevel(false);
                } else {
                    showChooseLevel(true);
                }
            }
        });
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<LevelItem> levelItems = new ArrayList<>();
        levelItems.add(new LevelItem(1, "Nhẹ"));
        levelItems.add(new LevelItem(2, "Trung bình"));
        levelItems.add(new LevelItem(3, "Cao"));

        mLevelAdapter = new LevelAdapter();
        mLevelAdapter.addData(levelItems);
        mLevelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSelectedLevel = ((LevelItem) adapter.getData().get(position)).getLevel();
                setCheckLevel();
            }
        });

        mLevelRecyclerView.setAdapter(mLevelAdapter);
        mLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void getListCategory() {
        showLoading();
        ApiManager.getInstance().getCategoryService().getAllCategory().enqueue(new RestCallback<GetAllCategoryResponse>() {
            @Override
            public void success(GetAllCategoryResponse res) {
                hideLoading();
                mCategoryAdapter.addData(res.getCategories());
                updateData();
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), ChooseCategoryActivity.class.getName());
            }
        });
    }

    private void showChooseLevel(boolean isShow) {
        if (isShow) {
            mLevelRecyclerView.setVisibility(View.VISIBLE);
            mTvLevel.setVisibility(View.VISIBLE);
        } else {
            mLevelRecyclerView.setVisibility(View.GONE);
            mTvLevel.setVisibility(View.GONE);
        }
    }

    private void setCheckCategory() {
        for (int i = 0; i < mCategoryAdapter.getData().size(); i++) {
            if (mCategoryAdapter.getData().get(i).getId().equals(mSelectedCategory.getId())) {
                mCategoryAdapter.getData().get(i).setSelected(true);
            } else {
                mCategoryAdapter.getData().get(i).setSelected(false);
            }
        }
        mCategoryAdapter.setNewData(mCategoryAdapter.getData());
    }

    private void setCheckLevel() {
        for (int i = 0; i < mLevelAdapter.getData().size(); i++) {
            if (mSelectedLevel == mLevelAdapter.getData().get(i).getLevel()) {
                mLevelAdapter.getData().get(i).setSelected(true);
            } else {
                mLevelAdapter.getData().get(i).setSelected(false);
            }
        }
        mLevelAdapter.setNewData(mLevelAdapter.getData());
    }

    private void updateData() {
        if(mSelectedCategory == null) return;
        setCheckCategory();
        if(mSelectedCategory.getId().equals("4")) {
            setCheckLevel();
            showChooseLevel(true);
        }
    }

    public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

        public CategoryAdapter() {
            super(R.layout.item_category, new ArrayList<Category>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Category item) {
            helper.setText(R.id.tvName, item.getName());
            ImageView imgCheck = helper.getView(R.id.imgCheck);
            imgCheck.setSelected(item.isSelected());
        }
    }


    public class LevelAdapter extends BaseQuickAdapter<LevelItem, BaseViewHolder> {

        public LevelAdapter() {
            super(R.layout.item_category, new ArrayList<LevelItem>());
        }

        @Override
        protected void convert(BaseViewHolder helper, LevelItem item) {
            helper.setText(R.id.tvName, item.getLevelName());
            ImageView imgCheck = helper.getView(R.id.imgCheck);
            imgCheck.setSelected(item.isSelected());
        }
    }
}
