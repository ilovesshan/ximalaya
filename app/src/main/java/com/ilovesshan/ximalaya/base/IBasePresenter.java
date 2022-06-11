package com.ilovesshan.ximalaya.base;

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

     void registerViewController(T t);


    /**
     * 取消注册 viewController
     */
     void unRegisterViewController(T t);
}
