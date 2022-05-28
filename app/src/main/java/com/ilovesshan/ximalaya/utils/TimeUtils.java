package com.ilovesshan.ximalaya.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public class TimeUtils {
    public static String timeFormatToString(Object timeStamp, String rules) {
        return new SimpleDateFormat(rules, Locale.CHINA).format(timeStamp);
    }
}
