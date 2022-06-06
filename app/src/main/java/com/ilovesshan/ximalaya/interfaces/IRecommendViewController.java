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
     void onDataLoaded(List<Album> albumList);

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
