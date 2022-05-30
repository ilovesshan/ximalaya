package com.ilovesshan.ximalaya.presenter;

import static com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants.STATE_PREPARED;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.interfaces.IPlayer;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/29
 * @description:
 */
public class PlayerPresenter implements IPlayer {
    private static final String TAG = "PlayerPresenter";

    private List<IPlayerViewController> mIPlayerViewControllers = new ArrayList<>();
    private List<Track> mTracks = new ArrayList<>();
    private int mIndex = 0;
    private boolean mPlaySet = false;

    private static PlayerPresenter sPlayerPresenter = null;
    private XmPlayerManager mXmPlayerManager;

    private PlayerPresenter() {
    }

    public static PlayerPresenter getInstance() {
        if (sPlayerPresenter == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;
    }

    @Override
    public void play() {
        if (mPlaySet) {
            mXmPlayerManager.play();
        }
    }

    @Override
    public void play(int index) {
        if (mPlaySet) {
            mXmPlayerManager.play(index);
        }
    }

    @Override
    public void pause() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.pause();
        }
    }

    @Override
    public void stop() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.stop();
        }
    }

    @Override
    public void playPrev() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.playNext();
        }
    }

    @Override
    public void setPlayMode(XmPlayListControl.PlayMode playMode) {
        for (IPlayerViewController iPlayerViewController : mIPlayerViewControllers) {
            iPlayerViewController.onPlayModeUpdate(playMode);
        }
    }

    @Override
    public void seekTo(int progress) {
        if (mXmPlayerManager != null) {
            mXmPlayerManager.seekTo(progress);
        }
    }


    public boolean isPlaying() {
        return mXmPlayerManager.isPlaying();
    }

    @Override
    public void registerViewController(IPlayerViewController iPlayerViewController) {
        if (!mIPlayerViewControllers.contains(iPlayerViewController)) {
            mIPlayerViewControllers.add(iPlayerViewController);
        }
        iPlayerViewController.onLoadedPlayList(mTracks);
    }

    @Override
    public void unRegisterViewController(IPlayerViewController iPlayerViewController) {
        mIPlayerViewControllers.remove(iPlayerViewController);
    }

    public void setPlayList(List<Track> tracks, int index) {
        mTracks.clear();
        mTracks.addAll(tracks);
        mIndex = index;

        mXmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getBaseCtx());
        mXmPlayerManager.setPlayList(tracks, index);
        mPlaySet = true;


        // 监听广告播放状态
        mXmPlayerManager.addAdsStatusListener(new IXmAdsStatusListener() {
            /**
             * 	开始获取广告物料
             */
            @Override
            public void onStartGetAdsInfo() {
                LogUtil.d(TAG, "onStartGetAdsInfo", "开始获取广告物料");
            }

            /**
             * 获取广告物料成功
             * @param advertiseList 广告信息
             */
            @Override
            public void onGetAdsInfo(AdvertisList advertiseList) {
                LogUtil.d(TAG, "onStartGetAdsInfo", "获取广告物料成功");
            }

            /**
             * 广告开始缓冲
             */
            @Override
            public void onAdsStartBuffering() {
                LogUtil.d(TAG, "onStartGetAdsInfo", "广告开始缓冲");
            }

            /**
             * 广告结束缓冲
             */
            @Override
            public void onAdsStopBuffering() {
                LogUtil.d(TAG, "onStartGetAdsInfo", "广告结束缓冲");
            }

            /**
             * 开始播放广告
             *
             * @param advertise ad : 当前播放广告
             * @param i position : 当前播放的广告在广告列表中的索引
             */
            @Override
            public void onStartPlayAds(Advertis advertise, int i) {
                LogUtil.d(TAG, "onStartGetAdsInfo", "开始播放广告");
                ToastUtils.show("播放广告中...");
            }

            /**
             * 广告播放完毕
             */
            @Override
            public void onCompletePlayAds() {
                LogUtil.d(TAG, "onStartGetAdsInfo", "广告播放完毕");
                ToastUtils.show("广告播放完毕...");
            }

            /**
             * 播放广告错误
             *
             * @param what 错误类型
             * @param extra 错误的额外信息
             */
            @Override
            public void onError(int what, int extra) {
                LogUtil.d(TAG, "onStartGetAdsInfo", "播放广告错误 what = " + what + " extra = " + extra);
            }
        });

        // 监听播放器的状态变化
        mXmPlayerManager.addPlayerStatusListener(new IXmPlayerStatusListener() {
            /**
             * 开始播放
             */
            @Override
            public void onPlayStart() {
                LogUtil.d(TAG, "onPlayStart", "开始播放");
                for (IPlayerViewController iPlayerViewController : mIPlayerViewControllers) {
                    iPlayerViewController.onPlayStart(tracks.get(mXmPlayerManager.getCurrentIndex()));
                    iPlayerViewController.onTrackUpdate(tracks.get(mXmPlayerManager.getCurrentIndex()), mXmPlayerManager.getCurrentIndex());
                }
            }

            /**
             * 暂停播放
             */
            @Override
            public void onPlayPause() {
                LogUtil.d(TAG, "onPlayStart", "暂停播放");
                for (IPlayerViewController iPlayerViewController : mIPlayerViewControllers) {
                    iPlayerViewController.onPlayPause();
                }
            }

            /**
             * 停止播放
             */
            @Override
            public void onPlayStop() {
                LogUtil.d(TAG, "onPlayStart", "停止播放");
                for (IPlayerViewController iPlayerViewController : mIPlayerViewControllers) {
                    iPlayerViewController.onPlayStop();
                }
            }


            /**
             *  播放完成
             */
            @Override
            public void onSoundPlayComplete() {
                LogUtil.d(TAG, "onPlayStart", "播放完成");
            }

            /**
             *  播放器准备完毕
             */
            @Override
            public void onSoundPrepared() {
                LogUtil.d(TAG, "onPlayStart", "播放器准备完毕");
                if (mXmPlayerManager.getPlayerStatus() == STATE_PREPARED) {
                    mXmPlayerManager.play();
                }
            }

            /**
             * 切歌
             * 请通过model中的kind字段来判断是track、radio和schedule； 上一首的播放时间请通过lastPlayedMills字段来获取;
             *
             * @param prevMode 上一首model,可能为空
             * @param currentMode 当前首model
             */
            @Override
            public void onSoundSwitch(PlayableModel prevMode, PlayableModel currentMode) {
                LogUtil.d(TAG, "onPlayStart", "切歌");
                if (prevMode != null) {
                    if (currentMode instanceof Track) {
                        for (IPlayerViewController iPlayerViewController : mIPlayerViewControllers) {
                            iPlayerViewController.onTrackUpdate((Track) currentMode, mXmPlayerManager.getCurrentIndex());
                        }
                    }
                }
            }

            /**
             * 开始缓冲
             */
            @Override
            public void onBufferingStart() {
                LogUtil.d(TAG, "onPlayStart", "开始缓冲");
            }

            /**
             * 结束缓冲
             */
            @Override
            public void onBufferingStop() {
                LogUtil.d(TAG, "onPlayStart", "结束缓冲");
            }

            /**
             * 缓冲进度回调
             * @param i 缓冲进度
             */
            @Override
            public void onBufferProgress(int i) {
                LogUtil.d(TAG, "onPlayStart", "缓冲进度回调 i =" + i);
            }

            /**
             * 播放进度回调
             *
             * @param currPosition 当前播放进度
             * @param totalDuration 总进度
             */
            @Override
            public void onPlayProgress(int currPosition, int totalDuration) {
                LogUtil.d(TAG, "onPlayStart", "播放进度回调 currPosition " + currPosition + " totalDuration = " + totalDuration);
                for (IPlayerViewController iPlayerViewController : mIPlayerViewControllers) {
                    iPlayerViewController.onPlayProgressUpdate(currPosition, totalDuration);
                }
            }

            /**、
             * 播放器错误
             * @param e 错判信息
             * @return boolean
             */
            @Override
            public boolean onError(XmPlayerException e) {
                LogUtil.d(TAG, "onPlayStart", "播放器错误 message = " + e.getMessage());
                return false;
            }
        });
    }
}
