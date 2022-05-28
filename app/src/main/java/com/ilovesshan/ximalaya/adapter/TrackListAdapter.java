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
import com.ilovesshan.ximalaya.utils.NumberUtils;
import com.ilovesshan.ximalaya.utils.TimeUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */

@SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.InnerHolder> {
    private static final String TAG = "TrackListAdapter";

    List<Track> mTracks = new ArrayList<>();

    OnDownloadClickListener mOnDownloadClickListener = null;
    OnItemClickListener mOnItemClickListener = null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracks, parent, false);
        return new InnerHolder(viewItem);
    }


    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        View viewItem = holder.itemView;
        viewItem.setTag(position);

        TextView mTvTrackOrder = viewItem.findViewById(R.id.tv__track_order);
        TextView mTvTrackTitle = viewItem.findViewById(R.id.tv_track_title);
        TextView mTvTrackPlayCount = viewItem.findViewById(R.id.tv_track_play_count);
        TextView mTvTrackPlayDuration = viewItem.findViewById(R.id.tv_track_play_duration);
        TextView mTvTrackCommentCount = viewItem.findViewById(R.id.tv_track_comment_count);
        TextView mTvTrackUpdateTime = viewItem.findViewById(R.id.tv__track_update_time);
        ImageView mIvTrackDownload = viewItem.findViewById(R.id.iv__track_download);
        mIvTrackDownload.setTag(position);

        mTvTrackOrder.setText((position + 1) + "");
        mTvTrackTitle.setText(mTracks.get(position).getTrackTitle());
        mTvTrackPlayCount.setText(NumberUtils.number2CountingUnit(mTracks.get(position).getPlayCount()));
        // getDuration() 返回的是秒 需要x1000转成毫秒计算
        mTvTrackPlayDuration.setText(TimeUtils.timeFormatToString((mTracks.get(position).getDuration() * 1000), "mm:ss"));
        mTvTrackCommentCount.setText(NumberUtils.number2CountingUnit(mTracks.get(position).getCommentCount()));
        mTvTrackUpdateTime.setText(TimeUtils.timeFormatToString(mTracks.get(position).getUpdatedAt(), "yyyy-MM-dd"));
        mIvTrackDownload.setOnClickListener(v -> {
            if (mOnDownloadClickListener != null) {
                mOnDownloadClickListener.onClick((int) v.getTag(), mTracks.get((int) v.getTag()));
            }
        });
        viewItem.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onClick((int) v.getTag(), mTracks.get((int) v.getTag()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setDat(List<Track> tracks) {
        mTracks.clear();
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }


    public void setOnDownloadClickListener(OnDownloadClickListener listener) {
        this.mOnDownloadClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnDownloadClickListener {
        public void onClick(int position, Track track);
    }

    public interface OnItemClickListener {
        public void onClick(int position, Track track);
    }
}
