package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public interface IAlbumDetailApi {

    /**
     * 请求数据 获取推荐列表
     *
     * @param mAlbumId id
     * @param page     页码
     * @param callBack 请求回调函数
     */
    void getAlbumDetailList(long mAlbumId, int page, IDataCallBack<TrackList> callBack);
}
