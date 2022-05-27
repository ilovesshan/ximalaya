package com.ilovesshan.ximalaya.presenter;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IRecommend;
import com.ilovesshan.ximalaya.interfaces.IRecommendViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/27
 * @description:
 */
public class RecommendPresenter implements IRecommend {
    private static final String TAG = "RecommendPresenter";

    List<IRecommendViewController> mViewControllers = new ArrayList<>();

    private RecommendPresenter() {
    }

    public static RecommendPresenter sRecommendPresenter = null;

    /**
     * 单例模式
     *
     * @return RecommendPresenter
     */
    public static RecommendPresenter getInstance() {
        if (sRecommendPresenter == null) {
            synchronized (RecommendPresenter.class) {
                if (sRecommendPresenter == null) {
                    sRecommendPresenter = new RecommendPresenter();
                }
            }
        }
        return sRecommendPresenter;
    }


    @Override
    public void loadedData() {
        requestRecommendList();
    }

    @Override
    public void registerViewController(IRecommendViewController viewController) {
        if (mViewControllers != null && !mViewControllers.contains(viewController)) {
            mViewControllers.add(viewController);
        }
    }

    @Override
    public void uRegisterViewController(IRecommendViewController viewController) {
        if (mViewControllers != null) {
            mViewControllers.clear();
        }
    }

    /**
     * 3.10.6 获取猜你喜欢专辑
     * <p>
     * 获取推荐列数据
     */
    private void requestRecommendList() {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_LIST_SIZE + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "onSuccess", "3.10.6 获取猜你喜欢专辑 数据获取成功");
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    // 通知UI 更新界面
                    if (mViewControllers != null) {
                        for (IRecommendViewController viewController : mViewControllers) {
                            viewController.onDataLoaded(albumList);
                        }
                    }
                    LogUtil.d(TAG, "onSuccess", "albumList" + albumList.size());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onSuccess", "3.10.6 获取猜你喜欢专辑 数据获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
            }
        });
    }

}
