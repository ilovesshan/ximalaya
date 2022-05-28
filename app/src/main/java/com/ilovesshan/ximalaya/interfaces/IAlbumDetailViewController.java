package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public interface IAlbumDetailViewController {
    /**
     * 请求专辑详情对应列表数据完成
     */
    public void onLoadedDetailList(List<Track> trackList);

    /**
     * 请求专辑详情数据完成
     */
    public void onLoadedDetail(Album album);
}
