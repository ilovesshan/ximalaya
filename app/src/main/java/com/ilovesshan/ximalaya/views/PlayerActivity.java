package com.ilovesshan.ximalaya.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.utils.TimeUtils;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

@SuppressLint("UseCompatLoadingForDrawables")
public class PlayerActivity extends AppCompatActivity implements IPlayerViewController {
    private static final String TAG = "PlayerActivity";

    private ImageView mIvPlayerBigCover;
    private TextView mTvPlayerTitle;
    private LinearLayout mLlProgressControllerContainer;
    private LinearLayout mLlPlayerControllerContainer;
    private ImageView mIvPlayerMode;
    private ImageView mIvPlayerPrev;
    private ImageView mIvPlayerPlayOrPause;
    private ImageView mIvPlayerNext;
    private ImageView mIvPlayerList;
    private TextView mTvPlayerCurrentDuration;
    private TextView mTvPlayerTotalDuration;
    private SeekBar mSbPlayerProgress;

    private PlayerPresenter mPlayerPresenter;
    private boolean isUserTouching = false;
    // 默认列表循环播放
    private XmPlayListControl.PlayMode mCurrentPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // 透明状态栏
        // ViewUtils.makeStatusBarTransparent(this);

        // 初始化view
        initView();

        // 绑定事件
        bindEvent();

        // 进入界面 默认开始播放
        startPlay();

    }

    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mIvPlayerBigCover = findViewById(R.id.iv_player_big_cover);
        mTvPlayerTitle = findViewById(R.id.tv_player_title);
        mLlProgressControllerContainer = findViewById(R.id.ll_progress_controller_container);
        mLlPlayerControllerContainer = findViewById(R.id.ll_player_controller_container);
        mIvPlayerMode = findViewById(R.id.iv_player_mode);
        mIvPlayerPrev = findViewById(R.id.iv_player_prev);
        mIvPlayerPlayOrPause = findViewById(R.id.iv_player_play_or_pause);
        mIvPlayerNext = findViewById(R.id.iv_player_next);
        mIvPlayerList = findViewById(R.id.iv_player_list);
        mTvPlayerCurrentDuration = findViewById(R.id.tv_player_current_duration);
        mTvPlayerTotalDuration = findViewById(R.id.tv_player_total_duration);
        mSbPlayerProgress = findViewById(R.id.sb_player_progress);
    }


    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {
        // 获取PlayerPresenter
        mPlayerPresenter = PlayerPresenter.getInstance();

        // 注册IPlayerViewController 控制器
        mPlayerPresenter.registerViewController(this);

        // 播放或者暂停按钮点击回调
        mIvPlayerPlayOrPause.setOnClickListener((v) -> {
            if (mPlayerPresenter != null) {
                if (mPlayerPresenter.isPlaying()) {
                    pausePlay();
                } else {
                    startPlay();
                }
            }
        });

        // 进度条拖动的回调
        mSbPlayerProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 进度条进度改变
             *
             * @param seekBar 进度条
             * @param progress 当前进度
             * @param fromUser 是否是用户在改变进度条
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouching = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayerPresenter.seekTo(seekBar.getProgress());
            }
        });


        // 上一首
        mIvPlayerPrev.setOnClickListener(v -> {
            if (mPlayerPresenter != null) {
                mPlayerPresenter.playPrev();
            }
        });

        // 下一首
        mIvPlayerNext.setOnClickListener(v -> {
            if (mPlayerPresenter != null) {
                mPlayerPresenter.playNext();
            }
        });

        // 改变播放模式
        mIvPlayerMode.setOnClickListener(v -> {
            // PLAY_MODEL_LIST_LOOP 列表循环
            // PLAY_MODEL_RANDOM 随机播放
            // PLAY_MODEL_SINGLE 单曲播放
            // 切换顺序 列表循环 => 随机播放=>单曲播放
            if (mPlayerPresenter != null) {
                if (mCurrentPlayMode == XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP) {
                    mCurrentPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
                } else if (mCurrentPlayMode == XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM) {
                    mCurrentPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE;
                } else if (mCurrentPlayMode == XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE) {
                    mCurrentPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
                }
                mPlayerPresenter.setPlayMode(mCurrentPlayMode);
            }
        });
    }


    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }


    /**
     * 暂停播放
     */
    private void pausePlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.pause();
        }
    }


    /**
     * 停止播放
     */
    private void stopPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.stop();
        }
    }


    @Override
    public void onPlayStart(Track track) {
        if (mIvPlayerPlayOrPause != null) {
            mIvPlayerPlayOrPause.setImageResource(R.drawable.player_pause);
        }
    }

    @Override
    public void onPlayPause() {
        if (mIvPlayerPlayOrPause != null) {
            mIvPlayerPlayOrPause.setImageResource(R.drawable.player_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (mIvPlayerPlayOrPause != null) {
            mIvPlayerPlayOrPause.setImageResource(R.drawable.player_play);
        }
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
        LogUtil.d(TAG, "onLoadedPlayList", "List<Track> ==" + tracks.size());
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
        // currPosition totalDuration 单位毫秒
        String currPositionStr = "";
        String totalDurationStr = "";

        // 如果大于一个小时 格式化成 "HH:mm:ss"，小于个小时 格式化成 "mm:ss"
        if (currPosition > 1000 * 60 * 60) {
            currPositionStr = TimeUtils.timeFormatToString(currPosition, "HH:mm:ss");
            totalDurationStr = TimeUtils.timeFormatToString(totalDuration, "HH:mm:ss");
        } else {
            currPositionStr = TimeUtils.timeFormatToString(currPosition, "mm:ss");
            totalDurationStr = TimeUtils.timeFormatToString(totalDuration, "mm:ss");
        }

        // 更新UI
        mTvPlayerCurrentDuration.setText(currPositionStr);
        mTvPlayerTotalDuration.setText(totalDurationStr);
        mSbPlayerProgress.setMax(totalDuration);
        // 如果用户正在拖动进度条 则不更新
        if (!isUserTouching) {
            mSbPlayerProgress.setProgress(currPosition);
        }
    }

    @Override
    public void onPlayModeUpdate(XmPlayListControl.PlayMode playMode) {
        // PLAY_MODEL_SINGLE 单曲播放
        // PLAY_MODEL_SINGLE_LOOP 单曲循环播放
        // PLAY_MODEL_LIST 列表播放
        // PLAY_MODEL_LIST_LOOP 列表循环播放
        // PLAY_MODEL_RANDOM 随机播放
        if (playMode == XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE) {
            ToastUtils.show("单曲播放");
            mIvPlayerMode.setImageResource(R.drawable.player_mode_danqu);
        } else if (playMode == XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP) {
            ToastUtils.show("列表循环播放");
            mIvPlayerMode.setImageResource(R.drawable.player_mode_shunxu);
        } else if (playMode == XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM) {
            ToastUtils.show("随机播放");
            mIvPlayerMode.setImageResource(R.drawable.player_mode_suiji);
        }
    }

    @Override
    public void onTrackUpdate(Track track, int position) {
        if (mTvPlayerTitle != null) {
            mTvPlayerTitle.setText(track.getTrackTitle());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放资源
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewController(this);
            mPlayerPresenter = null;
        }
    }
}