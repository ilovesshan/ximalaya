package com.ilovesshan.ximalaya.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public interface ISubscriptionDao {
    /**
     * 添加订阅
     *
     * @param album 专辑
     */
    void addSubscription(Album album);


    /**
     * 删除订阅
     *
     * @param albumId 专辑ID
     */
    void deleteSubscription(long albumId);


    /**
     * 查询订阅列表
     *
     */
    void querySubscriptionList();
}
