package com.ilovesshan.ximalaya.fragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.RecommendListAdapter;
import com.ilovesshan.ximalaya.base.BaseFragment;
import com.ilovesshan.ximalaya.config.Constants;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description: 推荐
 */

@SuppressLint("ResourceType")
public class RecommendFragment extends BaseFragment {

    private static final String TAG = "RecommendFragment";

    private RecommendListAdapter mAdapter;
    private View mViewItem;


    @Override
    protected View getSubViewItem(LayoutInflater inflater, ViewGroup container) {
        // 加载布局文件
        mViewItem = inflater.inflate(R.layout.fragment_recommend, container, false);

        if (mViewItem != null) {
            mAdapter = new RecommendListAdapter();
            RecyclerView recyclerView = mViewItem.findViewById(R.id.rv_recommend_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(mAdapter);
        }

        // 获取推荐列表数据
        requestRecommendList();

        return mViewItem;
    }


    /**
     * 3.10.6 获取猜你喜欢专辑
     * <p>
     * 获取推荐列数据
     */
    private void requestRecommendList() {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_LIST_SIZE + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "onSuccess", "3.10.6 获取猜你喜欢专辑 数据获取成功");
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    mAdapter.setData(albumList);
                    LogUtil.d(TAG, "onSuccess", "albumList" + albumList.size());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onSuccess", "3.10.6 获取猜你喜欢专辑 数据获取失败");
                LogUtil.d(TAG, "onError", "code = " + i + " message = " + s);
            }
        });
    }
}
