package com.ilovesshan.ximalaya.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ilovesshan.ximalaya.R;
import com.ilovesshan.ximalaya.base.BaseFragment;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description: 历史
 */

public class HistoryFragment extends BaseFragment {

    @Override
    protected View getSubViewItem(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}
