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
    void onLoadedDetailList(List<Track> trackList);

    /**
     * 请求专辑详情数据完成
     */
    void onLoadedDetail(Album album);

    /**
     * 加载失败
     */
    void onLoadError();


    /**
     * 无数据
     */
    void onLoadEmpty();


    /**
     * 加载中
     */
    void onLoading();

}
