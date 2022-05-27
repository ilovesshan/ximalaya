package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/27
 * @description:
 */
public interface IRecommendViewController {
    /**
     * 请求数据完成
     */
    public void onDataLoaded(List<Album> albumList);

    /**
     * 加载失败
     */
    public void onLoadError();


    /**
     * 无数据
     */
    public void onLoadEmpty();


    /**
     * 加载中
     */
    public void onLoading();
}
