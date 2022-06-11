package com.ilovesshan.ximalaya.data.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.config.DBConstants;
import com.ilovesshan.ximalaya.data.DataBaseHelper;
import com.ilovesshan.ximalaya.data.ISubscriptionCallBack;
import com.ilovesshan.ximalaya.data.ISubscriptionDao;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/11
 * @description:
 */

@SuppressLint({"Range", "Recycle"})
public class SubscriptionDaoImpl implements ISubscriptionDao {
    private static final String TAG = "SubscriptionDaoImpl";

    private final DataBaseHelper mDataBaseHelper;
    private ISubscriptionCallBack mISubscriptionCallBack;

    private SubscriptionDaoImpl() {
        // 创建 数据库Helper
        mDataBaseHelper = new DataBaseHelper(BaseApplication.getBaseCtx());
    }

    private static SubscriptionDaoImpl sSubscriptionDao = null;

    public static SubscriptionDaoImpl getInstance() {
        if (sSubscriptionDao == null) {
            synchronized (SubscriptionDaoImpl.class) {
                if (sSubscriptionDao == null) {
                    sSubscriptionDao = new SubscriptionDaoImpl();
                }
            }
        }
        return sSubscriptionDao;
    }


    @Override
    public void addSubscription(Album album) {
        SQLiteDatabase database = null;
        try {
            database = mDataBaseHelper.getWritableDatabase();
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_ID, album.getId());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_TITLE, album.getAlbumTitle());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_SCORE, album.getAlbumScore());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_DESCRIBE, album.describeContents());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_IMAGE_COVER, album.getCoverUrlSmall());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_PLAY_COUNT, album.getPlayCount());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_SUBSCRIPTION_COUNT, album.getSubscribeCount());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_AUTHOR_IMAGE_COVER, album.getAnnouncer().getAvatarUrl());
            values.put(DBConstants.DB_SUBSCRIPTION_ALBUM_AUTHOR_NICK_NAME, album.getAnnouncer().getNickname());

            long row = database.insert(DBConstants.DB_SUBSCRIPTION_TABLE_NAME, null, values);
            LogUtil.d(TAG, "addSubscription", "订阅专辑结果：" + row);

            if (mISubscriptionCallBack != null) {
                mISubscriptionCallBack.onAddSubscriptionResult(true);
            }

            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (mISubscriptionCallBack != null) {
                mISubscriptionCallBack.onAddSubscriptionResult(false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }

    }


    @Override
    public void deleteSubscription(long id) {
        SQLiteDatabase database = null;
        try {
            database = mDataBaseHelper.getWritableDatabase();
            database.beginTransaction();

            int row = database.delete(DBConstants.DB_SUBSCRIPTION_TABLE_NAME, DBConstants.DB_SUBSCRIPTION_ALBUM_ID + "= ?", new String[]{id + ""});
            LogUtil.d(TAG, "addSubscription", "删除专辑结果：" + row);
            if (mISubscriptionCallBack != null) {
                mISubscriptionCallBack.onDeleteSubscriptionResult(true);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (mISubscriptionCallBack != null) {
                mISubscriptionCallBack.onDeleteSubscriptionResult(false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }

    @Override
    public void querySubscriptionList() {
        ArrayList<Album> albums = new ArrayList<>();
        SQLiteDatabase database = null;

        try {
            database = mDataBaseHelper.getReadableDatabase();

            database.beginTransaction();
            Cursor cursor = database.query(DBConstants.DB_SUBSCRIPTION_TABLE_NAME, null, null, null, null, null, "id desc");
            while (cursor.moveToNext()) {

                //TODO  cursor.getColumnIndex() 传入静态常量 报异常 暂未解决

                // int id = cursor.getInt(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ID));
                // int albumId = cursor.getInt(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_ID));
                // String title = cursor.getString(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_TITLE));
                // int score = cursor.getInt(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_SCORE));
                // String describe = cursor.getString(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_DESCRIBE));
                // String imageCover = cursor.getString(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_IMAGE_COVER));
                // int playCount = cursor.getInt(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_PLAY_COUNT));
                // int subscriptionCount = cursor.getInt(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_SUBSCRIPTION_COUNT));
                // String authorImageCover = cursor.getString(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_AUTHOR_IMAGE_COVER));
                // String authorNickName = cursor.getString(cursor.getColumnIndex(DBConstants.DB_SUBSCRIPTION_ALBUM_AUTHOR_NICK_NAME));


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


            LogUtil.d(TAG, "addSubscription", "查询专辑结果条数：" + albums.size());
            if (mISubscriptionCallBack != null) {
                mISubscriptionCallBack.onSubscriptionListLoaded(albums, true);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (mISubscriptionCallBack != null) {
                mISubscriptionCallBack.onSubscriptionListLoaded(albums, false);
            }
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
                database.close();
            }
        }
    }


    public void setISubscriptionCallBack(ISubscriptionCallBack callBack) {
        this.mISubscriptionCallBack = callBack;
    }

    public void removeISubscriptionCallBack() {
        if (mISubscriptionCallBack != null) {
            this.mISubscriptionCallBack = null;
        }
    }
}
