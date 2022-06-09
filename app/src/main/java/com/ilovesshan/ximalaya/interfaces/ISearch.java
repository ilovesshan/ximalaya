package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public interface ISearch extends IBasePresenter<ISearchViewController> {

    /**
     * 搜索
     *
     * @param keyWord 关键字
     */

    void search(String keyWord);


    /**
     * 获取最新热搜词
     */
    void requestHotWords();


    /**
     * 获取某个关键词的联想词
     *
     * @param keyWord 关键字
     */

    void requestSuggestWord(String keyWord);


}
