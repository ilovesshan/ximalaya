package com.ilovesshan.ximalaya.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/2
 * @description:
 */

@SuppressLint("NotifyDataSetChanged")
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.innerHolder> {

    List<Track> mTracks = new ArrayList<>();
    int mCurrentPlayIndex = 0;
    private OnPlayListTrackItemClickListener mOnPlayListTrackItemClickListener = null;
    private OnPlayListTrackDownloadClickListener mOnPlayListTrackDownloadClickListener;

    @NonNull
    @Override
    public innerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_track_list, parent, false);
        return new innerHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull innerHolder holder, int position) {
        View itemView = holder.itemView;

        ImageView mIvPlayerListTrackIcon = itemView.findViewById(R.id.iv_player_list_track_icon);
        TextView mTvPlayerListTrackTitle = itemView.findViewById(R.id.tv_player_list_track_title);
        ImageView mIvPlayerListTrackDownload = itemView.findViewById(R.id.iv_player_list_track_download);

        mIvPlayerListTrackIcon.setVisibility(mCurrentPlayIndex == position ? ViewGroup.VISIBLE : View.GONE);
        mTvPlayerListTrackTitle.setText(mTracks.get(position).getTrackTitle());
        mTvPlayerListTrackTitle.setTextColor(itemView.getResources().getColor(mCurrentPlayIndex == position ? R.color.app_primary : R.color.black));

        //TODO 播放列表底部弹窗中 下载按钮被点击逻辑暂未实现
        mIvPlayerListTrackDownload.setOnClickListener(v -> {
            mOnPlayListTrackDownloadClickListener.onClick(position);
        });

        mTvPlayerListTrackTitle.setOnClickListener(v -> {
            mOnPlayListTrackItemClickListener.onClick(position);
            setCurrentPlayIndex(position);
        });
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public class innerHolder extends RecyclerView.ViewHolder {
        public innerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public void setData(List<Track> tracks) {
        mTracks.clear();
        mTracks = tracks;
        notifyDataSetChanged();
    }

    public void setCurrentPlayIndex(int playIndex) {
        this.mCurrentPlayIndex = playIndex;

        notifyDataSetChanged();
    }


    public void setOnPlayListTrackItemClickListener(OnPlayListTrackItemClickListener listener) {
        this.mOnPlayListTrackItemClickListener = listener;
    }

    public interface OnPlayListTrackItemClickListener {
        public void onClick(int playIndex);
    }

    public void setOnPlayListTrackDownloadClickListener(OnPlayListTrackDownloadClickListener listener) {
        this.mOnPlayListTrackDownloadClickListener = listener;
    }

    public interface OnPlayListTrackDownloadClickListener {
        public void onClick(int playIndex);
    }
}
