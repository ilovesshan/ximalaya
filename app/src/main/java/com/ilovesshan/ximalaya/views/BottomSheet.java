package com.ilovesshan.ximalaya.views;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.base.BaseApplication;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/1
 * @description:
 */
public class BottomSheet extends PopupWindow {

    public BottomSheet() {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 获取布局文件
        View view = LayoutInflater.from(BaseApplication.getBaseCtx()).inflate(R.layout.item_player_list, null);

        // 点击之外的区域可以关闭
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);

        // 添加到视图中
        setContentView(view);

        // 设置动画
        setAnimationStyle(R.style.bottom_sheet_animation);

    }
}
