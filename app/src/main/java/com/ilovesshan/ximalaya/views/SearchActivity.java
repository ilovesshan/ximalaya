package com.ilovesshan.ximalaya.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.AlbumListAdapter;
import com.ilovesshan.ximalaya.adapter.SearchRecommendWordListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.interfaces.ISearchViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.SearchPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
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
    private AlbumListAdapter mAlbumListAdapter;
    private LinearLayout mLlContainer;
    private ImageView mIvSearchDelete;
    private RecyclerView mRcvSearchRecommendWordList;


    private UILoader mUiLoader;
    private String mKeyWord = "";
    private SearchRecommendWordListAdapter mSearchRecommendWordListAdapter;
    private AlbumDetailPresenter mAlbumDetailPresenter;

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
        mLlContainer = findViewById(R.id.ll_container);
        mIvSearchDelete = findViewById(R.id.iv_search_delete);

        // 使用 UILoader
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View createSuccessView() {
                    View view = FrameLayout.inflate(SearchActivity.this, R.layout.item_search_result_list, null);
                    mRcvSearchResultList = view.findViewById(R.id.rcv_search_result_list);
                    mRcvSearchRecommendWordList = view.findViewById(R.id.rcv_search_recommend_list);

                    // 搜索结果
                    mRcvSearchResultList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    mAlbumListAdapter = new AlbumListAdapter();
                    mRcvSearchResultList.setAdapter(mAlbumListAdapter);

                    // 点击跳转到 专辑详情界面
                    mAlbumListAdapter.setOnItemClickListener(new AlbumListAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int index, Album album) {
                            mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
                            mAlbumDetailPresenter.setAlbum(album);
                            startActivity(new Intent(SearchActivity.this, AlbumDetailActivity.class));
                            LogUtil.d(TAG, "onClick", "index = " + index + "album =" + album);
                        }
                    });

                    // 搜索联想关键字
                    mRcvSearchRecommendWordList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    mSearchRecommendWordListAdapter = new SearchRecommendWordListAdapter();
                    mRcvSearchRecommendWordList.setAdapter(mSearchRecommendWordListAdapter);


                    mSearchRecommendWordListAdapter.setOnRecommendHotWordItemClick(hotWord -> {
                        ToastUtils.show(hotWord);
                        searchHandler(hotWord);
                    });

                    return view;
                }
            };
            if (mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            }
            mLlContainer.addView(mUiLoader);
        }

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
                    // 隐藏 搜索列表 / 联想热词列表
                    mRcvSearchResultList.setVisibility(View.GONE);
                    mRcvSearchRecommendWordList.setVisibility(View.GONE);

                    // 隐藏 删除Icon
                    mIvSearchDelete.setVisibility(View.GONE);

                } else {
                    // 显示 删除Icon
                    mIvSearchDelete.setVisibility(View.VISIBLE);

                    // 模糊匹配关键字
                    mSearchPresenter.requestSuggestWord(s.toString());

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

        // 出错了 再次加载
        mUiLoader.setOnRetryLoadClickListener(() -> searchHandler(this.mKeyWord));

        // 清输入框内容
        mIvSearchDelete.setOnClickListener(v -> mEtSearchInput.setText(""));
    }

    private void searchHandler(String keyWord) {
        this.mKeyWord = keyWord;
        if (TextUtils.isEmpty(keyWord)) {
            ToastUtils.show("请输入搜索关键字");
            return;
        }
        if (mRecommendHotWordView.getVisibility() != View.GONE) {
            mRecommendHotWordView.setVisibility(View.GONE);
        }

        // 关闭键盘
        visibleSoftInputFromWindow(false);
        mSearchPresenter.search(keyWord);
        mUiLoader.updateUILoaderState(UILoader.UILoaderState.LOADING);
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
        mRecommendHotWordView.setVisibility(View.GONE);
        mRcvSearchRecommendWordList.setVisibility(View.GONE);
        mRcvSearchResultList.setVisibility(View.VISIBLE);

        if (album.size() == 0) {
            if (mUiLoader != null) {
                mUiLoader.updateUILoaderState(UILoader.UILoaderState.EMPTY);
            }
        } else {
            if (mUiLoader != null) {
                mUiLoader.updateUILoaderState(UILoader.UILoaderState.SUCCESS);
                mAlbumListAdapter.setData(album);
            }
        }
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
        mRecommendHotWordView.setVisibility(View.GONE);
        mRcvSearchResultList.setVisibility(View.GONE);
        mRcvSearchRecommendWordList.setVisibility(View.VISIBLE);

        mSearchRecommendWordListAdapter.setData(keyWordList);
        LogUtil.d(TAG, "ontSuggestWordResultLoaded", "keyWordList === " + keyWordList);
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.ERROR);
        }
    }
}