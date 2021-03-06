package com.ilovesshan.ximalaya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.adapter.TrackListAdapter;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ilovesshan.ximalaya.interfaces.ISubscriptionViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ilovesshan.ximalaya.presenter.SubscriptionPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.utils.NumberUtils;
import com.ilovesshan.ximalaya.utils.ViewUtils;
import com.ilovesshan.ximalaya.views.UILoader;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class AlbumDetailActivity extends AppCompatActivity implements IAlbumDetailViewController, IPlayerViewController, ISubscriptionViewController {
    private static final String TAG = "AlbumDetailActivity";
    private ImageView mIvBack;
    private ImageView mIvShare;
    private ImageView mIvMore;

    private ImageView mIvBigCover;
    private ImageView mIvSmallCover;
    private TextView mTvAlbumTitle;
    private ImageView mIvAlbumAuthorAvatar;
    private TextView mIvAlbumAuthorName;
    private TextView mTvScoreValue;
    private TextView mTvPlayAmountValue;
    private TextView mTvSubscriptionAmountValue;
    private TextView mBtnSubscription;
    private RecyclerView mRcvAlbumDetailList;
    private ImageView mIvAlbumDetailPlayIcon;
    private TextView mTvAlbumDetailPlayText;
    private LinearLayout mLlAlbumDetailCheckPlayMode;

    private AlbumDetailPresenter mAlbumDetailPresenter;
    private TrackListAdapter mTrackListAdapter;

    private UILoader mUiLoader = null;

    private int mPageNum = 1;
    private long mAlbumId = -1;
    private PlayerPresenter mPlayerPresenter;
    private List<Track> mTrack = new ArrayList<>();
    private RefreshLayout mRefreshLayout;
    private String mAlbumName = "";
    private SubscriptionPresenter mSubscriptionPresenter;
    private Album mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        // ?????????????????????
        ViewUtils.makeStatusBarTransparent(AlbumDetailActivity.this);

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
        mIvShare = findViewById(R.id.iv_share);
        mIvMore = findViewById(R.id.iv_more);
        mIvBigCover = findViewById(R.id.iv_big_cover);
        mIvSmallCover = findViewById(R.id.iv_small_cover);
        mTvAlbumTitle = findViewById(R.id.tv_album_title);
        mIvAlbumAuthorAvatar = findViewById(R.id.iv_album_author_avatar);
        mIvAlbumAuthorName = findViewById(R.id.iv_album_author_name);
        mTvScoreValue = findViewById(R.id.tv_score_value);
        mTvPlayAmountValue = findViewById(R.id.tv_play_amount_value);
        mTvSubscriptionAmountValue = findViewById(R.id.tv_subscription_amount_value);
        mBtnSubscription = findViewById(R.id.btn_subscription);
        mLlAlbumDetailCheckPlayMode = findViewById(R.id.ll_album_detail_check_play_mode);
        mIvAlbumDetailPlayIcon = findViewById(R.id.iv_album_detail_play_icon);
        mTvAlbumDetailPlayText = findViewById(R.id.tv_album_detail_play_text);

    }


    /**
     * ???????????? ????????????
     */
    private void bindEvent() {

        // ???????????? ?????????????????? (?????????/??????/??????/??????...)
        FrameLayout mRcvAlbumDetailListContainer = findViewById(R.id.rcv_album_detail_list_container);

        // ?????????????????????
        mTvAlbumDetailPlayText.requestFocus();

        // ??????UILoader
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View createSuccessView() {
                    View viewItem = LayoutInflater.from(AlbumDetailActivity.this).inflate(R.layout.activity_album_detail_list, mRcvAlbumDetailListContainer, false);
                    mRefreshLayout = viewItem.findViewById(R.id.refreshLayout);
                    mRcvAlbumDetailList = viewItem.findViewById(R.id.rcv_album_detail_list);

                    mIvBack.setOnClickListener(v -> finish());
                    //TODO ?????? recommend????????? ????????????????????????
                    mIvShare.setOnClickListener(v -> ToastUtils.show("?????????????????????..."));

                    //TODO ?????? recommend????????? ????????????????????????
                    mIvMore.setOnClickListener(v -> ToastUtils.show("?????????????????????..."));

                    //recommend????????? ????????????????????????????????????
                    mBtnSubscription.setOnClickListener(v -> {
                        boolean isSubscription = mSubscriptionPresenter.isSubscription((int) mAlbum.getId());

                        if (isSubscription) {
                            // ????????????
                            if (mSubscriptionPresenter != null) {
                                mSubscriptionPresenter.deleteSubscription((int) mAlbum.getId());
                            }
                        } else {
                            // ????????????
                            mSubscriptionPresenter.addSubscription(mAlbum);
                        }
                    });

                    // ????????????????????????
                    mTrackListAdapter = new TrackListAdapter();
                    mRcvAlbumDetailList.setLayoutManager(new LinearLayoutManager(AlbumDetailActivity.this));
                    mRcvAlbumDetailList.setAdapter(mTrackListAdapter);

                    //TODO ?????? recommend??????????????? ????????????????????????
                    mTrackListAdapter.setOnDownloadClickListener((index, track) -> {
                        ToastUtils.show("?????????????????????...");
                    });
                    mTrackListAdapter.setOnItemClickListener((tracks, index) -> {
                        // ???PlayerPresenter ??????????????????
                        PlayerPresenter.getInstance().setPlayList(tracks, index);
                        startActivity(new Intent(AlbumDetailActivity.this, PlayerActivity.class));
                    });

                    // ?????? RefreshLayout ???????????????????????????
                    mRefreshLayout.setRefreshHeader(new ClassicsHeader(AlbumDetailActivity.this));
                    mRefreshLayout.setRefreshFooter(new ClassicsFooter(AlbumDetailActivity.this));
                    // ????????????????????????????????? ????????????
                    mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                        // ????????????
                        @Override
                        public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                            if (mAlbumDetailPresenter != null) {
                                mAlbumDetailPresenter.refresh(refreshlayout);
                            }
                        }
                    });

                    mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                        // ????????????
                        @Override
                        public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                            if (mAlbumDetailPresenter != null) {
                                mAlbumDetailPresenter.loadMore(refreshlayout);
                            }
                        }
                    });
                    return viewItem;
                }
            };

            // ???????????????????????????????????????
            mUiLoader.setOnRetryLoadClickListener(new UILoader.OnRetryLoadClickListener() {
                @Override
                public void onRetry() {
                    if (mAlbumDetailPresenter != null) {
                        mAlbumDetailPresenter.loadDetailListData((int) mAlbumId, mPageNum);
                    }
                }
            });

            mRcvAlbumDetailListContainer.removeAllViews();
            mRcvAlbumDetailListContainer.addView(mUiLoader);
        }


        // ???????????????????????????????????????
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mPlayerPresenter = PlayerPresenter.getInstance();
        mSubscriptionPresenter = SubscriptionPresenter.getInstance();

        mAlbumDetailPresenter.registerViewController(this);
        mPlayerPresenter.registerViewController(this);
        mSubscriptionPresenter.registerViewController(this);

        // ????????????????????????????????????????????? ???????????????
        mSubscriptionPresenter.querySubscriptionList();
        // ??????????????? ?????????????????????
        boolean isSubscription = mSubscriptionPresenter.isSubscription((int) mAlbum.getId());
        mBtnSubscription.setText(isSubscription ? "????????????" : "+ ??????");

        // ?????????????????????
        mLlAlbumDetailCheckPlayMode.setOnClickListener(v -> {
            if (mPlayerPresenter != null) {
                if (mPlayerPresenter.hasPlayList()) {
                    if (mPlayerPresenter.isPlaying()) {
                        mPlayerPresenter.pause();
                    } else {
                        mPlayerPresenter.play();
                    }
                } else {
                    mPlayerPresenter.setPlayList(mTrack, 0);
                }
            }
        });
    }

    @Override
    public void onLoadedDetailList(List<Track> tracks) {
        this.mTrack = tracks;
        mTrackListAdapter.setDat(tracks);
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.SUCCESS);
        }
    }

    @Override
    public void onLoadedDetail(Album album) {
        this.mAlbum = album;

        if (this.mAlbumName == null || this.mAlbumName.length() == 0) {
            this.mAlbumName = album.getAlbumTitle();
        }
        LogUtil.d(TAG, "onLoadedDetail", "album = " + album);
        mAlbumId = album.getId();
        // ????????????ID??????????????????????????????
        mAlbumDetailPresenter.loadDetailListData((int) mAlbumId, mPageNum);
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.LOADING);
        }
        // ????????????
        RequestOptions borderRadius = ViewUtils.makeBorderRadius(30);
        // ??????????????????
        RequestOptions blurTransform = ViewUtils.blurTransformation(25, 3);

        Glide.with(mIvBigCover).load(album.getCoverUrlSmall()).apply(blurTransform).into(mIvBigCover);
        Glide.with(mIvSmallCover).load(album.getCoverUrlSmall()).apply(borderRadius).into(mIvSmallCover);
        mTvAlbumTitle.setText(album.getAlbumTitle());
        Glide.with(mIvAlbumAuthorAvatar).load(album.getAnnouncer().getAvatarUrl()).apply(borderRadius).into(mIvAlbumAuthorAvatar);
        mIvAlbumAuthorName.setText(album.getAnnouncer().getNickname());
        mTvScoreValue.setText(album.getAlbumScore() == null ? "0.0" : album.getAlbumScore());
        mTvPlayAmountValue.setText(NumberUtils.number2CountingUnit(album.getPlayCount()));
        mTvSubscriptionAmountValue.setText(NumberUtils.number2CountingUnit(album.getSubscribeCount()));

    }

    // ??????????????????
    @Override
    public void onLoadError() {
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.ERROR);
        }
    }

    // ????????????????????????
    @Override
    public void onLoadEmpty() {
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.EMPTY);
        }
    }

    // ???????????????
    @Override
    public void onLoading() {
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.LOADING);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.unRegisterViewController(this);
        }

        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewController(this);
        }

        if (mSubscriptionPresenter != null) {
            mSubscriptionPresenter.unRegisterViewController(this);
        }
    }

    private void setPlayModeTextAndIcon(boolean isPlay) {
        mIvAlbumDetailPlayIcon.setBackgroundResource(isPlay ? R.drawable.pause : R.drawable.play);
        if (isPlay) {
            mTvAlbumDetailPlayText.setText(mAlbumName);
        } else {
            mTvAlbumDetailPlayText.setText("????????????");
        }
    }


    @Override
    public void onPlayStart(Track track) {
        setPlayModeTextAndIcon(true);
    }

    @Override
    public void onPlayPause() {
        setPlayModeTextAndIcon(false);
    }

    @Override
    public void onPlayStop() {
        setPlayModeTextAndIcon(false);
    }

    @Override
    public void onSoundPlayComplete() {

    }

    @Override
    public void onPlayPrev(Track track) {

    }

    @Override
    public void onPlayNext(Track track) {

    }

    @Override
    public void onLoadedPlayList(List<Track> tracks) {

    }

    @Override
    public void onPlayError(XmPlayerException exception) {

    }

    @Override
    public void onStartPlayAds(Advertis ad, int position) {

    }

    @Override
    public void onPlayAdsError(int what, int extra) {

    }

    @Override
    public void onCompletePlayAds() {

    }

    @Override
    public void onPlayProgressUpdate(int currPosition, int totalDuration) {

    }

    @Override
    public void onPlayModeUpdate(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onTrackUpdate(Track track, int position) {
        this.mAlbumName = track.getTrackTitle();
    }

    @Override
    public void onInitPlayMode(XmPlayListControl.PlayMode playMode) {
        if (mPlayerPresenter != null) {
            setPlayModeTextAndIcon(mPlayerPresenter.isPlaying());
        }
    }

    @Override
    public void onAddSubscriptionResult(boolean isSuccess) {
        LogUtil.d(TAG, "OnAddSubscriptionResult", "????????????" + isSuccess);
        if (isSuccess) {
            ToastUtils.show("????????????");
            mBtnSubscription.setText("????????????");
        }
    }

    @Override
    public void onDeleteSubscriptionResult(boolean isSuccess) {
        LogUtil.d(TAG, "OnAddSubscriptionResult", "??????????????????" + isSuccess);
        if (isSuccess) {
            ToastUtils.show("????????????");
            mBtnSubscription.setText("+ ??????");
        }
    }


    @Override
    public void onSubscriptionListLoaded(List<Album> albumList) {

    }
}