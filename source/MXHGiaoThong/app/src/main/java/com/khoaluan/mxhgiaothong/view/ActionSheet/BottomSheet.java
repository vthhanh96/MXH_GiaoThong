package com.khoaluan.mxhgiaothong.view.ActionSheet;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

public class BottomSheet<T extends BottomSheet.BottomSheetItem> {
    public interface ActionButtonInterface<T extends BottomSheetItem> {
        void onClicked(T selectedItem);
    }

    public interface HeaderInterface {
        void onBind(View view);
    }

    public interface RecyclerViewInterface<T extends BottomSheetItem> {
        void onBind(BaseViewHolder helper, T item);
    }

    public interface RecyclerViewLoadMoreInterface {
        void onLoadMore();
    }

    public interface RecyclerViewItemClickInterface<T extends BottomSheetItem> {
        void onItemClick(T selectedItem);
    }

    public static class BottomSheetItem {
        private boolean mIsSelected = false;

        public boolean isSelected() {
            return mIsSelected;
        }

        public void setSelected(boolean selected) {
            mIsSelected = selected;
        }
    }

    public static class DefaultBottomSheetItem extends BottomSheetItem {
        private String mTitle;

        public DefaultBottomSheetItem(String title) {
            mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }
    }

    private Context mContext;

    private int mHeaderViewResId = 0;
    private int mItemResId = 0;

    public boolean isAutoHide() {
        return mAutoHide;
    }

    public void setAutoHide(boolean autoHide) {
        mAutoHide = autoHide;
    }

    public void setActionButtonEnable(boolean enable) {
        if(enable) {
            mActionButton.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            mActionButton.setTextColor(mContext.getResources().getColor(R.color.white));
            mActionButton.setEnabled(true);
        } else {
            mActionButton.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            mActionButton.setTextColor(mContext.getResources().getColor(R.color.white));
            mActionButton.setEnabled(false);
        }
    }

    public void setActionButtonVisibility(int visibility) {
        mActionButton.setVisibility(visibility);
    }

    private boolean mAutoHide = true;
    private ActionButtonInterface mActionButtonInterface;
    private RecyclerViewInterface mRecyclerViewInterface;
    private RecyclerViewLoadMoreInterface mRecyclerViewLoadMoreInterface;
    private RecyclerViewItemClickInterface mRecyclerViewItemClickInterface;

    private BottomSheetDialog mBottomSheetDialog;
    private ViewGroup mHeaderView;
    private Adapter mAdapter;
    private int mSelectedIndex = -1;

    @BindView(R.id.vBody) ViewGroup mBodyView;
    @BindView(R.id.rvBody) public RecyclerView mBodyRecyclerView;
    @BindView(R.id.btnAction) FancyButton mActionButton;

    @OnClick(R.id.vTop)
    public void onTopViewClicked() {
        dismiss();
    }

    @OnClick(R.id.btnAction)
    public void setOnActionButtonClicked() {
        if(mAutoHide) {
            dismiss();
        }

        if(mActionButtonInterface == null) {
            return;
        }

        T selectedItem = null;

        if(mSelectedIndex >= 0) {
            selectedItem = mAdapter.getItem(mSelectedIndex);
        }

        mActionButtonInterface.onClicked(selectedItem);
    }

    public BottomSheet(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        initDialog();
    }

    private void initDialog() {
        mBottomSheetDialog = new BottomSheetDialog(mContext);
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_sheet_body, null);
        ButterKnife.bind(this, contentView);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mBottomSheetDialog.addContentView(contentView, layoutParams);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void show() {
        hideKeyboard();
        mBottomSheetDialog.show();
    }

    public void dismiss() {
        mBottomSheetDialog.dismiss();
    }

    public void addHeaderView(int headerViewResId, HeaderInterface headerInterface) {
        mHeaderViewResId = headerViewResId;
        mHeaderView = (ViewGroup) LayoutInflater.from(mContext).inflate(mHeaderViewResId, null);

        if(headerInterface != null) {
            headerInterface.onBind(mHeaderView);
        }

        mBodyView.addView(mHeaderView, 0);
    }

    public void addDefaultHeaderView(final String title) {
        addHeaderView(R.layout.layout_bottom_sheet_default_header, new BottomSheet.HeaderInterface() {
            @Override
            public void onBind(View view) {
                TextView titleTextView = view.findViewById(R.id.txtTitle);
                titleTextView.setText(title);
            }
        });
    }

