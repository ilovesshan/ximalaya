package com.ilovesshan.ximalaya.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.ilovesshan.ximalaya.AlbumDetailActivity;
import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.AlbumListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.base.BaseFragment;
import com.ilovesshan.ximalaya.interfaces.ISubscriptionViewController;
import com.ilovesshan.ximalaya.presenter.AlbumDetailPresenter;
import com.ilovesshan.ximalaya.presenter.HistoryPresenter;
import com.ilovesshan.ximalaya.presenter.SubscriptionPresenter;
import com.ilovesshan.ximalaya.utils.LogUtil;
import com.ilovesshan.ximalaya.views.ConfirmDialog;
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
    private AlbumListAdapter mAdapter;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private SmartRefreshLayout mRefreshLayout;
    private SubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mRecyclerView;
    private HistoryPresenter mHistoryPresenter;

    @Override
    protected View getSubViewItem(LayoutInflater inflater, ViewGroup container) {
        mViewItem = inflater.inflate(R.layout.fragment_subscription, container, false);
        if (mViewItem != null) {
            mRecyclerView = mViewItem.findViewById(R.id.rv_recommend_list);
            mRefreshLayout = mViewItem.findViewById(R.id.main_refresh_layout);

            // 获取适配器
            mAdapter = new AlbumListAdapter();

            // 获取 Presenter
            mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
            mSubscriptionPresenter = SubscriptionPresenter.getInstance();

            mSubscriptionPresenter.registerViewController(this);
            mSubscriptionPresenter.querySubscriptionList();

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
            mRecyclerView.setAdapter(mAdapter);

            // 专辑列表被点击
            mAdapter.setOnItemClickListener((index, album) -> {
                // 记录一次历史记录
                mHistoryPresenter = HistoryPresenter.getInstance();
                mHistoryPresenter.addAlbum(album);

                mAlbumDetailPresenter.setAlbum(album);
                startActivity(new Intent(getContext(), AlbumDetailActivity.class));
                LogUtil.d(TAG, "onClick", "index = " + index + "album =" + album);
            });

            // 专辑列表被长按
            mAdapter.setOnItemLongClickListener((index, album) -> {
                ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
                confirmDialog.setDialogTitle("确定取消订阅吗?");
                confirmDialog.setDialogSureText("删除");
                confirmDialog.setDialogCancelText("再想想");

                confirmDialog.setOnDialogActionClickListener(new ConfirmDialog.OnDialogActionClickListener() {
                    // dialog 取消按钮 被点击
                    @Override
                    public void onCancelClick() {
                        confirmDialog.dismiss();
                    }

                    // dialog 确定按钮 被点击
                    @Override
                    public void onSureClick() {
                        mSubscriptionPresenter.deleteSubscription((int) album.getId());
                        ToastUtils.show("取消成功");
                        confirmDialog.dismiss();
                    }
                });

                // 显示 dialog
                confirmDialog.show();
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