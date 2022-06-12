package com.ilovesshan.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public interface IHistoryDao {


    /**
     * 添加一条历史记录
     *
     * @param album 专辑信息
     */
    void addAlbum(Album album);


    /**
     * 查询专辑历史列表
     */
    void queryAlbumList();


    /**
     * 删除一条历史记录
     *
     * @param albumID 专辑ID
     */
    void deleteAlbumById(int albumID);


    /**
     * 清空历史记录
     */
    void deleteAllAlbum();
}
