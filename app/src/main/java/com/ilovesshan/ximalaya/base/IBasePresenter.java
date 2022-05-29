package com.ilovesshan.ximalaya.base;

import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/29
 * @description:
 */
public interface IBasePresenter<T> {
    /**
     * 注册 viewController
     */

    public void registerViewController(T t);


    /**
     * 取消注册 viewController
     */
    public void unRegisterViewController(T t);
}
