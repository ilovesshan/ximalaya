package com.ilovesshan.ximalaya.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.RecommendListAdapter;
import com.ilovesshan.ximalaya.base.BaseFragment;
import com.ilovesshan.ximalaya.interfaces.IRecommendViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.RecommendPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.views.AlbumDetailActivity;
import com.ilovesshan.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

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
    private UILoader mUiLoader;
    private AlbumDetailPresenter mAlbumDetailPresenter;


    @Override
    protected View getSubViewItem(LayoutInflater inflater, ViewGroup container) {

        mUiLoader = new UILoader(getContext()) {
            @Override
            public View createSuccessView() {
                // 加载布局文件
                mViewItem = inflater.inflate(R.layout.fragment_recommend, container, false);
                if (mViewItem != null) {
                    mAdapter = new RecommendListAdapter();
                    // 推荐列表被点击
                    mAdapter.setOnItemClickListener(new RecommendListAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int index, Album album) {
                            mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
                            mAlbumDetailPresenter.setAlbum(album);
                            startActivity(new Intent(getContext(), AlbumDetailActivity.class));
                            LogUtil.d(TAG, "onClick", "index = " + index + "album =" + album);
                        }
                    });
                    RecyclerView recyclerView = mViewItem.findViewById(R.id.rv_recommend_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                    recyclerView.setAdapter(mAdapter);
                }
                return mViewItem;
            }
        };

        // 加载失败 点击重新加载回调
        mUiLoader.setOnRetryLoadClickListener(() -> mRecommendPresenter.loadedData());

        // 实例化逻辑层控制器
        mRecommendPresenter = RecommendPresenter.getInstance();
        // 设置回调 监听数据
        mRecommendPresenter.registerViewController(this);

        // 请求数据
        mRecommendPresenter.loadedData();

        // 解绑
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        return mUiLoader;
    }


    // 数据加载成功回调
    @Override
    public void onDataLoaded(List<Album> albumList) {
        mAdapter.setData(albumList);
        mUiLoader.updateUILoaderState(UILoader.UILoaderState.SUCCESS);
    }

    // 加载失败回调
    @Override
    public void onLoadError() {
        mUiLoader.updateUILoaderState(UILoader.UILoaderState.ERROR);
    }

    // 加载数据为空回调
    @Override
    public void onLoadEmpty() {
        mUiLoader.updateUILoaderState(UILoader.UILoaderState.EMPTY);
    }

    // 加载中回调
    @Override
    public void onLoading() {
        mUiLoader.updateUILoaderState(UILoader.UILoaderState.LOADING);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewController(this);
        }
    }
}
