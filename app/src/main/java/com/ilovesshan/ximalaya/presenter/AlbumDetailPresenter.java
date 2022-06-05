package com.ilovesshan.ximalaya.presenter;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetail;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.views.UILoader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public class AlbumDetailPresenter implements IAlbumDetail {

    private static final String TAG = "AlbumDetailPresenter";

    private final List<IAlbumDetailViewController> mIAlbumDetailViewControllers = new ArrayList<>();
    private Album mAlbum;

    public static AlbumDetailPresenter sAlbumDetailPresenter = null;

    List<Track> mTracks = new ArrayList<>();
    private int mAlbumId = -1;
    private int mPage = 1;

    private AlbumDetailPresenter() {
    }

    public static AlbumDetailPresenter getInstance() {
        if (sAlbumDetailPresenter == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sAlbumDetailPresenter == null) {
                    sAlbumDetailPresenter = new AlbumDetailPresenter();
                }
            }
        }
        return sAlbumDetailPresenter;
    }

    /**
     * 3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表
     *
     * @param albumId 专辑ID
     * @param page    当前第几页，不填默认为 1
     */
    @Override
    public void loadDetailListData(int albumId, int page) {
        this.mAlbumId = albumId;
        this.mPage = page;
        mTracks.clear();
        handleRequestData(false, null);
    }

    /**
     * 请求数据
     *
     * @param isLoadMore    是否是加载更多
     * @param refreshLayout 刷新控制器
     */
    private void handleRequestData(boolean isLoadMore, RefreshLayout refreshLayout) {
        for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
            viewController.onLoading();
        }

        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, mAlbumId + "");
        map.put(DTransferConstants.PAGE, mPage + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Constants.RECOMMEND_DETAIL_LIST_SIZE));
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG, "onSuccess", "3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表");
                    LogUtil.d(TAG, "onSuccess", "tracks == " + tracks);
                    if (tracks != null) {
                        if (tracks.size() == 0) {
                            // 数据为空
                            if (refreshLayout != null) {
                                notifyRefreshLayoutUpdate(isLoadMore, false, false, refreshLayout);
                                notifyUiLoaderUpdate(UILoader.UILoaderState.SUCCESS);
                            } else {
                                notifyUiLoaderUpdate(UILoader.UILoaderState.EMPTY);
                            }
                        } else {
                            if (refreshLayout != null) {
                                notifyRefreshLayoutUpdate(isLoadMore, true, false, refreshLayout);
                            }
                            mTracks.addAll(tracks);
                            notifyUiLoaderUpdate(UILoader.UILoaderState.SUCCESS);
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError", "3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表 数据获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
                notifyRefreshLayoutUpdate(isLoadMore, true, true, refreshLayout);
                notifyUiLoaderUpdate(UILoader.UILoaderState.ERROR);
            }
        });
    }

    /**
     * 通知 UILoader 更新状态
     *
     * @param state UILoader 状态
     */
    public void notifyUiLoaderUpdate(UILoader.UILoaderState state) {
        switch (state) {
            case EMPTY:
                for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                    viewController.onLoadEmpty();
                }
                break;
            case SUCCESS:
                for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                    viewController.onLoadedDetailList(mTracks);
                }
                break;
            case ERROR:
                for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                    viewController.onLoadError();
                }
                break;
        }
    }


    /**
     * 通知 UI刷新控制器 更新状态
     *
     * @param isLoadMore    是否是加载更多
     * @param hasMore       是否还有更多的数据
     * @param isError       是否是加载失败
     * @param refreshLayout UI刷新控制器
     */
    public void notifyRefreshLayoutUpdate(boolean isLoadMore, boolean hasMore, boolean isError, RefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            if (isLoadMore) {
                // 加载更多情况 但是加载失败了 需要将page--
                if (isError) {
                    mPage--;
                }
                refreshLayout.finishLoadMore(0, true, !hasMore);
            } else {
                // 不是加载更多情况 就是刷新 需要将mTracks清空, page改成1
                if (hasMore) {
                    mPage = 1;
                    mTracks.clear();
                }
                refreshLayout.finishRefresh(0, true, !hasMore);
            }
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void refresh(RefreshLayout refreshlayout) {
        mTracks.clear();
        mPage=1;
        handleRequestData(false, refreshlayout);
    }

    /**
     * 上拉加载
     */
    @Override
    public void loadMore(RefreshLayout refreshlayout) {
        this.mPage++;
        handleRequestData(true, refreshlayout);
    }

    @Override
    public void registerViewController(IAlbumDetailViewController viewController) {
        if (!this.mIAlbumDetailViewControllers.contains(viewController)) {
            this.mIAlbumDetailViewControllers.add(viewController);
            if (mAlbum != null) {
                viewController.onLoadedDetail(mAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewController(IAlbumDetailViewController viewController) {
        this.mIAlbumDetailViewControllers.remove(viewController);
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }
}
