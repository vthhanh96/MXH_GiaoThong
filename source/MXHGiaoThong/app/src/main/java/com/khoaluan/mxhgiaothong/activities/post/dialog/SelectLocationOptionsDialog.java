package com.khoaluan.mxhgiaothong.activities.post.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.dialog.BaseCustomDialogFragment;
import com.khoaluan.mxhgiaothong.customView.dialog.adapter.StringListViewAdapter;
import com.khoaluan.mxhgiaothong.utils.FontHelper;

/**
 * Created by Hong Hanh on 6/3/2018.
 */

public class SelectLocationOptionsDialog extends BaseCustomDialogFragment {

    private ListView lsvEditPost;
    private int step;
    private SelectLocationOptionsListener mListener;

    public SelectLocationOptionsDialog() {
        super();
        setView(R.layout.dialog_select_list);
        setHasAction(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = super.onCreateView(inflater, container, savedInstanceState);

        final String[] array = getResources().getStringArray(R.array.dialog_select_location_options);
        StringListViewAdapter adapter;
        adapter = new StringListViewAdapter(getContext(), array, FontHelper.FONT_QUICKSAND_BOLD);

        lsvEditPost = mainView.findViewById(R.id.lsvDialogList);
        lsvEditPost.setAdapter(adapter);

        lsvEditPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (array[position].equals("Vị trí hiện tại")) {
                    if (mListener != null) {
                        mListener.setCurrentLocation();
                    }
                    step = 1;
                    dismiss();
                }
                if (array[position].equals("Tìm kiếm vị trí khác")) {
                    if (mListener != null) {
                        mListener.openSearchLocation();
                    }
                    step = 1;
                    dismiss();
                }
            }
        });

        return mainView;
    }

    public void onPause() {
        super.onPause();

        if (step == 1) {
            dismissDialog();
        }
    }

    public void setLocationOptionsListener(SelectLocationOptionsListener listener) {
        mListener = listener;
    }

    public interface SelectLocationOptionsListener {
        void setCurrentLocation();

        void openSearchLocation();
    }

}

