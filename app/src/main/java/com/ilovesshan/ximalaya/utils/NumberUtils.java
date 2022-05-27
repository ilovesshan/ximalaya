package com.ilovesshan.ximalaya.utils;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/27
 * @description: 数字 工具类
 */
public class NumberUtils {

    /**
     * 将数字进行计数单位格式化 (万)
     */
    public static String number2CountingUnit(long value) {
        String formatValue = "";

        if (value >= 10000) {
            formatValue = new DecimalFormat("#.00").format((value * 1.0) / 10000) + "万";
        } else {
            formatValue = String.valueOf(value);
        }
        return formatValue;
    }
}
