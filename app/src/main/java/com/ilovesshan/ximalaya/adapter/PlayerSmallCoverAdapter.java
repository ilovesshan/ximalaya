package com.ilovesshan.ximalaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.utils.ViewUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/30
 * @description:
 */
public class PlayerSmallCoverAdapter extends PagerAdapter {
    private List<Track> mTracks = new ArrayList<>();


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View viewItem = LayoutInflater.from(container.getContext()).inflate(R.layout.item_player_samll_cover, container, false);
        container.addView(viewItem);

        ImageView smallCover = viewItem.findViewById(R.id.iv_player_small_cover);
        RequestOptions borderRadius = ViewUtils.makeBorderRadius(10);
        Glide.with(smallCover).load(mTracks.get(position).getCoverUrlLarge()).apply(borderRadius).into(smallCover);
        return viewItem;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setDat(List<Track> tracks) {
        mTracks.clear();
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }
}
