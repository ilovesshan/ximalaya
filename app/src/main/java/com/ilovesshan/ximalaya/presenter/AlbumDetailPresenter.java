package com.ilovesshan.ximalaya.presenter;

import androidx.annotation.Nullable;

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
     * @param page    "asc"表示喜马拉雅正序，"desc"表示喜马拉雅倒序，"time_asc"表示时间升序，"time_desc"表示时间降序，默认为"asc"
     * @param sort    当前第几页，不填默认为 1
     */

    @Override
    public void loadDetailListData(String albumId, String page, String sort) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, "4603856");
        map.put(DTransferConstants.SORT, sort);
        map.put(DTransferConstants.PAGE, page);
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    if (tracks != null) {
                        LogUtil.d(TAG, "onSuccess", "3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表");
                        LogUtil.d(TAG, "onSuccess", "tracks == " + tracks);
                        for (IAlbumDetailViewController viewController : mIAlbumDetailViewControllers) {
                            viewController.onLoadedDetailList(tracks);
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError", "3.2.4 专辑浏览，根据专辑ID获取专辑下的声音列表 数据获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
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
