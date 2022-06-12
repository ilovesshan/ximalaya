package com.ilovesshan.ximalaya.presenter;

import com.ilovesshan.ximalaya.data.IHistoryCallBack;
import com.ilovesshan.ximalaya.data.impl.HistoryDaoImpl;
import com.ilovesshan.ximalaya.interfaces.IHistory;
import com.ilovesshan.ximalaya.interfaces.IHistoryViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/12
 * @description:
 */
public class HistoryPresenter implements IHistory, IHistoryCallBack {
    private static final String TAG = "HistoryPresenter";

    private final HistoryDaoImpl mHistoryDao;

    HashMap<Integer, Album> mAlbumHashMap = new HashMap<>();
    private List<IHistoryViewController> mIHistoryViewControllers = new ArrayList<>();


    private HistoryPresenter() {
        // 获取 订阅Dao
        mHistoryDao = HistoryDaoImpl.getInstance();
        mHistoryDao.setIHistoryCallBack(this);
        mHistoryDao.queryAlbumList();


    }

    private static HistoryPresenter sHistoryPresenter = null;

    public static HistoryPresenter getInstance() {

        if (sHistoryPresenter == null) {
            synchronized (HistoryPresenter.class) {
                if (sHistoryPresenter == null) {
                    sHistoryPresenter = new HistoryPresenter();
                }
            }
        }
        return sHistoryPresenter;
    }


    @Override
    public void addAlbum(Album album) {
        // 判断是否已经添加过了
        boolean added = isAdded((int) album.getId());
        if (added) {
            //TODO 添加历史记录时 暂未处理已经添过的记录在数据库中排序问题

            // 已经添加过了
        } else {
            // 未添加
            mHistoryDao.addAlbum(album);
        }
    }

    @Override
    public void queryAlbumList() {
        mHistoryDao.queryAlbumList();
    }

    @Override
    public void deleteAlbumById(int albumID) {
        mHistoryDao.deleteAlbumById(albumID);
    }

    @Override
    public void deleteAllAlbum() {
        mHistoryDao.deleteAllAlbum();
    }

    @Override
    public boolean isAdded(int id) {
        return mAlbumHashMap.get(id) == null;
    }


    @Override
    public void onAddAlbumResult(boolean isSuccess) {
        LogUtil.d(TAG, "onAddAlbumResult", "历史记录添加结果:" + isSuccess);
        for (IHistoryViewController iHistoryViewController : mIHistoryViewControllers) {
            iHistoryViewController.onAddAlbumResult(isSuccess);
        }
    }

    @Override
    public void onQueryAlbumListResult(List<Album> albumList, boolean isSuccess) {
        LogUtil.d(TAG, "onAddAlbumResult", "历史记录查询结果:" + albumList.size());
        mAlbumHashMap.clear();
        for (Album album : albumList) {
            mAlbumHashMap.put((int) album.getId(), album);
        }
        for (IHistoryViewController iHistoryViewController : mIHistoryViewControllers) {
            iHistoryViewController.onQueryAlbumListResult(albumList, isSuccess);
        }
    }

    @Override
    public void onDeleteAlbumByIdResult(boolean isSuccess) {
        LogUtil.d(TAG, "onAddAlbumResult", "历史记录根据ID删除结果:" + isSuccess);
        for (IHistoryViewController iHistoryViewController : mIHistoryViewControllers) {
            iHistoryViewController.onDeleteAlbumByIdResult(isSuccess);
        }
    }

    @Override
    public void onDeleteAllAlbumResult(boolean isSuccess) {
        LogUtil.d(TAG, "onAddAlbumResult", "历史记录删除全部结果:" + isSuccess);
        for (IHistoryViewController iHistoryViewController : mIHistoryViewControllers) {
            iHistoryViewController.onDeleteAllAlbumResult(isSuccess);
        }
    }


    @Override
    public void registerViewController(IHistoryViewController iHistoryViewController) {
        if (!mIHistoryViewControllers.contains(iHistoryViewController)) {
            mIHistoryViewControllers.add(iHistoryViewController);
        }
    }

    @Override
    public void unRegisterViewController(IHistoryViewController iHistoryViewController) {
        mIHistoryViewControllers.remove(iHistoryViewController);
    }
}
