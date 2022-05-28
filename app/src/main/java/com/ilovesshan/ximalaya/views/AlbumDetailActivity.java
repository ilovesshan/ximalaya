package com.ilovesshan.ximalaya.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.TrackListAdapter;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.utils.NumberUtils;
import com.ilovesshan.ximalaya.utils.ViewUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

@SuppressLint("SetTextI18n")
public class AlbumDetailActivity extends AppCompatActivity implements IAlbumDetailViewController {
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


    private AlbumDetailPresenter mAlbumDetailPresenter;
    private TrackListAdapter mTrackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        // 设置透明状态栏
        ViewUtils.makeStatusBarTransparent(AlbumDetailActivity.this);

        // 初始化view和绑定事件
        initViewAndBindEvent();

    }


    /**
     * 初始化view和绑定事件 处理函数
     */
    private void initViewAndBindEvent() {
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
        mRcvAlbumDetailList = findViewById(R.id.rcv_album_detail_list);

        mIvBack.setOnClickListener(v -> finish());

        //TODO 实现 recommend详情中 分享按钮点击逻辑
        mIvShare.setOnClickListener(v -> ToastUtils.show("功能正在开发中..."));

        //TODO 实现 recommend详情中 更多按钮点击逻辑
        mIvMore.setOnClickListener(v -> ToastUtils.show("功能正在开发中..."));

        //TODO 实现 recommend详情中 订阅按钮点击逻辑
        mBtnSubscription.setOnClickListener(v -> ToastUtils.show("功能正在开发中..."));

        // 获取逻辑层控制器和注册监听
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewController(this);


        // 创建和设置适配器
        mTrackListAdapter = new TrackListAdapter();
        mRcvAlbumDetailList.setLayoutManager(new LinearLayoutManager(this));
        mRcvAlbumDetailList.setAdapter(mTrackListAdapter);

        //TODO 实现 recommend详情列表中 点击按钮下载功能
        mTrackListAdapter.setOnDownloadClickListener((index, track) -> {
            ToastUtils.show("功能正在开发中...");
        });

        //TODO 实现 recommend详情列表中 Item点击跳转逻辑
        mTrackListAdapter.setOnItemClickListener((index, track) -> {
            ToastUtils.show("功能正在开发中...");
        });
    }

    @Override
    public void onLoadedDetailList(List<Track> tracks) {
        mTrackListAdapter.setDat(tracks);
    }

    @Override
    public void onLoadedDetail(Album album) {
        LogUtil.d(TAG, "onLoadedDetail", "album = " + album);


        // 根据专辑ID获取专辑下的声音列表
        mAlbumDetailPresenter.loadDetailListData(album.getId() + "", 1 + "", "asc");


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
}