package com.yyd.fjsd.filemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String FILE_NAME = "user_settings";
    private final String  FIRST_START = "firstStart";
    private static SharedPreferencesHelper mSharedPreferencesHelper;
    private SharedPreferences mSharedPreferences;


    public SharedPreferencesHelper(Context context){
        mSharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }

    public static SharedPreferencesHelper getInstance(Context context){
        if(mSharedPreferencesHelper == null){
            mSharedPreferencesHelper = new SharedPreferencesHelper(context);
        }
        return mSharedPreferencesHelper;
    }

    public boolean getFirstStart(){
        if(mSharedPreferences.getBoolean(FIRST_START, true)){
            //第一次启动
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(FIRST_START, false);
            editor.commit();
            return true;
        }else{
            return false;
        }
    }
}
