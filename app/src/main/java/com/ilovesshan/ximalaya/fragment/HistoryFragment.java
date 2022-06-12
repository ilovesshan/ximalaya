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
import com.ilovesshan.ximalaya.interfaces.IHistoryViewController;
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
 * @description: 历史界面UI
 */

public class HistoryFragment extends BaseFragment implements ISubscriptionViewController, IHistoryViewController {

    private static final String TAG = "HistoryFragment";

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private AlbumListAdapter mAdapter;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private SubscriptionPresenter mSubscriptionPresenter;
    private HistoryPresenter mHistoryPresenter;
    private HistoryPresenter mMHistoryPresenter;

    @Override
    protected View getSubViewItem(LayoutInflater inflater, ViewGroup container) {
        View mViewItem = inflater.inflate(R.layout.fragment_history, container, false);
        if (mViewItem != null) {
            mRecyclerView = mViewItem.findViewById(R.id.rv_history_list);
            mRefreshLayout = mViewItem.findViewById(R.id.main_refresh_layout);

            // 获取适配器
            mAdapter = new AlbumListAdapter();

            // 获取 Presenter
            mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
            mSubscriptionPresenter = SubscriptionPresenter.getInstance();
            mMHistoryPresenter = HistoryPresenter.getInstance();

            mSubscriptionPresenter.registerViewController(this);
            mSubscriptionPresenter.querySubscriptionList();
            mMHistoryPresenter.registerViewController(this);
            mMHistoryPresenter.queryAlbumList();

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
                confirmDialog.setDialogTitle("确定移除历史记录吗?");
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
                        mMHistoryPresenter.deleteAlbumById((int) album.getId());
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

    }

    @Override
    public void onAddAlbumResult(boolean isSuccess) {

    }

    @Override
    public void onQueryAlbumListResult(List<Album> albumList, boolean isSuccess) {
        if (mAdapter != null) {
            mAdapter.setData(albumList);
        }
    }

    @Override
    public void onDeleteAlbumByIdResult(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.show("删除成功");
        }
    }

    @Override
    public void onDeleteAllAlbumResult(boolean isSuccess) {
        if (isSuccess) {
            ToastUtils.show("清空成功");
        }
    }
}
