package com.ilovesshan.ximalaya.views;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.PlayerListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/6/1
 * @description:
 */
public class BottomSheet extends PopupWindow {

    private final View mViewItem;
    private PlayerListAdapter mPlayerListAdapter;
    private PlayerPresenter mPlayerPresenter;
    private RecyclerView mPlayerList;

    public BottomSheet() {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 获取布局文件
        mViewItem = LayoutInflater.from(BaseApplication.getBaseCtx()).inflate(R.layout.item_player_list, null);

        // 点击之外的区域可以关闭
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);

        // 添加到视图中
        setContentView(mViewItem);

        // 设置动画
        setAnimationStyle(R.style.bottom_sheet_animation);

        // 初始化view
        initView();

        // 绑定事件
        bindEvent();

    }

    /**
     * 初始化view 处理函数
     */
    private void initView() {
        mPlayerList = mViewItem.findViewById(R.id.rcv_player_list_center);

        mPlayerPresenter = PlayerPresenter.getInstance();

        // 设置布局管理器和适配器
        mPlayerList.setLayoutManager(new LinearLayoutManager(mViewItem.getContext()));
        mPlayerListAdapter = new PlayerListAdapter();
        mPlayerList.setAdapter(mPlayerListAdapter);
    }


    /**
     * 绑定事件 处理函数
     */
    private void bindEvent() {

        // 播放列表中item被点击 通知PlayerPresenter 切换歌曲
        mPlayerListAdapter.setOnPlayListTrackItemClickListener(playIndex -> {
            mPlayerPresenter.play(playIndex);
        });
    }

    /**
     * 将 数据传递给 PlayerListAdapter适配器
     *
     * @param tracks 节目列表
     */
    public void setData(List<Track> tracks) {
        if (mPlayerListAdapter != null) {
            mPlayerListAdapter.setData(tracks);
        }
    }

    public void setCurrentPlayIndex(int playIndex) {
        if (mPlayerListAdapter != null) {
            mPlayerList.smoothScrollToPosition(playIndex);
            mPlayerListAdapter.setCurrentPlayIndex(playIndex);
        }
    }
}
