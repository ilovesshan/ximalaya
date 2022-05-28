package com.ilovesshan.ximalaya.interfaces;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public interface IAlbumDetail {

    /**
     * 请求专辑详情对应列表数据
     */
    public void loadDetailListData(String albumId, String page, String sort);


    /**
     * 注册 viewController
     */

    public void registerViewController(IAlbumDetailViewController viewController);


    /**
     * 取消注册 viewController
     */
    public void unRegisterViewController(IAlbumDetailViewController viewController);
}
