package com.khoaluan.mxhgiaothong.view.ActionSheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.khoaluan.mxhgiaothong.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropDownBottomSheetLayout<T extends BottomSheet.BottomSheetItem> extends LinearLayout {
    private BottomSheet<T> mBottomSheet;
    private DropDownBottomSheetLayoutBindViewInterface<T> mDropDownBottomSheetLayoutBindViewInterface;
    private BottomSheet.ActionButtonInterface<T> mBottomSheetActionButtonInterface;
    private DropDownBottomSheetLayoutInterface mDropDownBottomSheetLayoutInterface;

    private T mSelectedItem;

    @BindView(R.id.loContentContainer) ViewGroup mContentContainerLayout;
    @BindView(R.id.cardView)
    CardView mCardView;

    public DropDownBottomSheetLayout(Context context) {
        this(context, null, 0);
    }

    public DropDownBottomSheetLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownBottomSheetLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.layout_drop_down_bottom_sheet, this);
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        initBottomSheet();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheet.show();
                if(mDropDownBottomSheetLayoutInterface != null) {
                    mDropDownBottomSheetLayoutInterface.onBottomSheetShow();
                }
            }
        });

        mBottomSheet.setOnActionButtonClicked(new BottomSheet.ActionButtonInterface<T>() {
            @Override
            public void onClicked(T selectedItem) {
                displayItem(selectedItem);
                mSelectedItem = selectedItem;

                if(mBottomSheetActionButtonInterface != null) {
                    mBottomSheetActionButtonInterface.onClicked(selectedItem);
                }
            }
        });
    }

    public DropDownBottomSheetLayoutBindViewInterface<T> getDropDownBottomSheetLayoutBindViewInterface() {
        return mDropDownBottomSheetLayoutBindViewInterface;
    }

    public void setDropDownBottomSheetLayoutBindViewInterface(DropDownBottomSheetLayoutBindViewInterface<T> dropDownBottomSheetLayoutBindViewInterface) {
        mDropDownBottomSheetLayoutBindViewInterface = dropDownBottomSheetLayoutBindViewInterface;
    }

    public void setContentView(int contentViewResId, DropDownBottomSheetLayoutBindViewInterface<T> dropDownBottomSheetLayoutBindViewInterface) {
        mContentContainerLayout.removeAllViews();
        inflate(getContext(), contentViewResId, mContentContainerLayout);
        setDropDownBottomSheetLayoutBindViewInterface(dropDownBottomSheetLayoutBindViewInterface);
    }

    public void addBottomSheetHeaderView(int headerViewResId, BottomSheet.HeaderInterface headerInterface) {
        mBottomSheet.addHeaderView(headerViewResId, headerInterface);
    }

    public void addBottomSheetDefaultHeaderView(String title) {
        mBottomSheet.addDefaultHeaderView(title);
    }

    public void initBottomSheetRecyclerView(int itemResId, BottomSheet.RecyclerViewInterface<T> recyclerViewInterface) {
        mBottomSheet.initRecyclerView(itemResId, recyclerViewInterface);
    }

    public void addBottomSheetItem(T item) {
        mBottomSheet.addItem(item);
    }

    public void addBottomSheetItems(List<T> items) {
        mBottomSheet.addItems(items);
    }

    public void setNewBottomSheetItems(List<T> items) {
        mBottomSheet.setNewItems(items);
    }

    public void setNewBottomSheetItems(List<T> items, int selectedIndex) {
        setNewBottomSheetItems(items);
        mBottomSheet.selectItemAtIndex(selectedIndex);
    }

    public void selectBottomSheetItemAt(int selectedIndex) {
        mBottomSheet.selectItemAtIndex(selectedIndex);
        mSelectedItem = mBottomSheet.getItems().get(selectedIndex);
    }

    public void clearBottomSheetItems() {
        mBottomSheet.setNewItems(new ArrayList<T>());
        mSelectedItem = null;
    }

    public void setBottomSheetActionButtonTitle(String title) {
        mBottomSheet.setActionButtonTitle(title);
    }

    public void setOnBottomSheetActionButtonClicked(@NonNull BottomSheet.ActionButtonInterface<T> actionButtonInterface) {
        mBottomSheetActionButtonInterface = actionButtonInterface;
    }

    public void setOnRecyclerViewItemClick() {
        mBottomSheet.setOnItemRecyclerViewClick(new BottomSheet.RecyclerViewItemClickInterface<T>() {
            @Override
            public void onItemClick(T selectedItem) {
                displayItem(selectedItem);
                mSelectedItem = selectedItem;
            }
        });
    }

    public void setActionButtonVisibility(int visibility) {
        mBottomSheet.setActionButtonVisibility(visibility);
    }

    public void setEnableLoadMore(boolean isEnable) {
        mBottomSheet.setEnableLoadMore(isEnable);
    }

    public void setLoadMoreFail() {
        mBottomSheet.setLoadMoreFail();
    }

    public void setLoadMoreComplete() {
        mBottomSheet.setLoadMoreComplete();
    }

    public void setLoadMoreEnd() {
        mBottomSheet.setLoadMoreEnd();
    }

    public void setOnRequestLoadMore(@NonNull BottomSheet.RecyclerViewLoadMoreInterface recyclerViewLoadMoreInterface) {
        mBottomSheet.setOnLoadMoreRequest(recyclerViewLoadMoreInterface);
    }

    public void displayItem(T item) {
        if(mDropDownBottomSheetLayoutBindViewInterface != null) {
            mDropDownBottomSheetLayoutBindViewInterface.onBindContentView(mContentContainerLayout.getChildAt(0), item);
        }
    }

    private void initBottomSheet() {
        mBottomSheet = new BottomSheet<>(getContext());
    }

    public void setDropDownBottomSheetLayoutInterface(DropDownBottomSheetLayoutInterface dropDownBottomSheetLayoutInterface) {
        mDropDownBottomSheetLayoutInterface = dropDownBottomSheetLayoutInterface;
    }

    public T getSelectedItem() {
        return mSelectedItem;
    }

    public void disableCardView() {
        mCardView.setCardElevation(0);
        mCardView.setCardBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        mCardView.setCardBackgroundColor(enable ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.gray));
    }
}