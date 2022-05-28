package com.ilovesshan.ximalaya.interfaces;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/27
 * @description:
 */
public interface IRecommend {
    /**
     * 请求数据
     */
    public void loadedData();


    /**
     * 注册 viewController
     */

    public void registerViewController(IRecommendViewController viewController);


    /**
     * 取消注册 viewController
     */
    public void unRegisterViewController(IRecommendViewController viewController);


}
