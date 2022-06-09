package com.ilovesshan.ximalaya.presenter;

import com.ilovesshan.ximalaya.interfaces.ISearch;
import com.ilovesshan.ximalaya.interfaces.ISearchViewController;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/9
 * @description:
 */
public class SearchPresenter implements ISearch {

    private   SearchPresenter(){}
    private  static  SearchPresenter sSearchPresenter = null;
    public  static SearchPresenter getInstance(){
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class){
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return  sSearchPresenter;
    }




    ArrayList<ISearchViewController> mISearchViewControllers = new ArrayList<>();

    @Override
    public void search(String keyWord) {

    }

    @Override
    public void requestHotWords() {

    }

    @Override
    public void requestSuggestWord(String keyWord) {

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
