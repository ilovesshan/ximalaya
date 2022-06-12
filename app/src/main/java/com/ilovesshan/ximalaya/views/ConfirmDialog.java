package com.ilovesshan.ximalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.R;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/12
 * @description:
 */
public class ConfirmDialog extends Dialog {
    private TextView mTvDialogSure;
    private TextView mTvDialogCancel;

    private OnDialogActionClickListener mOnDialogActionClickListener = null;


    public ConfirmDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        this(context, true, null);
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局文件
        setContentView(R.layout.confirm_dialog);


        // 初始化view
        initView();

        // 绑定事件
        bindEvent();


    }

    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mTvDialogSure = findViewById(R.id.tv_dialog_sure);
        mTvDialogCancel = findViewById(R.id.tv_dialog_cancel);
    }


    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {
        mTvDialogSure.setOnClickListener(v-> mOnDialogActionClickListener.onSureClick());
        mTvDialogCancel.setOnClickListener(v-> mOnDialogActionClickListener.onCancelClick());
    }


    public void setOnDialogActionClickListener(OnDialogActionClickListener listener) {
        this.mOnDialogActionClickListener = listener;
    }

    public interface OnDialogActionClickListener {
        void onCancelClick();

        void onSureClick();
    }

}
