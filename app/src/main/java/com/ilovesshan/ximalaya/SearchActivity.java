package com.ilovesshan.ximalaya;

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
import com.ilovesshan.ximalaya.adapter.AlbumListAdapter;
import com.ilovesshan.ximalaya.adapter.SearchRecommendWordListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.interfaces.ISearchViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.HistoryPresenter;
import com.ilovesshan.ximalaya.presenter.SearchPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.views.FlowTextLayout;
import com.ilovesshan.ximalaya.views.UILoader;
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
    private HistoryPresenter mHistoryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // ?????????view
        initView();

        // ????????????
        bindEvent();

    }


    /**
     * ?????????view ????????????
     */
    private void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mEtSearchInput = findViewById(R.id.et_search_input);
        mTvSearchBtn = findViewById(R.id.tv_search_btn);
        mRecommendHotWordView = findViewById(R.id.recommend_hot_word_view);
        mLlContainer = findViewById(R.id.ll_container);
        mIvSearchDelete = findViewById(R.id.iv_search_delete);

        // ?????? UILoader
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View createSuccessView() {
                    View view = FrameLayout.inflate(SearchActivity.this, R.layout.item_search_result_list, null);
                    mRcvSearchResultList = view.findViewById(R.id.rcv_search_result_list);
                    mRcvSearchRecommendWordList = view.findViewById(R.id.rcv_search_recommend_list);

                    // ????????????
                    mRcvSearchResultList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    mAlbumListAdapter = new AlbumListAdapter();
                    mRcvSearchResultList.setAdapter(mAlbumListAdapter);

                    // ??????????????? ??????????????????
                    mAlbumListAdapter.setOnItemClickListener(new AlbumListAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int index, Album album) {
                            // ????????????????????????
                            mHistoryPresenter = HistoryPresenter.getInstance();
                            mHistoryPresenter.addAlbum(album);

                            mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
                            mAlbumDetailPresenter.setAlbum(album);
                            startActivity(new Intent(SearchActivity.this, AlbumDetailActivity.class));
                            LogUtil.d(TAG, "onClick", "index = " + index + "album =" + album);
                        }
                    });

                    // ?????????????????????
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
     * ???????????? ????????????
     */
    private void bindEvent() {
        // ????????????
        visibleSoftInputFromWindow(true);

        // ?????? SearchPresenter
        mSearchPresenter = SearchPresenter.getInstance();
        mSearchPresenter.registerViewController(this);


        // ????????????
        mSearchPresenter.requestHotWords();

        // ?????????????????????
        mIvBack.setOnClickListener(v -> finish());

        // ????????????
        mTvSearchBtn.setOnClickListener(v -> {
            searchHandler(mEtSearchInput.getText().toString());
        });

        // ?????? ??????????????????
        mRecommendHotWordView.setClickListener(s -> {
            // ??????????????????
            mEtSearchInput.setText(s);
            mEtSearchInput.setSelection(s.length());
            searchHandler(s);
        });

        // ????????? ????????????
        mEtSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ?????? ??????????????????
                if (s.length() == 0) {
                    // ?????? ??????????????????
                    mRecommendHotWordView.setVisibility(View.VISIBLE);
                    // ?????? ???????????? / ??????????????????
                    mRcvSearchResultList.setVisibility(View.GONE);
                    mRcvSearchRecommendWordList.setVisibility(View.GONE);

                    // ?????? ??????Icon
                    mIvSearchDelete.setVisibility(View.GONE);

                } else {
                    // ?????? ??????Icon
                    mIvSearchDelete.setVisibility(View.VISIBLE);

                    // ?????????????????????
                    mSearchPresenter.requestSuggestWord(s.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // ????????? ????????????
        mEtSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchHandler(v.getText().toString());
            }
            return false;
        });

        // ????????? ????????????
        mUiLoader.setOnRetryLoadClickListener(() -> searchHandler(this.mKeyWord));

        // ??????????????????
        mIvSearchDelete.setOnClickListener(v -> mEtSearchInput.setText(""));
    }

    private void searchHandler(String keyWord) {
        this.mKeyWord = keyWord;
        if (TextUtils.isEmpty(keyWord)) {
            ToastUtils.show("????????????????????????");
            return;
        }
        if (mRecommendHotWordView.getVisibility() != View.GONE) {
            mRecommendHotWordView.setVisibility(View.GONE);
        }

        // ????????????
        visibleSoftInputFromWindow(false);
        mSearchPresenter.search(keyWord);
        mUiLoader.updateUILoaderState(UILoader.UILoaderState.LOADING);
    }


    /**
     * ????????????
     */

    void visibleSoftInputFromWindow(boolean visible) {
        InputMethodManager inputMethodManager = (InputMethodManager) (getSystemService(Context.INPUT_METHOD_SERVICE));
        BaseApplication.getHandler().postDelayed(() -> {
            if (inputMethodManager != null) {
                if (visible) {
                    // ??????
                    mEtSearchInput.requestFocus();
                    inputMethodManager.showSoftInput(mEtSearchInput, 0);
                } else {
                    // ??????
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