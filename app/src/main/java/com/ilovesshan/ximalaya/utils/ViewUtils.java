package com.ilovesshan.ximalaya.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public class ViewUtils {
    /**
     * 设置透明状态栏
     *
     * @param activity Activity
     */
    public static void makeStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置圆角 配和Glide
     *
     * @param radius 角度
     * @return RequestOptions
     */
    public static RequestOptions makeBorderRadius(int radius) {
        RoundedCorners roundedCorners = new RoundedCorners(radius);
        return RequestOptions.bitmapTransform(roundedCorners);
    }

    /**
     * 设置高斯模糊  配和Glide
     *
     * @param blur 模糊度
     * @param sampling 缩放sampling背在进行模糊
     * @return RequestOptions
     */
    public static RequestOptions blurTransformation(int blur, int sampling) {
        return RequestOptions.bitmapTransform(new BlurTransformation(blur, sampling));
    }
}
