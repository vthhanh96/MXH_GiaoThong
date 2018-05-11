package com.khoaluan.mxhgiaothong.activities.map.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;

import java.util.List;

public class ListPlaceAdapter extends ArrayAdapter<String> {
    public ListPlaceAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ListPlaceAdapter(Context context, int resource, List<String> place) {
        super(context, resource, place);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.place_search_item, null);
        }

        String place = getItem(position);

        if (place != null) {
            TextView txtDestination = (TextView) v.findViewById(R.id.txtDestination);
            txtDestination.setText(place);
        }

        return v;
    }
}
