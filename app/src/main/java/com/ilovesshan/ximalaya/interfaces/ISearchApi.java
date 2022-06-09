package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public interface ISearchApi {


    /**
     * 搜索专辑
     *
     * @param keyWord  关键字
     * @param page     页码
     * @param callback 回调函数
     */
    void getSearch(String keyWord, int page, IDataCallBack<SearchAlbumList> callback);


    /**
     * 获取最新热搜词
     *
     * @param callback 回调函数
     */
    void getHotWords(IDataCallBack<HotWordList> callback);


    /**
     * 获取某个关键词的联想词
     *
     * @param keyWord  关键字
     * @param callback 回调函数
     */
    void getSuggestWord(String keyWord, IDataCallBack<SuggestWords> callback);


}
