package com.ilovesshan.ximalaya.utils;

import androidx.fragment.app.Fragment;

import com.ilovesshan.ximalaya.fragment.HistoryFragment;
import com.ilovesshan.ximalaya.fragment.RecommendFragment;
import com.ilovesshan.ximalaya.fragment.SubscriptionFragment;

import java.net.CookieHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/26
 * @description:
 */
public class FragmentCreator {
    public static final int FRAGMENT_SIZE = 3;

    public static final int INDEX_RECOMMEND = 0;
    public static final int INDEX_SUBSCRIPTION = 1;
    public static final int INDEX_HISTORY = 2;

    public static Map<Integer, Fragment> sFragmentHashMap = new HashMap<>();


    public static Fragment getFragment(int index) {

        Fragment fragment = sFragmentHashMap.get(index);

        if (fragment != null) {
            return fragment;
        }

        switch (index) {
            case INDEX_RECOMMEND:
                fragment = new RecommendFragment();
                break;

            case INDEX_SUBSCRIPTION:
                fragment = new SubscriptionFragment();
                break;

            case INDEX_HISTORY:
                fragment = new HistoryFragment();
                break;
        }

        sFragmentHashMap.put(index, fragment);

        return fragment;
    }
}
