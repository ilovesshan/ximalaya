package com.ilovesshan.ximalaya.views;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.interfaces.ISearchViewController;
import com.ilovesshan.ximalaya.presenter.SearchPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements ISearchViewController {
    private static final String TAG = "SearchActivity";

    private ImageView mIvBack;
    private EditText mEtSearchInput;
    private TextView mTvSearchBtn;
    private SearchPresenter mSearchPresenter;
    private FlowTextLayout mRecommendHotWordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // 初始化view
        initView();

        // 绑定事件
        bindEvent();

    }


    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mEtSearchInput = findViewById(R.id.et_search_input);
        mTvSearchBtn = findViewById(R.id.tv_search_btn);
        mRecommendHotWordView = findViewById(R.id.recommend_hot_word_view);

    }


    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {
        // 弹出键盘
        visibleSoftInputFromWindow(true);

        // 获取 SearchPresenter
        mSearchPresenter = SearchPresenter.getInstance();
        mSearchPresenter.registerViewController(this);


        // 获取热词
        mSearchPresenter.requestHotWords();

        // 返回上一个界面
        mIvBack.setOnClickListener(v -> finish());

        // 点击搜索
        mTvSearchBtn.setOnClickListener(v -> {
            // 关闭键盘
            visibleSoftInputFromWindow(false);
            ToastUtils.show("搜索");
        });

        // 点击 推荐热词搜索
        mRecommendHotWordView.setClickListener(s -> {
            ToastUtils.show("点击了" + s);
        });

    }


    /**
     * 谈起键盘
     */

    void visibleSoftInputFromWindow(boolean visible) {
        InputMethodManager inputMethodManager = (InputMethodManager) (getSystemService(Context.INPUT_METHOD_SERVICE));
        BaseApplication.getHandler().postDelayed(() -> {
            if (inputMethodManager != null) {
                if (visible) {
                    // 弹出
                    mEtSearchInput.requestFocus();
                    inputMethodManager.showSoftInput(mEtSearchInput, 0);
                } else {
                    // 隐藏
                    mEtSearchInput.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(mEtSearchInput.getWindowToken(), 0);
                }
            }
        }, 500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            mSearchPresenter.unRegisterViewController(this);
            mSearchPresenter = null;
        }
    }

    @Override
    public void noSearchResultLoaded(List<Album> albums) {

    }

    @Override
    public void ontHotWordsResultLoaded(List<HotWord> hotWordList) {
        List<String> list = new ArrayList<>();
        for (HotWord hotWord : hotWordList) {
            list.add(hotWord.getSearchword());
        }
        mRecommendHotWordView.setTextContents(list);
    }

    @Override
    public void ontSuggestWordResultLoaded(List<QueryResult> keyWordList) {

    }
}