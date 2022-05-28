package com.ilovesshan.ximalaya.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.R;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */

@SuppressLint("ResourceAsColor")
public class DividerView extends View {

    private Paint mPaint = new Paint();

    public DividerView(Context context) {
        this(context, null);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.parseColor("#ebebeb"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(R.color.recommend_background);
        canvas.drawLine(0, 0, getWidth(), 1, mPaint);
    }
}