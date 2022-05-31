package com.ilovesshan.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/29
 * @description:
 */
public interface IPlayerViewController {
    /**
     * 开始播放
     */
    public void onPlayStart(Track track);

    /**
     * 暂停播放
     */
    public void onPlayPause();

    /**
     * 停止播放
     */
    public void onPlayStop();

    /**
     * 播放完成
     */
    public void onSoundPlayComplete();

    /**
     * 上一首
     */
    public void onPlayPrev(Track track);


    /**
     * 下一首
     */
    public void onPlayNext(Track track);

    /**
     * 请求播放列表加载完成
     */
    public void onLoadedPlayList(List<Track> tracks);

    /**
     * 播放器错误
     * <p>
     * 这里面返回的错误code没有参考价值,如果配置正确但是却播放不了,最大的可能就是网络导致的,
     * 如果配置正确但是却播放不了,最大的可能就是网络导致的,请注意log中的"PlayError"字段 ,如果responseCode != 200 说明就是网络问题,请换个网络重试下看看
     * code=612 表示没有播放地址
     * 参考喜马拉雅SDK文档：XimalayaAndroidSDK_V8.0.9/文档/喜马拉雅SDK接入文档.html#41-播放器概述
     */
    public void onPlayError(XmPlayerException exception);

    /**
     * 开始播放广告
     *
     * @param ad       当前播放广告
     * @param position 当前播放的广告在广告列表中的索引
     */
    public void onStartPlayAds(Advertis ad, int position);

    /**
     * 播放广告错误
     *
     * @param what  错误类型
     * @param extra 错误的额外信息
     */
    public void onPlayAdsError(int what, int extra);

    /**
     * 广告播放完毕
     */
    public void onCompletePlayAds();


    /**
     * 播放进度更新
     */
    public void onPlayProgressUpdate(int currPosition, int totalDuration);


    /**
     * 播放模式改变
     */
    public void onPlayModeUpdate(XmPlayListControl.PlayMode playMode);


    /**
     * @param track    节目信息
     * @param position 播放索引
     */
    public void onTrackUpdate(Track track, int position);



    public void onInitPlayMode(XmPlayListControl.PlayMode playMode);


}

