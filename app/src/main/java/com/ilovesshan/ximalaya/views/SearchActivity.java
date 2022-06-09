package com.ilovesshan.ximalaya.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.base.BaseApplication;


@SuppressLint("ServiceCast")
public class SearchActivity extends AppCompatActivity {
    private ImageView mIvBack;
    private EditText mEtSearchInput;
    private TextView mTvSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // 初始化view
        initView();

        // 绑定事件
        bindEvent();

    }


    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mEtSearchInput = findViewById(R.id.et_search_input);
        mTvSearchBtn = findViewById(R.id.tv_search_btn);
    }


    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {
        visibleSoftInputFromWindow(true);

        // 返回上一个界面
        mIvBack.setOnClickListener(v -> finish());

        // 点击搜索
        mTvSearchBtn.setOnClickListener(v -> {
            // 关闭键盘
            visibleSoftInputFromWindow(false);
            ToastUtils.show("搜索");
        });

    }


    /**
     * 谈起键盘
     */

    void visibleSoftInputFromWindow(boolean visible) {
        InputMethodManager inputMethodManager = (InputMethodManager) (getSystemService(Context.INPUT_METHOD_SERVICE));
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (inputMethodManager != null) {
                    if (visible) {
                        // 弹出
                        mEtSearchInput.requestFocus();
                        inputMethodManager.showSoftInput(mEtSearchInput, 0);
                    } else {
                        // 隐藏
                        mEtSearchInput.clearFocus();
                        inputMethodManager.hideSoftInputFromWindow(mEtSearchInput.getWindowToken(), 0);
                    }
                }
            }
        }, 500);
    }
}