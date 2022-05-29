package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public interface IAlbumDetail extends IBasePresenter<IAlbumDetailViewController> {

    /**
     * 请求专辑详情对应列表数据
     */
    public void loadDetailListData(String albumId, String page, String sort);



}
