package com.ilovesshan.ximalaya.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.utils.NumberUtils;
import com.ilovesshan.ximalaya.utils.ViewUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description: 推荐列表 适配器
 */

@SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.InnerHolder> {
    private List<Album> mList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        return new InnerHolder(viewItem);
    }


    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        View itemView = holder.itemView;
        Album album = mList.get(position);
        // 方便回调函数中获取position
        itemView.setTag(position);

        // 监听点击 并将当前index和对应数据传递给RecommendFragment
        itemView.setOnClickListener((v) -> {
            if (mOnItemClickListener != null) {
                int index = (int) v.getTag();
                mOnItemClickListener.onClick(index, mList.get(index));
            }
        });

        // 长按事件
        itemView.setOnLongClickListener((v) -> {
            if (mOnItemLongClickListener != null) {
                int index = (int) v.getTag();
                mOnItemLongClickListener.onLongClick(index, mList.get(index));
            }
            return true;
        });


        // 专辑封面小
        ImageView ivRecommendItemCoverUrlSmall = itemView.findViewById(R.id.iv_recommend_item_cover_url_small);
        // 专辑标题
        TextView tvRecommendItemTitle = itemView.findViewById(R.id.tv_recommend_item_title);
        // 专辑介绍
        TextView tvRecommendItemIntroduce = itemView.findViewById(R.id.tv_recommend_item_introduce);
        // 专辑播放次数
        TextView tvRecommendItemPlayCount = itemView.findViewById(R.id.tv_recommend_item_play_count);
        // 	专辑评分
        TextView tvRecommendItemAlbumScore = itemView.findViewById(R.id.tv_recommend_item_album_score);

        // 设置图片圆角
        RequestOptions borderRadius = ViewUtils.makeBorderRadius(20);

        // 加载数据
        Glide.with(ivRecommendItemCoverUrlSmall).load(album.getCoverUrlSmall()).apply(borderRadius).into(ivRecommendItemCoverUrlSmall);
        tvRecommendItemTitle.setText(album.getAlbumTitle());
        tvRecommendItemIntroduce.setText(album.getAlbumIntro());
        tvRecommendItemPlayCount.setText(NumberUtils.number2CountingUnit(album.getPlayCount()));
        tvRecommendItemAlbumScore.setText(album.getSubscribeCount() + "");

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public void setData(List<Album> albumList) {
        this.mList.clear();
        this.mList = albumList;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.mOnItemLongClickListener = longClickListener;
    }


    public interface OnItemClickListener {
        void onClick(int index, Album album);
    }

    public interface OnItemLongClickListener {
        void onLongClick(int index, Album album);
    }

}
