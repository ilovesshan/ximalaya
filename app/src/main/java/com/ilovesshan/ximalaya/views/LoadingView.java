package com.ilovesshan.ximalaya.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.R;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description: 绘制自定义loading效果
 */

public class LoadingView extends androidx.appcompat.widget.AppCompatImageView {


    // 旋转角度
    private int mDegrees = 0;
    // 是否继续旋转
    private boolean mIsRotating = false;


    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 设置图标
        setImageResource(R.drawable.loading);

    }

    /**
     * 绑定到界面上
     */
    @Override
    protected void onAttachedToWindow() {
        mIsRotating = true;
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += 30;
                mDegrees = mDegrees <= 360 ? mDegrees : 0;
                // 通知界面更新
                invalidate();
                if (mIsRotating) {
                    // 200ms 执行一次
                    postDelayed(this, 200);
                }

            }
        });
        super.onAttachedToWindow();
    }

    /**
     * 从界面解绑
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsRotating = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * float degrees 旋转角度
         * float px 旋转点位 x
         * float py 旋转点位 y
         */
        canvas.rotate(mDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
