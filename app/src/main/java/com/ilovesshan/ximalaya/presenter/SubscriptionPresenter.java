package com.ilovesshan.ximalaya.presenter;

import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.data.ISubscriptionCallBack;
import com.ilovesshan.ximalaya.data.impl.SubscriptionDaoImpl;
import com.ilovesshan.ximalaya.interfaces.ISubscription;
import com.ilovesshan.ximalaya.interfaces.ISubscriptionViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public class SubscriptionPresenter implements ISubscription, ISubscriptionCallBack {

    private static final String TAG = "SubscriptionPresenter";

    public ArrayList<ISubscriptionViewController> mISubscriptionViewControllers = new ArrayList<>();
    private SubscriptionDaoImpl mSubscriptionDao;
    private HashMap<Integer, Album> mAlbumHashMap = new HashMap<>();


    private SubscriptionPresenter() {
        // 获取 订阅Dao
        mSubscriptionDao = SubscriptionDaoImpl.getInstance();
        mSubscriptionDao.setISubscriptionCallBack(this);
    }

    private static SubscriptionPresenter sSubscriptionPresenter = null;

    public static SubscriptionPresenter getInstance() {

        if (sSubscriptionPresenter == null) {
            synchronized (SubscriptionPresenter.class) {
                if (sSubscriptionPresenter == null) {
                    sSubscriptionPresenter = new SubscriptionPresenter();
                }
            }
        }
        return sSubscriptionPresenter;
    }


    // 订阅专辑
    @Override
    public void addSubscription(Album album) {
        mSubscriptionDao.addSubscription(album);
    }

    // 取消订阅专辑
    @Override
    public void deleteSubscription(int id) {
        mSubscriptionDao.deleteSubscription(id);
    }

    // 获取 订阅的专辑列表
    @Override
    public void querySubscriptionList() {
        mSubscriptionDao.querySubscriptionList();
    }

    // 该专辑 是否已经被订阅过了
    @Override
    public boolean isSubscription(int id) {
        return mAlbumHashMap.get(id) != null;
    }


    // 添加专辑结果回调
    @Override
    public void onAddSubscriptionResult(boolean isSuccess) {
        for (ISubscriptionViewController iSubscriptionViewController : mISubscriptionViewControllers) {
            iSubscriptionViewController.onAddSubscriptionResult(isSuccess);
        }
        refreshList();
    }

    // 删除专辑结果回调
    @Override
    public void onDeleteSubscriptionResult(boolean isSuccess) {
        for (ISubscriptionViewController iSubscriptionViewController : mISubscriptionViewControllers) {
            iSubscriptionViewController.onDeleteSubscriptionResult(isSuccess);
        }
        refreshList();
    }


    // 订阅专辑列表结果回调
    @Override
    public void onSubscriptionListLoaded(List<Album> albumList, boolean isSuccess) {

        // 保证获取到的数据是最新的
        mAlbumHashMap.clear();

        LogUtil.d(TAG, "OnSubscriptionListLoaded", "订阅专辑列表结果回调 =" + albumList);
        for (Album album : albumList) {
            LogUtil.d(TAG, "OnSubscriptionListLoaded", "" + album.getAlbumTitle());
            mAlbumHashMap.put((int) album.getId(), album);
        }

        for (ISubscriptionViewController iSubscriptionViewController : mISubscriptionViewControllers) {
            iSubscriptionViewController.onSubscriptionListLoaded(albumList);
        }
    }

    // 注册 controller
    @Override
    public void registerViewController(ISubscriptionViewController controller) {
        if (!mISubscriptionViewControllers.contains(controller)) {
            mISubscriptionViewControllers.add(controller);
        }
    }


    // 取消注册 controller
    @Override
    public void unRegisterViewController(ISubscriptionViewController controller) {
        mISubscriptionViewControllers.remove(controller);
    }


    // 删除/添加 之后 通知界面更新(查一次数据)
    public void refreshList() {
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                querySubscriptionList();
            }
        }, 500);
    }
}
