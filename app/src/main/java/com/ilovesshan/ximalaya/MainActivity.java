package com.ilovesshan.ximalaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 测试喜马拉雅SDK

        // 获取喜马拉雅内容分类
        Map<String, String> map1 = new HashMap<String, String>();
        CommonRequest.getCategories(map1, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList object) {
                Log.d(TAG, "onSuccess: " + object);
            }

            @Override
            public void onError(int code, String message) {
                Log.d(TAG, "onSuccess: " + "code= " + code + "  message=" + "message");
            }
        });


        // 获取直播省市列表
        Map<String, String> map2 = new HashMap<String, String>();
        CommonRequest.getProvinces(map2, new IDataCallBack<ProvinceList>() {

            @Override
            public void onSuccess(@Nullable ProvinceList provinceList) {
                Log.d(TAG, "onSuccess: " + provinceList);
            }

            @Override
            public void onError(int code, String message) {
                Log.d(TAG, "onSuccess: " + "code= " + code + "  message=" + "message");
            }
        });

    }
}