package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

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
    public void loadDetailListData(int albumId, int page);


    /**
     * 下拉刷新
     *
     * @param refreshLayout 控制器
     */
    public void refresh(RefreshLayout refreshLayout);


    /**
     * 上拉加载
     *
     * @param refreshlayout 控制器
     */
    public void loadMore(RefreshLayout refreshlayout);


}
