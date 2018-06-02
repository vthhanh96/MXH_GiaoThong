package com.khoaluan.mxhgiaothong.customView.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.dialog.adapter.StringListViewAdapter;
import com.khoaluan.mxhgiaothong.utils.FontHelper;

/**
 * Created by hieu-nm on 2018/06/01.
 */

public class SelectModeImageDialogFragment extends BaseCustomDialogFragment{
    private ListView lsvEditPost;
    private int step;
    SelectModeImageListener selectModeImageListener;

    public SelectModeImageDialogFragment() {
        super();
        setView(R.layout.dialog_select_list);
        setHasAction(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = super.onCreateView(inflater, container, savedInstanceState);

        final String[] array = getResources().getStringArray(R.array.dialog_select_iamge);
//        String[] redLines = new String[]{array[(array.length - 1)]};
//        StringListViewAdapter adapter = new StringListViewAdapter(getContext(), array, FontHelper.FONT_QUICKSAND_BOLD, redLines);
        StringListViewAdapter adapter = new StringListViewAdapter(getContext(), array, FontHelper.FONT_QUICKSAND_BOLD);
        lsvEditPost = mainView.findViewById(R.id.lsvDialogList);
        lsvEditPost.setAdapter(adapter);

        lsvEditPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (array[position].equals("Máy ảnh")) {
                    if(selectModeImageListener != null) {
                        selectModeImageListener.callCamera();
                    }
                    step = 1;
                    dismiss();
                }
                if (array[position].equals("Thư viện")){
                    if(selectModeImageListener != null) {
                        selectModeImageListener.callGallery();
                    }
                    step = 1;
                    dismiss();
                }
            }
        });

        return mainView;
    }

    public void onPause(){
        super.onPause();

        if (step == 1){
            dismissDialog();
        }
    }

    public void setPostArticleEditListener(SelectModeImageListener selectModeImageListener) {
        this.selectModeImageListener = selectModeImageListener;
    }

    public interface SelectModeImageListener {
        void callCamera();
        void callGallery();
    }
}