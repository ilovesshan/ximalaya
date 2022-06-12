package com.ilovesshan.ximalaya.data.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.config.DBConstants;
import com.ilovesshan.ximalaya.data.DataBaseHelper;
import com.ilovesshan.ximalaya.data.IHistoryCallBack;
import com.ilovesshan.ximalaya.data.IHistoryDao;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/12
 * @description:
 */

@SuppressLint("Range")
public class HistoryDaoImpl implements IHistoryDao {

    private static final String TAG = "HistoryImpl";

    private final DataBaseHelper mDataBaseHelper;

    private IHistoryCallBack IHistoryCallBack = null;

    private HistoryDaoImpl() {
        // 创建 数据库Helper
        mDataBaseHelper = new DataBaseHelper(BaseApplication.getBaseCtx());
    }

    private static HistoryDaoImpl sHistory = null;

    public static HistoryDaoImpl getInstance() {
        if (sHistory == null) {
            synchronized (HistoryDaoImpl.class) {
                if (sHistory == null) {
                    sHistory = new HistoryDaoImpl();
                }
            }
        }
        return sHistory;
    }

    // 添加专辑到及历记录
    @Override
    public void addAlbum(Album album) {
        SQLiteDatabase database = null;
        try {
            database = mDataBaseHelper.getWritableDatabase();
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(DBConstants.DB_HISTORY_ALBUM_ID, album.getId());
            values.put(DBConstants.DB_HISTORY_ALBUM_TITLE, album.getAlbumTitle());
            values.put(DBConstants.DB_HISTORY_ALBUM_SCORE, album.getAlbumScore());
            values.put(DBConstants.DB_HISTORY_ALBUM_DESCRIBE, album.getAlbumIntro());
            values.put(DBConstants.DB_HISTORY_ALBUM_IMAGE_COVER, album.getCoverUrlSmall());
            values.put(DBConstants.DB_HISTORY_ALBUM_PLAY_COUNT, album.getPlayCount());
            values.put(DBConstants.DB_HISTORY_ALBUM_SUBSCRIPTION_COUNT, album.getSubscribeCount());
            values.put(DBConstants.DB_HISTORY_ALBUM_AUTHOR_IMAGE_COVER, album.getAnnouncer().getAvatarUrl());
            values.put(DBConstants.DB_HISTORY_ALBUM_AUTHOR_NICK_NAME, album.getAnnouncer().getNickname());

            // 添加之前先删除掉(暂未考虑有没有存在)
            database.delete(DBConstants.DB_HISTORY_TABLE_NAME, DBConstants.DB_HISTORY_ALBUM_ID + "= ?", new String[]{album.getId() + ""});

            long row = database.insert(DBConstants.DB_HISTORY_TABLE_NAME, null, values);
            LogUtil.d(TAG, "addSubscription", "添加专辑到历史记录结果：" + row);

            if (IHistoryCallBack != null) {
                IHistoryCallBack.onAddAlbumResult(true);
            }

            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onAddAlbumResult(false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }

    // 查询历史记录列表
    @Override
    public void queryAlbumList() {
        ArrayList<Album> albums = new ArrayList<>();
        SQLiteDatabase database = null;

        try {
            database = mDataBaseHelper.getReadableDatabase();

            database.beginTransaction();
            Cursor cursor = database.query(DBConstants.DB_HISTORY_TABLE_NAME, null, null, null, null, null, "id desc");
            while (cursor.moveToNext()) {
                //TODO  cursor.getColumnIndex() 传入静态常量 报异常 暂未解决
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int albumId = cursor.getInt(cursor.getColumnIndex("album_id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                String describe = cursor.getString(cursor.getColumnIndex("describe"));
                String imageCover = cursor.getString(cursor.getColumnIndex("image_cover"));
                int playCount = cursor.getInt(cursor.getColumnIndex("play_count"));
                int subscriptionCount = cursor.getInt(cursor.getColumnIndex("subscription_count"));
                String authorImageCover = cursor.getString(cursor.getColumnIndex("author_image_cover"));
                String authorNickName = cursor.getString(cursor.getColumnIndex("author_nick_name"));

                Album album = new Album();

                album.setId(albumId);
                album.setAlbumTitle(title);
                album.setAlbumScore(score + "");
                album.setAlbumIntro(describe);
                album.setCoverUrlSmall(imageCover);
                album.setPlayCount(playCount);
                album.setSubscribeCount(subscriptionCount);
                Announcer announcer = new Announcer();
                announcer.setAvatarUrl(authorImageCover);
                announcer.setNickname(authorNickName);
                album.setAnnouncer(announcer);
                albums.add(album);

            }


            LogUtil.d(TAG, "addSubscription", "查询历史记录专辑结果条数：" + albums.size());
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onQueryAlbumListResult(albums, true);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onQueryAlbumListResult(albums, false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }

    // 根据ID删除专辑历史记录
    @Override
    public void deleteAlbumById(int albumID) {
        SQLiteDatabase database = null;
        try {
            database = mDataBaseHelper.getWritableDatabase();
            database.beginTransaction();

            int row = database.delete(DBConstants.DB_HISTORY_TABLE_NAME, DBConstants.DB_HISTORY_ALBUM_ID + "= ?", new String[]{albumID + ""});
            LogUtil.d(TAG, "addSubscription", "删除专辑历史记录结果：" + row);
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onDeleteAlbumByIdResult(true);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onDeleteAlbumByIdResult(false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }

    // 删除全部专辑历史记录
    @Override
    public void deleteAllAlbum() {
        SQLiteDatabase database = null;
        try {
            database = mDataBaseHelper.getWritableDatabase();
            database.beginTransaction();

            String sql = "DELETE FROM " + DBConstants.DB_HISTORY_TABLE_NAME;
            database.execSQL(sql);
            LogUtil.d(TAG, "addSubscription", "删除全部专辑历史记录 成功");
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onDeleteAllAlbumResult(true);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (IHistoryCallBack != null) {
                IHistoryCallBack.onDeleteAllAlbumResult(false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }

    public void setIHistoryCallBack(IHistoryCallBack callBack) {
        this.IHistoryCallBack = callBack;
    }
}
