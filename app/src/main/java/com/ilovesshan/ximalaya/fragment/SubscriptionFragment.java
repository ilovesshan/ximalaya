package com.ilovesshan.ximalaya.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.RecommendListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.base.BaseFragment;
import com.ilovesshan.ximalaya.interfaces.ISubscriptionViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.SubscriptionPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.views.AlbumDetailActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description: 订阅
 */

public class SubscriptionFragment extends BaseFragment implements ISubscriptionViewController {
    private static final String TAG = "SubscriptionFragment";

    private View mViewItem;
    private RecommendListAdapter mAdapter;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private SmartRefreshLayout mRefreshLayout;
    private SubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mRecyclerView;

    @Override
    protected View getSubViewItem(LayoutInflater inflater, ViewGroup container) {
        mViewItem = inflater.inflate(R.layout.fragment_subscription, container, false);
        if (mViewItem != null) {
            mRecyclerView = mViewItem.findViewById(R.id.rv_recommend_list);
            mRefreshLayout = mViewItem.findViewById(R.id.main_refresh_layout);

            // 获取适配器
            mAdapter = new RecommendListAdapter();

            // 获取 Presenter
            mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
            mSubscriptionPresenter = SubscriptionPresenter.getInstance();

            mSubscriptionPresenter.registerViewController(this);
            mSubscriptionPresenter.querySubscriptionList();

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
            mRecyclerView.setAdapter(mAdapter);

            // 专辑列表被点击
            mAdapter.setOnItemClickListener((index, album) -> {
                mAlbumDetailPresenter.setAlbum(album);
                startActivity(new Intent(getContext(), AlbumDetailActivity.class));
                LogUtil.d(TAG, "onClick", "index = " + index + "album =" + album);
            });

            mRefreshLayout.setRefreshHeader(new ClassicsHeader(BaseApplication.getBaseCtx()));
            mRefreshLayout.setRefreshFooter(new ClassicsFooter(BaseApplication.getBaseCtx()));
        }

        return mViewItem;
    }

    @Override
    public void onAddSubscriptionResult(boolean isSuccess) {

    }

    @Override
    public void onDeleteSubscriptionResult(boolean isSuccess) {

    }

    @Override
    public void onSubscriptionListLoaded(List<Album> albumList) {
        if (mAdapter != null) {
            mAdapter.setData(albumList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptionPresenter != null) {
            mSubscriptionPresenter.unRegisterViewController(this);
        }
    }
}