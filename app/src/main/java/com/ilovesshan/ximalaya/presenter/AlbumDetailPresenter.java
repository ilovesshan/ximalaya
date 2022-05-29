package com.ilovesshan.ximalaya.presenter;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetail;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
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
     * @param sort    "asc"表示喜马拉雅正序，"desc"表示喜马拉雅倒序，"time_asc"表示时间升序，"time_desc"表示时间降序，默认为"asc"
     */

    @Override
    public void loadDetailListData(String albumId, String page, String sort) {
        for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
            viewController.onLoading();
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, "4603856");
        map.put(DTransferConstants.SORT, sort);
        map.put(DTransferConstants.PAGE, page);
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
                            for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                                viewController.onLoadEmpty();
                            }
                        } else {
                            for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                                viewController.onLoadedDetailList(tracks);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError", "3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表 数据获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
                for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                    viewController.onLoadError();
                }
            }
        });
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
