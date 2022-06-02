package com.ilovesshan.ximalaya.views;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.adapter.PlayerListAdapter;
import com.ilovesshan.ximalaya.base.BaseApplication;
import com.ilovesshan.ximalaya.presenter.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

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
    private ImageView mMIvPlayerListPlayModeIcon;
    private TextView mMIvPlayerListPlayModeText;
    private ImageView mMIvPlayerListPlayOrderIcon;
    private TextView mMtvPlayerListPlayOrderText;
    private LinearLayout mMLlPlayerListBottom;
    private RelativeLayout mMRlPlayerListTop;


    private OnPlayListPlayModeChangeListener mOnPlayListPlayModeChangeListener;
    private OnPlayListItemClickListener mOnPlayListItemClickListener;

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
        mMRlPlayerListTop = mViewItem.findViewById(R.id.rl_player_list_top);
        mMIvPlayerListPlayModeIcon = mViewItem.findViewById(R.id.iv_player_list_play_mode_icon);
        mMIvPlayerListPlayModeText = mViewItem.findViewById(R.id.iv_player_list_play_mode_text);
        mMIvPlayerListPlayOrderIcon = mViewItem.findViewById(R.id.iv_player_list_play_order_icon);
        mMtvPlayerListPlayOrderText = mViewItem.findViewById(R.id.tv_player_list_play_order_text);
        mMLlPlayerListBottom = mViewItem.findViewById(R.id.ll_player_list_bottom);


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
        mMLlPlayerListBottom.setOnClickListener(v -> {
            // 关闭 BottomSheet
            this.dismiss();
        });

        // 播放列表中item被点击 通知PPlayerActivity 切换歌曲
        mPlayerListAdapter.setOnPlayListTrackItemClickListener(playIndex -> {
            mOnPlayListItemClickListener.onClick(playIndex);
        });

        //TODO 播放列表底部弹窗中 下载按钮被点击逻辑暂未实现
        mPlayerListAdapter.setOnPlayListTrackDownloadClickListener((track, playIndex) -> {

        });


        // 播放列表播放模式切换 通知PlayerActivity 切换播放模式
        mMRlPlayerListTop.setOnClickListener(v -> {
            if (this.mOnPlayListPlayModeChangeListener != null) {
                mOnPlayListPlayModeChangeListener.onChange();
            }
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

    public void setPlayMode(XmPlayListControl.PlayMode playMode) {
        if (playMode == XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP) {
            mMIvPlayerListPlayModeText.setText("单曲循环播放");
            mMIvPlayerListPlayModeIcon.setImageResource(R.drawable.player_mode_danqu);
        } else if (playMode == XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP) {
            mMIvPlayerListPlayModeText.setText("列表循环播放");
            mMIvPlayerListPlayModeIcon.setImageResource(R.drawable.player_mode_shunxu);
        } else if (playMode == XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM) {
            mMIvPlayerListPlayModeText.setText("随机播放");
            mMIvPlayerListPlayModeIcon.setImageResource(R.drawable.player_mode_suiji);
        }
    }


    public void setOnPlayListPlayModeChangeListener(OnPlayListPlayModeChangeListener listener) {
        this.mOnPlayListPlayModeChangeListener = listener;
    }


    public interface OnPlayListPlayModeChangeListener {
        public void onChange();
    }


    public void setOnPlayListItemClickListener(OnPlayListItemClickListener listener) {
        this.mOnPlayListItemClickListener = listener;
    }


    public interface OnPlayListItemClickListener {
        public void onClick(int playIndex);
    }
}