    public void setActionButtonTitle(String title) {
        mActionButton.setText(title);
    }

    public void setOnActionButtonClicked(ActionButtonInterface<T> actionButtonInterface) {
        mActionButtonInterface = actionButtonInterface;
    }

    public void setOnLoadMoreRequest(RecyclerViewLoadMoreInterface recyclerViewLoadMoreInterface) {
        mRecyclerViewLoadMoreInterface = recyclerViewLoadMoreInterface;

        if(mAdapter == null || mBodyRecyclerView == null || mRecyclerViewLoadMoreInterface == null) return;

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerViewLoadMoreInterface.onLoadMore();
            }
        }, mBodyRecyclerView);
    }

    public void setOnItemRecyclerViewClick(RecyclerViewItemClickInterface<T> recyclerViewItemClickInterface) {
        mRecyclerViewItemClickInterface = recyclerViewItemClickInterface;
    }

    public void initRecyclerView(int itemResId, RecyclerViewInterface<T> recyclerViewInterface) {
        mItemResId = itemResId;
        mRecyclerViewInterface = recyclerViewInterface;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mBodyRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new Adapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectItemAtIndex(position);
                if(mRecyclerViewItemClickInterface != null) {
                    if(mAutoHide) {
                        dismiss();
                    }

                    T selectedItem = null;

                    if(mSelectedIndex >= 0) {
                        selectedItem = mAdapter.getItem(mSelectedIndex);
                    }

                    mRecyclerViewItemClickInterface.onItemClick(selectedItem);
                }
            }
        });

        mBodyRecyclerView.setAdapter(mAdapter);
    }

    public void initDefaultRecyclerView() {
        initRecyclerView(R.layout.layout_bottom_sheet_default_item, new RecyclerViewInterface<T>() {
            @Override
            public void onBind(BaseViewHolder helper, T item) {
                ImageView imageView = helper.getView(R.id.imgChecked);
                imageView.setImageDrawable(item.isSelected() ? ContextCompat.getDrawable(mContext, R.drawable.ic_checked) : ContextCompat.getDrawable(mContext, R.drawable.ic_uncheck));
            }
        });
    }

    public void setEnableLoadMore(boolean isEnable) {
        mAdapter.setEnableLoadMore(isEnable);
    }

    public void setLoadMoreFail() {
        mAdapter.loadMoreFail();
    }

    public void setLoadMoreComplete() {
        mAdapter.loadMoreComplete();
    }

    public void setLoadMoreEnd() {
        mAdapter.loadMoreEnd();
    }

    public void addItem(T item) {
        if(mAdapter == null) {
            return;
        }

        mAdapter.addData(item);
    }

    public void addItems(List<T> items) {
        if(mAdapter == null) {
            return;
        }

        mAdapter.addData(items);
    }

    public void setNewItems(List<T> items) {
        if(mAdapter == null) {
            return;
        }
        mSelectedIndex = -1;
        mAdapter.setNewData(items);
    }

    public List<T> getItems() {
        return mAdapter.getData();
    }

    public void selectItemAtIndex(int index) {
//        if(mSelectedIndex == index) {
//            return;
//        }
//
//        if(mSelectedIndex >= 0 && mAdapter.getItem(mSelectedIndex) != null) {
//            mAdapter.getItem(mSelectedIndex).setSelected(false);
//            mAdapter.notifyItemChanged(mSelectedIndex);
//        }
//        if(mAdapter.getItem(index) != null) {
//            mAdapter.getItem(index).setSelected(true);
//        }
//        mAdapter.notifyItemChanged(index);
//        mSelectedIndex = index;
//        mBodyRecyclerView.scrollToPosition(index);

        if(mAdapter.getItem(index) == null) return;
        mAdapter.getItem(index).setSelected(!mAdapter.getItem(index).isSelected());
        mAdapter.notifyItemChanged(index);
    }

    public List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();
        for(T item : getItems()) {
            if(item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    private class Adapter extends BaseQuickAdapter<T, BaseViewHolder> {
        public Adapter() {
            super(mItemResId, new ArrayList<T>());
        }

        @Override
        protected void convert(BaseViewHolder helper, T item) {
            if(mRecyclerViewInterface == null) {
                return;
            }

            mRecyclerViewInterface.onBind(helper, item);
        }
    }
}