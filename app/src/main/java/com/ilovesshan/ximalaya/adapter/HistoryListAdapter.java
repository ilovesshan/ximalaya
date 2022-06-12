package com.ilovesshan.ximalaya.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.utils.NumberUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description: 历史记录列表 适配器
 */

@SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.InnerHolder> {
    private List<Album> mList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new InnerHolder(viewItem);
    }


    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        View itemView = holder.itemView;
        Album album = mList.get(position);
        // 方便回调函数中获取position
        itemView.setTag(position);

        // 监听点击
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


        // 排序
        TextView tvHistoryOrder = itemView.findViewById(R.id.tv_history_order);
        // 专辑标题
        TextView tvHistoryItemTitle = itemView.findViewById(R.id.tv_history_item_title);
        // 专辑播放次数
        TextView tvHistoryItemPlayCount = itemView.findViewById(R.id.tv_history_item_play_count);
        // 	专辑评分
        TextView tvHistoryItemAlbumScore = itemView.findViewById(R.id.tv_history_item_album_score);

        // 加载数据
        tvHistoryOrder.setText((position + 1) + "");
        tvHistoryItemTitle.setText(album.getAlbumTitle());
        tvHistoryItemPlayCount.setText(NumberUtils.number2CountingUnit(album.getPlayCount()));
        tvHistoryItemAlbumScore.setText(album.getSubscribeCount() + "");
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
