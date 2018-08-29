package com.yyd.fjsd.filemanager;

import android.app.Application;

import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;
import com.yyd.fjsd.filemanager.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication instance;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    public List<String> prePath;  //保存路径
    public int runStatus;   //运行状态

    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mSharedPreferencesHelper = SharedPreferencesHelper.getInstance(instance);
        prePath = new ArrayList<>();
        runStatus = RunStatus.NORMAL_MODE;
    }

    public SharedPreferencesHelper getSharedPreferencesHelper() {
        return mSharedPreferencesHelper;
    }

}
