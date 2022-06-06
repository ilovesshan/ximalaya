package com.ilovesshan.ximalaya.api;

import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IRecommendApi;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/7
 * @description:
 */
public class RecommendApi implements IRecommendApi {

    //TODO 代码待优化
    private RecommendApi() {
    }

    private static RecommendApi mRecommendApi = null;

    public static RecommendApi getInstance() {
        if (mRecommendApi == null) {
            synchronized (RecommendApi.class) {
                if (mRecommendApi == null) {
                    mRecommendApi = new RecommendApi();
                }
            }
        }
        return mRecommendApi;
    }

    @Override
    public void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_LIST_SIZE + "");
        CommonRequest.getGuessLikeAlbum(map, callBack);
    }
}
