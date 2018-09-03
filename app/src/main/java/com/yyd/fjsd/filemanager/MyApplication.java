package com.yyd.fjsd.filemanager;

import android.app.Application;

import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;
import com.yyd.fjsd.filemanager.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication instance;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    public List<String> prePath;  //保存父路径
    public String currPath;       //保存当前路径
    public int runStatus;   //运行状态
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
        runStatus = RunStatus.NORMAL_MODE;
        mSelectedList = new HashMap<>();
    }

    public SharedPreferencesHelper getSharedPreferencesHelper() {
        return mSharedPreferencesHelper;
    }

    public HashMap<Integer, String> getSelectedList() {
        return mSelectedList;
    }
}
