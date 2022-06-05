package com.ilovesshan.ximalaya.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.TrackListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.utils.NumberUtils;
import com.ilovesshan.ximalaya.utils.ViewUtils;
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
public class AlbumDetailActivity extends AppCompatActivity implements IAlbumDetailViewController, IPlayerViewController {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        // 设置透明状态栏
        ViewUtils.makeStatusBarTransparent(AlbumDetailActivity.this);

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
     * 绑定事件 处理函数
     */
    private void bindEvent() {

        // 占位的坑 方便填充数据 (加载中/成功/失败/为空...)
        FrameLayout mRcvAlbumDetailListContainer = findViewById(R.id.rcv_album_detail_list_container);

        // 创建UILoader
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View createSuccessView() {
                    View viewItem = LayoutInflater.from(AlbumDetailActivity.this).inflate(R.layout.activity_album_detail_list, mRcvAlbumDetailListContainer, false);
                    mRefreshLayout = viewItem.findViewById(R.id.refreshLayout);
                    mRcvAlbumDetailList = viewItem.findViewById(R.id.rcv_album_detail_list);

                    mIvBack.setOnClickListener(v -> finish());
                    //TODO 实现 recommend详情中 分享按钮点击逻辑
                    mIvShare.setOnClickListener(v -> ToastUtils.show("功能正在开发中..."));

                    //TODO 实现 recommend详情中 更多按钮点击逻辑
                    mIvMore.setOnClickListener(v -> ToastUtils.show("功能正在开发中..."));

                    //TODO 实现 recommend详情中 订阅按钮点击逻辑
                    mBtnSubscription.setOnClickListener(v -> ToastUtils.show("功能正在开发中..."));

                    // 创建和设置适配器
                    mTrackListAdapter = new TrackListAdapter();
                    mRcvAlbumDetailList.setLayoutManager(new LinearLayoutManager(AlbumDetailActivity.this));
                    mRcvAlbumDetailList.setAdapter(mTrackListAdapter);

                    //TODO 实现 recommend详情列表中 点击按钮下载功能
                    mTrackListAdapter.setOnDownloadClickListener((index, track) -> {
                        ToastUtils.show("功能正在开发中...");
                    });
                    mTrackListAdapter.setOnItemClickListener((tracks, index) -> {
                        // 给PlayerPresenter 设置播放列表
                        PlayerPresenter.getInstance().setPlayList(tracks, index);
                        startActivity(new Intent(AlbumDetailActivity.this, PlayerActivity.class));
                    });

                    // 设置 RefreshLayout 头部和脚部刷新风格
                    mRefreshLayout.setRefreshHeader(new ClassicsHeader(AlbumDetailActivity.this));
                    mRefreshLayout.setRefreshFooter(new ClassicsFooter(AlbumDetailActivity.this));
                    // 设置下拉刷新和上拉加载 回调接口
                    mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                        // 下拉刷新
                        @Override
                        public void onRefresh(RefreshLayout refreshlayout) {
                            BaseApplication.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show("下拉刷新成功~");
                                    refreshlayout.finishRefresh();//传入false表示刷新失败
                                }
                            }, 2000);
                        }
                    });

                    mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                        // 上拉加载
                        @Override
                        public void onLoadMore(RefreshLayout refreshlayout) {
                            BaseApplication.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show("上拉加载成功~");
                                    refreshlayout.finishLoadMore();//传入false表示加载失败
                                }
                            }, 2000);
                        }
                    });
                    return viewItem;
                }
            };

            // 发生错误时间、点击加载重试
            mUiLoader.setOnRetryLoadClickListener(new UILoader.OnRetryLoadClickListener() {
                @Override
                public void onRetry() {
                    if (mAlbumDetailPresenter != null) {
                        mAlbumDetailPresenter.loadDetailListData(mAlbumId + "", mPageNum + "", "asc");
                    }
                }
            });

            mRcvAlbumDetailListContainer.removeAllViews();
            mRcvAlbumDetailListContainer.addView(mUiLoader);
        }


        // 获取逻辑层控制器和注册监听
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mPlayerPresenter = PlayerPresenter.getInstance();

        mAlbumDetailPresenter.registerViewController(this);
        mPlayerPresenter.registerViewController(this);


        // 播放与暂停控制
        mLlAlbumDetailCheckPlayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        LogUtil.d(TAG, "onLoadedDetail", "album = " + album);
        mAlbumId = album.getId();
        // 根据专辑ID获取专辑下的声音列表
        mAlbumDetailPresenter.loadDetailListData(album.getId() + "", mPageNum + "", "asc");
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.LOADING);
        }
        // 图片圆角
        RequestOptions borderRadius = ViewUtils.makeBorderRadius(30);
        // 高斯模糊背景
        RequestOptions blurTransform = ViewUtils.blurTransformation(25, 3);

        Glide.with(mIvBigCover).load(album.getCoverUrlSmall()).apply(blurTransform).into(mIvBigCover);
        Glide.with(mIvSmallCover).load(album.getCoverUrlSmall()).apply(borderRadius).into(mIvSmallCover);
        mTvAlbumTitle.setText(album.getAlbumTitle());
        Glide.with(mIvAlbumAuthorAvatar).load(album.getAnnouncer().getAvatarUrl()).apply(borderRadius).into(mIvAlbumAuthorAvatar);
        mIvAlbumAuthorName.setText(album.getAnnouncer().getNickname());
        mTvScoreValue.setText(album.getAlbumScore());
        mTvPlayAmountValue.setText(NumberUtils.number2CountingUnit(album.getPlayCount()));
        mTvSubscriptionAmountValue.setText(NumberUtils.number2CountingUnit(album.getSubscribeCount()));

    }

    // 加载失败回调
    @Override
    public void onLoadError() {
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.ERROR);
        }
    }

    // 加载数据为空回调
    @Override
    public void onLoadEmpty() {
        if (mUiLoader != null) {
            mUiLoader.updateUILoaderState(UILoader.UILoaderState.EMPTY);
        }
    }

    // 加载中回调
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
            mPlayerPresenter.unRegisterViewController(this);
        }
    }

    private void setPlayModeTextAndIcon(boolean isPlay) {
        mIvAlbumDetailPlayIcon.setBackgroundResource(isPlay ? R.drawable.pause : R.drawable.play);
        mTvAlbumDetailPlayText.setText(isPlay ? "暂停" : "播放");
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

    }

    @Override
    public void onInitPlayMode(XmPlayListControl.PlayMode playMode) {
        if (mPlayerPresenter != null) {
            setPlayModeTextAndIcon(mPlayerPresenter.isPlaying());
        }
    }
}