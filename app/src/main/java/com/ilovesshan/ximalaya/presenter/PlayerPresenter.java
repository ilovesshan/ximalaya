package com.ilovesshan.ximalaya.presenter;

import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.interfaces.IPlayer;
import com.ilovesshan.ximalaya.interfaces.IPlayerViewController;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

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

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPrev() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void setPlayMode(int mode) {

    }

    @Override
    public void setProgress(long progress) {

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
    }
}
