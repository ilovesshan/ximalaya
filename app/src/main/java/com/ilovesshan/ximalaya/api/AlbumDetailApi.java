package com.ilovesshan.ximalaya.api;

import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailApi;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/7
 * @description:
 */
public class AlbumDetailApi implements IAlbumDetailApi {

    //TODO 代码待优化
    private AlbumDetailApi() {
    }

    private static AlbumDetailApi mAlbumDetailApi = null;

    public static AlbumDetailApi getInstance() {
        if (mAlbumDetailApi == null) {
            synchronized (AlbumDetailApi.class) {
                if (mAlbumDetailApi == null) {
                    mAlbumDetailApi = new AlbumDetailApi();
                }
            }
        }
        return mAlbumDetailApi;
    }


    @Override
    public void getAlbumDetailList(long mAlbumId, int page, IDataCallBack<TrackList> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, mAlbumId + "");
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Constants.RECOMMEND_DETAIL_LIST_SIZE));
        CommonRequest.getTracks(map, callBack);
    }
}
