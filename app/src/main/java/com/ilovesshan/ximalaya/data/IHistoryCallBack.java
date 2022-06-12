package com.ilovesshan.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public interface IHistoryCallBack {


    /**
     * 添加一条历史记录回调结果
     *
     * @param isSuccess 是否成功
     */
    void onAddAlbumResult(boolean isSuccess);


    /**
     * 查询专辑历史列表 回调结果
     *
     * @param albumList 专辑列表
     * @param isSuccess 是否成功
     */
    void onQueryAlbumListResult(List<Album> albumList, boolean isSuccess);


    /**
     * 删除一条历史记录 回调结果
     *
     * @param isSuccess 是否成功
     */
    void onDeleteAlbumByIdResult(boolean isSuccess);


    /**
     * 清空历史记录 回调结果
     *
     * @param isSuccess 是否成功
     */
    void onDeleteAllAlbumResult(boolean isSuccess);


}
