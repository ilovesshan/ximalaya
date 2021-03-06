package com.ilovesshan.ximalaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.adapter.TabIndicatorAdapter;
import com.ilovesshan.ximalaya.adapter.TabViewPagerAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ilovesshan.ximalaya.interfaces.IRecommendViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ilovesshan.ximalaya.presenter.RecommendPresenter;
import com.ilovesshan.ximalaya.utils.ViewUtils;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPlayerViewController, IAlbumDetailViewController, IRecommendViewController {
    private static final String TAG = "MainActivity";

    private MagicIndicator mTabIndicator;
    private ViewPager mTabViewPager;
    private ImageView mIvMainPlayerAlbumIcon;
    private TextView mTvMainPlayerAlbumTitle;
    private TextView mTvMainPlayerAlbumAuthor;
    private ImageView mIvMainPlayerPlayList;
    private ImageView mIvMainPlayerPlayOrPause;
    private LinearLayout mLlMainControllerBar;


    private String[] mTabIndicators = Constants.TAB_INDICATORS;
    private PlayerPresenter mPlayerPresenter;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private List<Album> mAlbumList = new ArrayList<>();
    private RecommendPresenter mRecommendPresenter;
    private RelativeLayout mRlSearchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ?????????view
        initView();

        // ????????????
        bindEvent();

    }

    /**
     * ?????????view ????????????
     */
    private void initView() {
        mTabIndicator = findViewById(R.id.tab_indicator);
        mTabViewPager = findViewById(R.id.tab_view_pager);
        mIvMainPlayerAlbumIcon = findViewById(R.id.iv_main_player_album_icon);
        mTvMainPlayerAlbumTitle = findViewById(R.id.tv_main_player_album_title);
        mTvMainPlayerAlbumAuthor = findViewById(R.id.tv_main_player_album_author);
        mIvMainPlayerPlayList = findViewById(R.id.iv_main_player_play_list);
        mIvMainPlayerPlayOrPause = findViewById(R.id.iv_main_player_play_or_pause);
        mLlMainControllerBar = findViewById(R.id.ll_main_controller_bar);
        mRlSearchIcon = findViewById(R.id.rl_search_icon);

    }


    /**
     * ???????????? ????????????
     */
    private void bindEvent() {
        mTvMainPlayerAlbumTitle.requestFocus();

        // ???????????????????????????????????????
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setBackgroundColor(getResources().getColor(R.color.app_primary));

        // indicator???????????????
        commonNavigator.setAdjustMode(true);
        TabIndicatorAdapter tabIndicatorAdapter = new TabIndicatorAdapter(Arrays.asList(mTabIndicators));
        commonNavigator.setAdapter(tabIndicatorAdapter);
        // TabIndicator ??????????????????
        tabIndicatorAdapter.setOnTabClickListener((index -> mTabViewPager.setCurrentItem(index)));

        // ???viewPager???????????????
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        mTabViewPager.setAdapter(tabViewPagerAdapter);

        // indicator???????????????
        mTabIndicator.setNavigator(commonNavigator);
        // ??????indicator???viewPager
        ViewPagerHelper.bind(mTabIndicator, mTabViewPager);

        // ??????PlayerPresenter ??????????????????
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewController(this);

        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewController(this);

        mRecommendPresenter = RecommendPresenter.getInstance();
        mRecommendPresenter.registerViewController(this);

        // ???????????????????????????
        mIvMainPlayerPlayOrPause.setOnClickListener(v -> {
            if (mPlayerPresenter.hasPlayList()) {
                // ????????????????????????
                if (mPlayerPresenter.isPlaying()) {
                    mPlayerPresenter.pause();
                } else {
                    mPlayerPresenter.play();
                }
            } else {
                // ????????????????????? ???????????????????????????????????????ID ????????????????????????
                if (mAlbumList != null && mAlbumList.size() > 0) {
                    mAlbumDetailPresenter.requestRecommendListById(mAlbumList.get(0).getId());
                } else {
                    ToastUtils.show("????????????");
                }
            }
        });

        // ??????????????????????????? ??????????????????????????????
        mLlMainControllerBar.setOnClickListener(v -> {
            if (!mPlayerPresenter.hasPlayList()) {
                mAlbumDetailPresenter.requestRecommendListById(mAlbumList.get(0).getId());
            }
            BaseApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, PlayerActivity.class));
                }
            }, 500);
        });

        // ???????????????????????????
        mRlSearchIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewController(this);
            mPlayerPresenter = null;

        }
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.unRegisterViewController(this);
            mAlbumDetailPresenter = null;
        }
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewController(this);
            mAlbumDetailPresenter = null;
        }
    }

    @Override
    public void onPlayStart(Track track) {
        if (track != null) {
            mTvMainPlayerAlbumTitle.setText(track.getTrackTitle());
            mTvMainPlayerAlbumAuthor.setText(track.getAnnouncer().getNickname());
            RequestOptions borderRadius = ViewUtils.makeBorderRadius(10);
            Glide.with(this).load(track.getCoverUrlSmall()).apply(borderRadius).into(mIvMainPlayerAlbumIcon);
            mIvMainPlayerPlayOrPause.setBackgroundResource(R.drawable.player_pause);
        }
    }

    @Override
    public void onPlayPause() {
        mIvMainPlayerPlayOrPause.setBackgroundResource(R.drawable.player_play);
    }

    @Override
    public void onPlayStop() {
        mIvMainPlayerPlayOrPause.setBackgroundResource(R.drawable.player_play);
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

    }

    @Override
    public void onLoadedDetailList(List<Track> trackList) {

    }

    @Override
    public void onLoadedDetail(Album album) {

    }

    @Override
    public void onDataLoaded(List<Album> albumList) {
        this.mAlbumList = albumList;
    }

    @Override
    public void onLoadError() {

    }

    @Override
    public void onLoadEmpty() {

    }

    @Override
    public void onLoading() {

    }
}