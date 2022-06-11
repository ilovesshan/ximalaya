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
public interface ISubscription extends IBasePresenter<ISubscriptionViewController> {
    /**
     * 添加订阅
     *
     * @param album 专辑
     */
    void addSubscription(Album album);


    /**
     * 删除订阅
     *
     * @param id 专辑ID
     */
    void deleteSubscription(int id);


    /**
     * 查询订阅列表
     */
    void querySubscriptionList();


    /**
     * 是否 已经订阅过专辑
     *
     * @param id 专辑ID
     * @return
     */
    boolean isSubscription(int id);

}
