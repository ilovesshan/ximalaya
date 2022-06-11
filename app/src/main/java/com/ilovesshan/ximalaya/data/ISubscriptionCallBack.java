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
public interface ISubscriptionCallBack {
    /**
     * 添加订阅结果
     *
     * @param isSuccess 是否成功
     */
    void onAddSubscriptionResult(boolean isSuccess);


    /**
     * 删除订阅结果
     *
     * @param isSuccess 是否成功
     */
    void onDeleteSubscriptionResult(boolean isSuccess);


    /**
     * 查询订阅列表
     *
     * @param albumList 专辑列表
     * @param isSuccess 是否成功
     */
    void onSubscriptionListLoaded(List<Album> albumList, boolean isSuccess);
}
