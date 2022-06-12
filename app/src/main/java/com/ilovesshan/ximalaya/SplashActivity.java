package com.ilovesshan.ximalaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ilovesshan.ximalaya.utils.ViewUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 设置状态栏 透明
        ViewUtils.makeStatusBarTransparent(SplashActivity.this);

        FrameLayout layoutSplash = this.findViewById(R.id.splash);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(2000);//设置动画播放时长1000毫秒（1秒）
        layoutSplash.startAnimation(alphaAnimation);
        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                // 跳转到首页 MainActivity
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                // startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}