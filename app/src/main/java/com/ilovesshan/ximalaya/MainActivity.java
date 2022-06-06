package com.ilovesshan.ximalaya;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ilovesshan.ximalaya.adapter.TabIndicatorAdapter;
import com.ilovesshan.ximalaya.adapter.TabViewPagerAdapter;
import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ilovesshan.ximalaya.utils.ViewUtils;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPlayerViewController {
    private static final String TAG = "MainActivity";

    private MagicIndicator mTabIndicator;
    private ViewPager mTabViewPager;
    private ImageView mIvMainPlayerAlbumIcon;
    private TextView mTvMainPlayerAlbumTitle;
    private TextView mTvMainPlayerAlbumAuthor;
    private ImageView mIvMainPlayerPlayList;
    private ImageView mIvMainPlayerPlayOrPause;


    private String[] mTabIndicators = Constants.TAB_INDICATORS;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化view
        initView();

        // 绑定事件
        bindEvent();


    }

    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mTabIndicator = findViewById(R.id.tab_indicator);
        mTabViewPager = findViewById(R.id.tab_view_pager);
        mIvMainPlayerAlbumIcon = findViewById(R.id.iv_main_player_album_icon);
        mTvMainPlayerAlbumTitle = findViewById(R.id.tv_main_player_album_title);
        mTvMainPlayerAlbumAuthor = findViewById(R.id.tv_main_player_album_author);
        mIvMainPlayerPlayList = findViewById(R.id.iv_main_player_play_list);
        mIvMainPlayerPlayOrPause = findViewById(R.id.iv_main_player_play_or_pause);

    }


    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {
        mTvMainPlayerAlbumTitle.requestFocus();

        // 创建公共导航器和设置适配器
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setBackgroundColor(getResources().getColor(R.color.app_primary));

        // indicator宽度自适应
        commonNavigator.setAdjustMode(true);
        TabIndicatorAdapter tabIndicatorAdapter = new TabIndicatorAdapter(Arrays.asList(mTabIndicators));
        commonNavigator.setAdapter(tabIndicatorAdapter);
        // TabIndicator 被点击的回调
        tabIndicatorAdapter.setOnTabClickListener((index -> mTabViewPager.setCurrentItem(index)));

        // 给viewPager设置适配器
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        mTabViewPager.setAdapter(tabViewPagerAdapter);

        // indicator设置导航器
        mTabIndicator.setNavigator(commonNavigator);
        // 绑定indicator和viewPager
        ViewPagerHelper.bind(mTabIndicator, mTabViewPager);

        // 获取PlayerPresenter 并且注册回调
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewController(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewController(this);
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
}