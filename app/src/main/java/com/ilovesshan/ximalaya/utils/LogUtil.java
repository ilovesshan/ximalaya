package com.ilovesshan.ximalaya.utils;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description: 封装了一个日志输出工具
 */

public class LogUtil {
    public static String mBaseTag = "LOG_UTIL";
    public static boolean mIsRelease = false;

    /**
     * 初始化工具
     *
     * @param baseTag   App包名
     * @param isRelease 模式
     */
    public static void init(String baseTag, boolean isRelease) {
        mBaseTag = baseTag;
        mIsRelease = isRelease;
    }

    /**
     * debug mode
     *
     * @param TAG        TAG名称
     * @param methodName 方法名称
     * @param content    输出信息
     */
    public static void d(String TAG, String methodName, String content) {
        if (!mIsRelease) {
            Log.d("[" + mBaseTag + "]" + " [" + TAG + " | " + methodName + "] ==> ", content);
        }
    }

    /**
     * verbose mode
     *
     * @param TAG        TAG名称
     * @param methodName 方法名称
     * @param content    输出信息
     */
    public static void v(String TAG, String methodName, String content) {
        if (!mIsRelease) {
            Log.v("[" + mBaseTag + "]" + " [" + TAG + " | " + methodName + "] ==> ", content);
        }
    }

    /**
     * info mode
     *
     * @param TAG        TAG名称
     * @param methodName 方法名称
     * @param content    输出信息
     */
    public static void i(String TAG, String methodName, String content) {
        if (!mIsRelease) {
            Log.i("[" + mBaseTag + "]" + " [" + TAG + " | " + methodName + "] ==> ", content);
        }
    }

    /**
     * warn mode
     *
     * @param TAG        TAG名称
     * @param methodName 方法名称
     * @param content    输出信息
     */
    public static void w(String TAG, String methodName, String content) {
        if (!mIsRelease) {
            Log.w("[" + mBaseTag + "]" + " [" + TAG + " | " + methodName + "] ==> ", content);
        }
    }

    /**
     * error mode
     *
     * @param TAG        TAG名称
     * @param methodName 方法名称
     * @param content    输出信息
     */
    public static void e(String TAG, String methodName, String content) {
        if (!mIsRelease) {
            Log.e("[" + mBaseTag + "]" + " [" + TAG + " | " + methodName + "] ==> ", content);
        }
    }
}


