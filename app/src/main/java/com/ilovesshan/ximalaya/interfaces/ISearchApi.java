package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public interface ISearchApi extends IBasePresenter<ISearchViewController> {


    /**
     * 搜索专辑
     *
     * @param params   请求参数
     * @param callback 回调函数
     */
    void getSearchedAlbums(Map<String, String> params, IDataCallBack<SearchAlbumList> callback);


    /**
     * 获取最新热搜词
     *
     * @param params   请求参数
     * @param callback 回调函数
     */
    void getHotWords(Map<String, String> params, IDataCallBack<HotWordList> callback);


    /**
     * 获取某个关键词的联想词
     *
     * @param params   请求参数
     * @param callback 回调函数
     */
    void getSuggestWord(Map<String, String> params, IDataCallBack<SuggestWords> callback);


}
