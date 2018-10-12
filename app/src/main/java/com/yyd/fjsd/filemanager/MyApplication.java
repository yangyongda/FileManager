package com.yyd.fjsd.filemanager;

import android.app.Application;

import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.FragmentList;
import com.yyd.fjsd.filemanager.utils.RequestPermissionUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;
import com.yyd.fjsd.filemanager.utils.SharedPreferencesHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

public class MyApplication extends Application {

    private static MyApplication instance;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    public List<String> prePath;  //保存父路径
    public List<Integer> prePosition; //保存父路径位置，返回时回到对应位置
    public String currPath;       //保存当前路径
    public int runStatus;   //运行状态
    public int fragment_page; //fragment页面
    private HashMap<Integer, String> mSelectedList; //被选中的item



    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mSharedPreferencesHelper = SharedPreferencesHelper.getInstance(instance);
        prePath = new ArrayList<>();
        prePosition = new ArrayList<>();
        runStatus = RunStatus.NORMAL_MODE;
        fragment_page = FragmentList.MAIN_FRAGMENT;
        mSelectedList = new HashMap<>();
        RequestPermissionUtil.requestPermission(this);

        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }

    public SharedPreferencesHelper getSharedPreferencesHelper() {
        return mSharedPreferencesHelper;
    }

    public HashMap<Integer, String> getSelectedList() {
        return mSelectedList;
    }
}
