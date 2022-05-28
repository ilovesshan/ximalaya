package com.ilovesshan.ximalaya.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity implements IAlbumDetailViewController {
    private static final String TAG = "AlbumDetailActivity";
    private AlbumDetailPresenter mAlbumDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        // 初始化view和绑定事件
        initViewAndBindEvent();

    }

    /**
     * 初始化view和绑定事件 处理函数
     */
    private void initViewAndBindEvent() {
        // 获取逻辑层控制器和注册监听
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewController(this);
    }

    @Override
    public void onLoadedDetailList(List<Album> album) {

    }

    @Override
    public void onLoadedDetail(Album album) {
        LogUtil.d(TAG, "onLoadedDetail", "album = " + album);
    }
}