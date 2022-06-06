package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/27
 * @description:
 */

public interface IRecommendApi {
    /**
     * 请求数据 获取推荐列表
     *
     * @param callBack 请求回调函数
     */
    void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack);
}
