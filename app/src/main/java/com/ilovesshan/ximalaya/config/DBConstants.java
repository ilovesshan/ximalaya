package com.ilovesshan.ximalaya.config;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public class DBConstants {

    // 数据库
    public static final String DB_NAME = "ximalaya_db";
    // 数据库 版本
    public static final int DB_VERSION = 1;

    // 订阅表
    public static final String DB_SUBSCRIPTION_TABLE_NAME = "subscription";

    // 订阅表 字段
    public static final String DB_SUBSCRIPTION_ID = "id";
    public static final String DB_SUBSCRIPTION_ALBUM_ID = "album_id ";
    public static final String DB_SUBSCRIPTION_ALBUM_TITLE = "title ";
    public static final String DB_SUBSCRIPTION_ALBUM_SCORE = "score";
    public static final String DB_SUBSCRIPTION_ALBUM_DESCRIBE = "describe";
    public static final String DB_SUBSCRIPTION_ALBUM_IMAGE_COVER = "image_cover";
    public static final String DB_SUBSCRIPTION_ALBUM_PLAY_COUNT = "play_count";
    public static final String DB_SUBSCRIPTION_ALBUM_SUBSCRIPTION_COUNT = "subscription_count";
    public static final String DB_SUBSCRIPTION_ALBUM_AUTHOR_IMAGE_COVER = "author_image_cover ";
    public static final String DB_SUBSCRIPTION_ALBUM_AUTHOR_NICK_NAME = "author_nick_name ";


    // 历史表
    public static final String DB_HISTORY_TABLE_NAME = "subscription";

    // 历史表 字段
    public static final String DB_HISTORY_ID = "id";
    public static final String DB_HISTORY_ALBUM_ID = "album_id ";
    public static final String DB_HISTORY_ALBUM_TITLE = "title ";
    public static final String DB_HISTORY_ALBUM_SCORE = "score";
    public static final String DB_HISTORY_ALBUM_DESCRIBE = "describe";
    public static final String DB_HISTORY_ALBUM_IMAGE_COVER = "image_cover";
    public static final String DB_HISTORY_ALBUM_PLAY_COUNT = "play_count";
    public static final String DB_HISTORY_ALBUM_SUBSCRIPTION_COUNT = "subscription_count";
    public static final String DB_HISTORY_ALBUM_AUTHOR_IMAGE_COVER = "author_image_cover ";
    public static final String DB_HISTORY_ALBUM_AUTHOR_NICK_NAME = "author_nick_name ";
}
