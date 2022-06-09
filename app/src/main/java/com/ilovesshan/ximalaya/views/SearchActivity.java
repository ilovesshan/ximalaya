package com.ilovesshan.ximalaya.views;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.RecommendListAdapter;
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
    private RecyclerView mRcvSearchResultList;
    private RecommendListAdapter mRecommendListAdapter;


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
        mRcvSearchResultList = findViewById(R.id.rcv_search_result_list);
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

        mRcvSearchResultList.setLayoutManager(new LinearLayoutManager(this));
        mRecommendListAdapter = new RecommendListAdapter();
        mRcvSearchResultList.setAdapter(mRecommendListAdapter);

        // 获取热词
        mSearchPresenter.requestHotWords();

        // 返回上一个界面
        mIvBack.setOnClickListener(v -> finish());

        // 点击搜索
        mTvSearchBtn.setOnClickListener(v -> {
            searchHandler(mEtSearchInput.getText().toString());
        });

        // 点击 推荐热词搜索
        mRecommendHotWordView.setClickListener(s -> {
            // 设置光标位置
            mEtSearchInput.setText(s);
            mEtSearchInput.setSelection(s.length());
            searchHandler(s);
        });

        // 输入框 内容改变
        mEtSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 用户 清空了输入框
                if (s.length() == 0) {
                    // 展示 推荐热词列表
                    mRecommendHotWordView.setVisibility(View.VISIBLE);
                    // 隐藏 搜索列表
                    mRcvSearchResultList.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 输入框 回车事件
        mEtSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchHandler(v.getText().toString());
            }
            return false;
        });
    }

    private void searchHandler(String keyWord) {
        // 关闭键盘
        visibleSoftInputFromWindow(false);
        mSearchPresenter.search(keyWord);
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
    public void onSearchResultLoaded(List<Album> album) {

        if (mRecommendHotWordView.getVisibility() != View.GONE) {
            mRecommendHotWordView.setVisibility(View.GONE);
        }

        if (mRcvSearchResultList.getVisibility() != View.VISIBLE) {
            mRcvSearchResultList.setVisibility(View.VISIBLE);
        }

        mRecommendListAdapter.setData(album);
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