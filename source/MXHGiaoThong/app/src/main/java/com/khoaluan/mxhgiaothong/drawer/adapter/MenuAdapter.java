package com.khoaluan.mxhgiaothong.drawer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.drawer.dto.BodyDto;
import com.khoaluan.mxhgiaothong.drawer.dto.FooterDto;
import com.khoaluan.mxhgiaothong.drawer.dto.HeaderDto;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MenuAdapter extends RecyclerView.Adapter {

//    public static final String TAG = LogUtils.makeLogTag(MenuAdapter.class);

    private int currentPosition;

    public Context mContext;
    public List<Object> mItems;

    public MenuAdapter(Context context, List<Object> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onHeaderClicked(boolean isSelected);

        void onBodyClick(int position);

        void onFooterClick();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case AppConstants.MENU_HEADER:
                view = LayoutInflater.from(mContext).inflate(R.layout.header_menu, parent, false);
                return new HeaderHolder(view);
            case AppConstants.MENU_FOOTER:
                view = LayoutInflater.from(mContext).inflate(R.layout.footer_menu, parent, false);
                return new FooterHolder(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.body_menu, parent, false);
                return new BodyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case AppConstants.MENU_HEADER: {
                final HeaderHolder header = (HeaderHolder) holder;
                header.bind();
                break;
            }
            case AppConstants.MENU_FOOTER: {
                final FooterHolder footer = (FooterHolder) holder;
                footer.bind((FooterDto) mItems.get(position));
                break;
            }
            default: {
                final BodyHolder body = (BodyHolder) holder;
                body.bind((BodyDto) mItems.get(position));
                break;
            }
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mItems.get(position);
        if (object instanceof HeaderDto) {
            return AppConstants.MENU_HEADER;
        } else if (object instanceof BodyDto) {
            return AppConstants.MENU_BODY;
        } else {
            return AppConstants.MENU_FOOTER;
        }
    }

    class HeaderHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onHeaderClicked(
                    ((HeaderDto) mItems.get(getAdapterPosition())).isSelected
            );
        }

        public void bind() {

        }
    }

    class BodyHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

       @BindView(R.id.imv_icon)
               ImageView imvIcon;

       @BindView(R.id.tvTitle_Menu)
               TextView tvTitle;

        BodyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

       public void bind(BodyDto bodyDto) {
            tvTitle.setText(bodyDto.title);
            imvIcon.setImageResource(bodyDto.icon);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onBodyClick(getAdapterPosition());
        }
    }

    class FooterHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                onItemClickListener.onFooterClick();
        }

        public void bind(FooterDto footerDto) {

        }
    }
}