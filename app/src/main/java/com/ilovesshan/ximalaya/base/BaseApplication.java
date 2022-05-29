package com.ilovesshan.ximalaya.base;

import android.app.Application;
import android.content.Context;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.DeviceInfoProviderDefault;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDeviceInfoProvider;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.util.SharedPreferencesUtil;

import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/25
 * @description:
 */
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";

    private static final String KEY_LAST_OAID = "last_oaid";
    private static final boolean IS_RELEASE = false;

    private static Handler mHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化日志输出工具
        LogUtil.init(this.getPackageName(), IS_RELEASE);

        // 初始化喜马拉雅配置
        initXimalayaConfig();

        // 初始化mHandler
        mHandler = new Handler();

        // 初始化Toast框架
        ToastUtils.init(this);

        // 初始化喜马拉雅 播放器 XmPlayerManager
        XmPlayerManager.getInstance(this).init();
    }


    /**
     * 获取 Handler对象
     *
     * @return Handler
     */

    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 初始化喜马拉雅配置
     */
    private void initXimalayaConfig() {
        CommonRequest mXimalaya = CommonRequest.getInstanse();
        if (DTransferConstants.isRelease) {
            String mAppSecret = "8646d66d6abe2efd14f2891f9fd1c8af";
            mXimalaya.setAppkey("9f9ef8f10bebeaa83e71e62f935bede8");
            mXimalaya.setPackid("com.app.test.android");
            mXimalaya.init(this, mAppSecret, true, getDeviceInfoProvider(this));
        } else {
            String mAppSecret = "ff31ae8153185db13b5f5393cae962c4";
            mXimalaya.setAppkey("be022ee6e9f19df55c4a6eb836b7b0b9");
            mXimalaya.setPackid("android.test");
            mXimalaya.init(this, mAppSecret, getDeviceInfoProvider(this));
        }
    }

    public IDeviceInfoProvider getDeviceInfoProvider(Context context) {
        return new DeviceInfoProviderDefault(context) {
            @Override
            public String oaid() {
                // 合作方要尽量优先回传用户真实的oaid，使用oaid可以关联并打通喜马拉雅主app中记录的用户画像数据，对后续个性化推荐接口推荐给用户内容的准确性会有极大的提升！
                return SharedPreferencesUtil.getInstance(getApplicationContext()).getString(KEY_LAST_OAID);
            }
        };
    }
}
