package com.khoaluan.mxhgiaothong.customView.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.khoaluan.mxhgiaothong.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HieuMinh on 4/18/2018.
 */

public abstract class BaseListDialog extends BaseCustomDialogFragment {
    public interface OnItemClickListener {
        void onItemClick(Object item, int position);
    }

    @BindView(R.id.lsvDialogList)
    ListView lsvProfileImage;

    private OnItemClickListener mListener;
    private ArrayAdapter adapter;

    public BaseListDialog() {
        super();
        setView(R.layout.dialog_select_list);
        setHasAction(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = super.onCreateView(inflater, container, savedInstanceState);
        if (mainView == null) {
            return null;
        }
        ButterKnife.bind(this, mainView);

        adapter = getAdapter();
        lsvProfileImage.setAdapter(adapter);
        lsvProfileImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null && isAllowUserInteract) {
                    mListener.onItemClick(adapter.getItem(position), position);
                }
            }
        });

        return mainView;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public abstract ArrayAdapter getAdapter();
}