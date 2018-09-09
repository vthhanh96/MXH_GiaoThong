package com.khoaluan.mxhgiaothong.drawer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.drawer.dto.BodyDto;
import com.khoaluan.mxhgiaothong.drawer.dto.FooterDto;
import com.khoaluan.mxhgiaothong.drawer.dto.HeaderDto;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.utils.SingleClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


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
            {

        @BindView(R.id.menu_header_avatar)
        CircleImageView avatar;
        @BindView(R.id.menu_header_email)
        TextView email;
        @BindView(R.id.menu_header_fullName)
        TextView fullName;

        HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onHeaderClick);
        }
        SingleClickListener onHeaderClick = new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onItemClickListener.onHeaderClicked(
                        ((HeaderDto) mItems.get(getAdapterPosition())).isSelected
                );
            }
        };

        public void bind() {
            User user = PreferManager.getInstance(mContext).getUser();
            if (user == null) return;
            Glide.with(mContext).load(user.getAvatar()).apply(RequestOptions.circleCropTransform()).into(avatar);
            fullName.setText(user.getFullName());
            email.setText(user.getEmail());
        }
    }

    class BodyHolder
            extends RecyclerView.ViewHolder {

        @BindView(R.id.view_select)
        View viewSelect;

        @BindView(R.id.imv_icon)
        ImageView imvIcon;

        @BindView(R.id.tvTitle_Menu)
        TextView tvTitle;

        BodyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onBodyClick);
        }

        public void bind(BodyDto bodyDto) {
            tvTitle.setText(bodyDto.title);
            imvIcon.setImageResource(bodyDto.icon);
            if (bodyDto.isSelected) {
                viewSelect.setBackgroundColor(Color.WHITE);
            } else {
                viewSelect.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        SingleClickListener onBodyClick = new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onItemClickListener.onBodyClick(getAdapterPosition());
            }
        };
    }

    class FooterHolder
            extends RecyclerView.ViewHolder
           {

        @BindView(R.id.imvIconFooter) ImageView iconFooter;
        @BindView(R.id.tvTitleFooter) TextView titleFooter;

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(footerClick);
        }
        SingleClickListener footerClick = new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onItemClickListener.onFooterClick();
            }
        };

        public void bind(FooterDto footerDto) {
            User user = PreferManager.getInstance(mContext).getUser();
            if (user == null) {
                iconFooter.setImageResource(R.drawable.ic_login);
                titleFooter.setText("Đăng nhập");
            }else {
                iconFooter.setImageResource(R.drawable.ic_logout);
                titleFooter.setText("Đăng xuất");
            }
        }
    }
}
