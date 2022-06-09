package com.ilovesshan.ximalaya.api;

import static com.ilovesshan.ximalaya.config.Constants.HOT_SEARCH_DETAIL_LIST_SIZE;
import static com.ilovesshan.ximalaya.config.Constants.SEARCH_DETAIL_LIST_SIZE;

import com.ilovesshan.ximalaya.interfaces.ISearchApi;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public class SearchApi implements ISearchApi {

    //TODO 代码待优化
    private SearchApi() {
    }

    private static SearchApi mRecommendApi = null;

    public static SearchApi getInstance() {
        if (mRecommendApi == null) {
            synchronized (RecommendApi.class) {
                if (mRecommendApi == null) {
                    mRecommendApi = new SearchApi();
                }
            }
        }
        return mRecommendApi;
    }

    // 搜索专辑
    @Override
    public void getSearch(String keyWord, int page, IDataCallBack<SearchAlbumList> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyWord);
        // CATEGORY_ID 分类ID，不填或者为0检索全库
        map.put(DTransferConstants.CATEGORY_ID, 0 + "");
        // 排序条件：2-最新，3-最多播放，4-最相关（默认）
        map.put(DTransferConstants.CALC_DIMENSION, 3 + "");
        // 返回第几页，必须大于等于1，不填默认为1
        map.put(DTransferConstants.PAGE, page + "");
        // 每页多少条，默认20，最多不超过200
        map.put(DTransferConstants.LIKE_COUNT, SEARCH_DETAIL_LIST_SIZE + "");
        CommonRequest.getSearchedAlbums(map, callback);
    }


    // 获取最新热搜词
    @Override
    public void getHotWords(IDataCallBack<HotWordList> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.TOP, HOT_SEARCH_DETAIL_LIST_SIZE + "");
        CommonRequest.getHotWords(map, callback);
    }


    // 获取某个关键词的联想词
    @Override
    public void getSuggestWord(String keyWord, IDataCallBack<SuggestWords> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyWord);
        CommonRequest.getSuggestWord(map, callback);
    }
}
