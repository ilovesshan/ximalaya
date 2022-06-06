package com.ilovesshan.ximalaya;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ilovesshan.ximalaya.adapter.TabIndicatorAdapter;
import com.ilovesshan.ximalaya.adapter.TabViewPagerAdapter;
import com.ilovesshan.ximalaya.config.Constants;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.Arrays;

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

        // indicator宽度自适应
        commonNavigator.setAdjustMode(true);
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