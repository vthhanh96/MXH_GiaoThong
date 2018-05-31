package com.khoaluan.mxhgiaothong.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.Reaction;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hong Hanh on 4/17/2018.
 */

public class PostAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {

    private User mUser;

    public PostAdapter(User user) {
        super(R.layout.item_post, new ArrayList<Post>());
        mUser = user;
    }

    @Override
    protected void convert(BaseViewHolder helper, Post item) {
        helper.setText(R.id.tvName, item.getCreator().getFullName());
        helper.setText(R.id.tvPlace, item.getPlace());
        helper.setText(R.id.tvTime, DateUtils.getTimeAgo(mContext, item.getCreatedDate()));
        helper.setText(R.id.tvContent, item.getContent());
        helper.setText(R.id.tvLikeAmount, String.valueOf(item.getLikeAmount()));
        helper.setText(R.id.tvDislikeAmount, String.valueOf(item.getDislikeAmount()));
        helper.setText(R.id.txtCommentAmount, mContext.getString(R.string.comment_amount, item.getComments().size()));

        int typeReaction = isReaction(item.getReaction());

        if(typeReaction == 1) {
            helper.setImageResource(R.id.imgLike, R.drawable.ic_like_color);
            helper.setImageResource(R.id.imgDislike, R.drawable.ic_dislike);
        } else if (typeReaction == 2){
            helper.setImageResource(R.id.imgDislike, R.drawable.ic_dislike_color);
            helper.setImageResource(R.id.imgLike, R.drawable.ic_like);
        } else {
            helper.setImageResource(R.id.imgDislike, R.drawable.ic_dislike);
            helper.setImageResource(R.id.imgLike, R.drawable.ic_like);
        }

        ImageView imgAvatar = helper.getView(R.id.imgAvatar);
        ImageView imgContent = helper.getView(R.id.imgContent);

        Glide.with(mContext).load(item.getCreator().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(imgAvatar);
        Glide.with(mContext).load(item.getImageUrl()).into(imgContent);

        helper.setText(R.id.tvCategory, item.getCategory().getName());

        switch (item.getLevel()) {
            case 0:
                helper.setBackgroundRes(R.id.tvCategory, R.drawable.bg_corner_solid_green);
                break;
            case 1:
                helper.setBackgroundRes(R.id.tvCategory, R.drawable.bg_corner_solid_yellow);
                break;
            case 2:
                helper.setBackgroundRes(R.id.tvCategory, R.drawable.bg_corner_solid_orange);
                break;
            case 3:
                helper.setBackgroundRes(R.id.tvCategory, R.drawable.bg_corner_solid_red);
                break;
        }

        helper.addOnClickListener(R.id.llLike);
        helper.addOnClickListener(R.id.llDislike);
        helper.addOnClickListener(R.id.llComments);
        helper.addOnClickListener(R.id.imgAvatar);
        helper.addOnClickListener(R.id.imgPostOptions);
    }

    private int isReaction(List<Reaction> reactions) {
        if(reactions == null || mUser == null) return 0;
        for(Reaction reaction : reactions) {
            if(reaction.getCreator().getId() == mUser.getId()) {
                return reaction.getStatus_reaction();
            }
        }
        return 0;
    }
}
