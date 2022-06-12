package com.ilovesshan.ximalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
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
public class ConfirmCheckBoxDialog extends Dialog {

    private String dialogText = "这是标题";
    private String dialogSureText = "确定";
    private String dialogCancelText = "取消";

    private TextView mTvDialogTitle;
    private TextView mTvDialogSure;
    private TextView mTvDialogCancel;
    private CheckBox mCbCleanAllHistory;

    private OnDialogActionClickListener mOnDialogActionClickListener = null;


    public ConfirmCheckBoxDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ConfirmCheckBoxDialog(@NonNull Context context, int themeResId) {
        this(context, true, null);
    }

    protected ConfirmCheckBoxDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局文件
        setContentView(R.layout.confirm_check_box_dialog);


        // 初始化view
        initView();

        // 绑定事件
        bindEvent();


    }

    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mTvDialogTitle = findViewById(R.id.tv_dialog_title);
        mTvDialogSure = findViewById(R.id.tv_dialog_sure);
        mTvDialogCancel = findViewById(R.id.tv_dialog_cancel);
        mCbCleanAllHistory = findViewById(R.id.cb_clean_all_history);

        mTvDialogTitle.setText(dialogText);
        mTvDialogSure.setText(dialogSureText);
        mTvDialogCancel.setText(dialogCancelText);
    }

    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {
        mTvDialogSure.setOnClickListener(v -> mOnDialogActionClickListener.onSureClick(mCbCleanAllHistory.isChecked()));
        mTvDialogCancel.setOnClickListener(v -> mOnDialogActionClickListener.onCancelClick());
    }

    public void setDialogTitle(String dialogText) {
        this.dialogText = dialogText;
    }

    public void setDialogSureText(String dialogSureText) {
        this.dialogSureText = dialogSureText;
    }

    public void setDialogCancelText(String dialogCancelText) {
        this.dialogCancelText = dialogCancelText;
    }

    public void setOnDialogActionClickListener(OnDialogActionClickListener listener) {
        this.mOnDialogActionClickListener = listener;
    }

    public interface OnDialogActionClickListener {
        void onCancelClick();

        void onSureClick(boolean isCleanAll);
    }

}
