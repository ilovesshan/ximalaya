package com.ilovesshan.ximalaya.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ilovesshan.ximalaya.utils.ViewUtils;

public class PlayerActivity extends AppCompatActivity {

    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // 透明状态栏
        ViewUtils.makeStatusBarTransparent(this);

        // 初始化view和绑定事件
        initViewAndBindEvent();

    }

    /**
     * 初始化view和绑定事件 处理函数
     */
    private void initViewAndBindEvent() {
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.play();
    }
}