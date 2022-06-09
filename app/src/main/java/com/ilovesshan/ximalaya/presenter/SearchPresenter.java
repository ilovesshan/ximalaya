package com.ilovesshan.ximalaya.presenter;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.api.SearchApi;
import com.ilovesshan.ximalaya.interfaces.ISearch;
import com.ilovesshan.ximalaya.interfaces.ISearchViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public class SearchPresenter implements ISearch {
    private static final String TAG = "SearchPresenter";

    private SearchPresenter() {
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getInstance() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }

    ArrayList<ISearchViewController> mISearchViewControllers = new ArrayList<>();


    // 根据关键字搜索专辑
    @Override
    public void search(String keyWord) {
        SearchApi searchApi = SearchApi.getInstance();
        searchApi.getSearch(keyWord, 1, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList list) {
                if (list != null) {
                    LogUtil.d(TAG, "onSuccess", "list.getAlbums()==" + list.getAlbums());

                    for (ISearchViewController iSearchViewController : mISearchViewControllers) {
                        iSearchViewController.noSearchResultLoaded(list.getAlbums());
                    }

                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError", "根据关键字搜索专辑 获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
            }
        });
    }

    // 获取最新热搜词
    @Override
    public void requestHotWords() {

        SearchApi searchApi = SearchApi.getInstance();
        searchApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(@Nullable HotWordList hotWordList) {
                if (hotWordList != null) {
                    LogUtil.d(TAG, "onSuccess", "hotWordList.getHotWordList()==" + hotWordList.getHotWordList());

                    for (ISearchViewController iSearchViewController : mISearchViewControllers) {
                        iSearchViewController.ontHotWordsResultLoaded(hotWordList.getHotWordList());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError", "获取最新热搜词 获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
            }
        });

    }

    // 获取某个关键词的联想词
    @Override
    public void requestSuggestWord(String keyWord) {
        SearchApi searchApi = SearchApi.getInstance();
        searchApi.getSuggestWord(keyWord, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(@Nullable SuggestWords suggestWords) {

                if (suggestWords != null) {
                    LogUtil.d(TAG, "onSuccess", "suggestWords.getKeyWordList() ==" + suggestWords.getKeyWordList());

                    for (ISearchViewController iSearchViewController : mISearchViewControllers) {
                        iSearchViewController.ontSuggestWordResultLoaded(suggestWords.getKeyWordList());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError", "获取某个关键词的联想词 获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
            }
        });
    }


    @Override
    public void registerViewController(ISearchViewController iSearchViewController) {
        if (!mISearchViewControllers.contains(iSearchViewController)) {
            mISearchViewControllers.add(iSearchViewController);
        }
    }

    @Override
    public void unRegisterViewController(ISearchViewController iSearchViewController) {
        mISearchViewControllers.remove(iSearchViewController);
    }
}
