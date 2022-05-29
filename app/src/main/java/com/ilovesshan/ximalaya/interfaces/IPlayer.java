package com.ilovesshan.ximalaya.interfaces;

import com.ilovesshan.ximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/29
 * @description:
 */

public interface IPlayer extends IBasePresenter<IPlayerViewController> {

    /**
     * 试恢复播放器的播放状态，如果恢复失败，则播放列表的第一条声音
     */

    public void play();


    /**
     * 播放指定索引处的声音
     *
     * @param index 索引
     */
    public void play(int index);


    /**
     * 暂停播放器(如果暂停的时候正在缓冲状态,是暂停不掉的,需要在缓冲结束后(比如开始播放时)才能暂停)
     */
    public void pause();


    /**
     * 停止播放器(一般情况下不需要调用,希望暂停就调用pause)
     */
    public void stop();


    /**
     * 上一首
     */
    public void playPrev();


    /**
     * 下一首
     */
    public void playNext();


    /**
     * 设置播放器模式，mode取值为PlayMode中的下列之一：‘
     * PLAY_MODEL_SINGLE单曲播放
     * PLAY_MODEL_SINGLE_LOOP 单曲循环播放
     * PLAY_MODEL_LIST列表播放
     * PLAY_MODEL_LIST_LOOP列表循环
     * PLAY_MODEL_RANDOM 随机播放
     *
     * @param mode 播放模式
     */

    public void setPlayMode(int mode);


    /**
     * 播放进度改变(进度条)
     *
     * @param progress 播放进度
     */

    public void setProgress(long progress);


    /**
     * 请求播放列表
     *
     */
    public void loadPlayList() ;
}
