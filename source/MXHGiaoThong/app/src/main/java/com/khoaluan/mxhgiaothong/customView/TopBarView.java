package com.khoaluan.mxhgiaothong.customView;
import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khoaluan.mxhgiaothong.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_MENU;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_LOGOUT;
import static com.khoaluan.mxhgiaothong.AppConstants.RIGHT_SETTING;

/**
 * Created by HieuMinh on 4/4/2018.
 */

public class TopBarView extends LinearLayoutCompat {

    OnItemClickListener onItemClickListener;

    public void setOnClickListener(OnItemClickListener callback) {
        onItemClickListener = callback;
    }

    @BindView(R.id.imv_left)
    ImageView imv_left;

    @BindView(R.id.imv_right)
    ImageView imv_right;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.tv_left)
    TextView tv_left;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.lnTopbar)
    LinearLayout lnTopBar;

    @BindView(R.id.imv_logo_app_name)
    ImageView imvLogoApp;

    public TopBarView( Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TopBarView(Context context) {
        super(context);
        init(context,null);
    }

    private void init(Context context, AttributeSet atts){
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_top_bar,
                this,
                false
        );
//        LayoutInflater inflater = LayoutInflater.from(context);
//        inflater.inflate(R.layout.layout_top_bar, this, false);
        ButterKnife.bind(this,view);
        this.addView(view);
        imvLogoApp.setVisibility(VISIBLE);
        tv_title.setVisibility(GONE);
    }

    public interface OnItemClickListener {

        void onImvLeftClicked();

        void onImvRightClicked();

        void onTvLeftClicked();

        void onTvRightClicked();
    }

    public void setTextTitle(String title) {
        imvLogoApp.setVisibility(GONE);
        tv_title.setVisibility(VISIBLE);
        tv_title.setText(title);
    }

    public void setTextTitleVisible(int type) {
        tv_title.setVisibility(type);
    }

    public void setTextViewLeft(String text){
        tv_left.setVisibility(VISIBLE);
        imv_left.setVisibility(GONE);
        tv_left.setText(text);
    }

    public void setTextViewRight(String text){
        tv_right.setVisibility(VISIBLE);
        imv_right.setVisibility(GONE);
        tv_right.setText(text);
    }

    public void setImageViewLeft(int type){
        imv_left.setVisibility(VISIBLE);
        tv_left.setVisibility(GONE);
        if(type == LEFT_MENU){
            imv_left.setImageResource(R.drawable.ic_menu);
        } else {
            imv_left.setImageResource(R.drawable.ic_back);
        }
    }

    public void setImageViewRight(int type){
        imv_right.setVisibility(VISIBLE);
        tv_right.setVisibility(GONE);
        if(type == RIGHT_SETTING){
            imv_right.setImageResource(R.drawable.ic_setting);
        }else if(type == RIGHT_LOGOUT){
            imv_right.setImageResource(R.drawable.ic_logout);
        }
        //Todo: Can bo sung
    }

    @OnClick(R.id.tv_left)
    public void ClickTvLeft(){
        if(onItemClickListener == null) return;
        onItemClickListener.onTvLeftClicked();
    }

    @OnClick(R.id.tv_right)
    public void ClickTvRight(){
        if(onItemClickListener == null) return;
        onItemClickListener.onTvRightClicked();
    }

    @OnClick(R.id.imv_left)
    public void ClickImageLeft(){
        if(onItemClickListener == null) return;
        onItemClickListener.onImvLeftClicked();
    }

    @OnClick(R.id.imv_right)
    public void ClickImageRight(){
        if(onItemClickListener == null) return;
        onItemClickListener.onImvRightClicked();
    }

    public void setTranpatentTopBar(){
        lnTopBar.setBackgroundColor(getResources().getColor(R.color.transparent));
    }
}

