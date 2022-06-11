package com.ilovesshan.ximalaya.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ilovesshan.ximalaya.config.DBConstants;
import com.ilovesshan.ximalaya.utils.LogUtil;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d(TAG, "onCreate", "创建数据库" + DBConstants.DB_NAME);
        String createTableSql = "CREATE TABLE " + DBConstants.DB_SUBSCRIPTION_TABLE_NAME + " (" +
                DBConstants.DB_SUBSCRIPTION_ID + " INTEGER (100)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_ID + " INTEGER (100)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_TITLE + " VARCHAR (100)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_DESCRIBE + " VARCHAR (255)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_SCORE + " INTEGER (100)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_IMAGE_COVER + " VARCHAR (50)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_PLAY_COUNT + " INTEGER (100)," +
                DBConstants.DB_SUBSCRIPTION_ALBUM_SUBSCRIPTION_COUNT + " INTEGER (100)" +
                ")";

        db.execSQL(createTableSql);
        LogUtil.d(TAG, "onCreate", "数据库创建ok");

        // 添加测试数据
        // String sql1 = "INSERT INTO " + DBConstants.DB_SUBSCRIPTION_TABLE_NAME + "(" + DBConstants.DB_SUBSCRIPTION_ALBUM_TITLE + "," + DBConstants.DB_SUBSCRIPTION_ALBUM_DESCRIBE + ") " + "VALUES('我是标题111','我是描述111')";
        // String sql2 = "INSERT INTO " + DBConstants.DB_SUBSCRIPTION_TABLE_NAME + "(" + DBConstants.DB_SUBSCRIPTION_ALBUM_TITLE + "," + DBConstants.DB_SUBSCRIPTION_ALBUM_DESCRIBE + ") " + "VALUES('我是标题222','我是描述222')";
        //
        // db.execSQL(sql1);
        // db.execSQL(sql2);
        //

        // 查询
        // Cursor query = db.query(DBConstants.DB_SUBSCRIPTION_TABLE_NAME, new String[]{"title", "describe"}, null, null, null, null, null);
        //
        // while (query.moveToNext()) {
        //     @SuppressLint("Range") String title = query.getString(query.getColumnIndex("title"));
        //     @SuppressLint("Range") String describe = query.getString(query.getColumnIndex("describe"));
        //     LogUtil.d(TAG, "onCreate", title + "====" + describe);
        // }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
