package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public interface IHistory extends IBasePresenter<IHistoryViewController> {


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


    /**
     * 是否 已经添加过专辑
     *
     * @param id 专辑ID
     * @return
     */
    boolean isAdded(int id);
}
