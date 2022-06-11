package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */

public interface ISubscriptionViewController {
    /**
     * 添加订阅结果
     *
     * @param isSuccess 是否成功
     */
    void OnAddSubscriptionResult(boolean isSuccess);


    /**
     * 删除订阅结果
     *
     * @param isSuccess 是否成功
     */
    void OnDeleteSubscriptionResult(boolean isSuccess);


    /**
     * 查询订阅列表
     *
     * @param albumList 专辑列表
     */
    void OnSubscriptionListLoaded(List<Album> albumList);
}
