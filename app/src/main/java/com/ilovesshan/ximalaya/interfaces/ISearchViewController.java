package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public interface ISearchViewController {

    void onSearchResultLoaded(List<Album> albums);

    void ontHotWordsResultLoaded(List<HotWord> hotWordList);

    void ontSuggestWordResultLoaded(List<QueryResult> keyWordList);

}
