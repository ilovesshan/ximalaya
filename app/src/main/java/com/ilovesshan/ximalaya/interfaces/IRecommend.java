package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/27
 * @description:
 */
public interface IRecommend extends IBasePresenter<IRecommendViewController> {
    /**
     * 请求数据
     */
    public void loadedData();
}
