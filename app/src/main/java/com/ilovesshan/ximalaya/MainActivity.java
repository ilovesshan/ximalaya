package com.ilovesshan.ximalaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ilovesshan.ximalaya.adapter.TabIndicatorAdapter;
import com.ilovesshan.ximalaya.adapter.TabViewPagerAdapter;
import com.ilovesshan.ximalaya.config.Constants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MagicIndicator mTabIndicator;
    private ViewPager mTabViewPager;

    private String[] mTabIndicators = Constants.TAB_INDICATORS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化view和绑定事件
        initViewAndBindEvent();


    }

    /**
     * 初始化view和绑定事件 处理函数
     */
    private void initViewAndBindEvent() {
        mTabIndicator = findViewById(R.id.tab_indicator);
        mTabViewPager = findViewById(R.id.tab_view_pager);

        // 创建公共导航器和设置适配器
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setBackgroundColor(getResources().getColor(R.color.app_primary));
        TabIndicatorAdapter tabIndicatorAdapter = new TabIndicatorAdapter(Arrays.asList(mTabIndicators));
        commonNavigator.setAdapter(tabIndicatorAdapter);
        // TabIndicator 被点击的回调
        tabIndicatorAdapter.setOnTabClickListener((index -> mTabViewPager.setCurrentItem(index)));


        // 给viewPager设置适配器
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        mTabViewPager.setAdapter(tabViewPagerAdapter);


        // indicator设置导航器
        mTabIndicator.setNavigator(commonNavigator);
        // 绑定indicator和viewPager
        ViewPagerHelper.bind(mTabIndicator, mTabViewPager);
    }
}