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
import com.ilovesshan.ximalaya.interfaces.IRecommendViewController;
import com.ilovesshan.ximalaya.presenter.RecommendPresenter;
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
 * @description: 推荐界面UI
 */

@SuppressLint("ResourceType")
public class RecommendFragment extends BaseFragment implements IRecommendViewController {

    private static final String TAG = "RecommendFragment";

    private RecommendListAdapter mAdapter;
    private View mViewItem;
    private RecommendPresenter mRecommendPresenter;


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

        // 实例化逻辑层控制器
        mRecommendPresenter = RecommendPresenter.getInstance();
        // 请求数据
        mRecommendPresenter.loadedData();
        // 设置回调 监听数据
        mRecommendPresenter.registerViewController(this);
        return mViewItem;
    }


    // 数据加载成功回调
    @Override
    public void onDataLoaded(List<Album> albumList) {
        mAdapter.setData(albumList);
    }
}
