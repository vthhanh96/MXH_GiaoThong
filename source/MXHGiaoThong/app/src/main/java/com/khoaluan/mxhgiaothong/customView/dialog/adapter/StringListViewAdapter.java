package com.khoaluan.mxhgiaothong.customView.dialog.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.utils.FontHelper;


/**
 * Created by duy-tp on 8/31/2017.
 */

public class StringListViewAdapter extends ArrayAdapter<String> {
    private String fontFace;
    private String[] redLineItems;

    public StringListViewAdapter(Context context, String[] items, String fontFace, String[] redLineItems) {
        super (context, 0, items);
        this.fontFace = fontFace;
        this.redLineItems = redLineItems;
    }

    public StringListViewAdapter(Context context, String[] items, String fontFace) {
        super (context, 0, items);
        this.fontFace = fontFace;
        this.redLineItems = new String[]{};
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_text_center, parent, false);
        }

        String item = getItem(position);
        boolean isRedLine = false;

        for (int i = 0; i < redLineItems.length;  i ++) {
            if (redLineItems[i].equals(item)) {
                isRedLine = true;
            }
        }

        TextView tvItem = (TextView) convertView.findViewById(R.id.tvListItem);
        TextView tvItemRed = (TextView) convertView.findViewById(R.id.tvListItemRed);

        if (isRedLine) {
            tvItemRed.setVisibility(View.VISIBLE);
            tvItem.setVisibility(View.GONE);
            tvItemRed.setText(item);
            if (!fontFace.equals("")) {
                Typeface typeface = FontHelper.getInstance().getTypeface(getContext(), this.fontFace);
                if (typeface !=  null) {
                    tvItemRed.setTypeface(typeface, 0);
                }
            }
        } else {
            tvItemRed.setVisibility(View.GONE);
            tvItem.setVisibility(View.VISIBLE);
            tvItem.setText(item);
            if (!fontFace.equals("")) {
                Typeface typeface = FontHelper.getInstance().getTypeface(getContext(), this.fontFace);
                if (typeface !=  null) {
                    tvItem.setTypeface(typeface, 0);
                }
            }
        }


        return convertView;
    }


}