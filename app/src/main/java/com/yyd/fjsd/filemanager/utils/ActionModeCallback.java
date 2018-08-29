package com.yyd.fjsd.filemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.FileListAdapter;

public class ActionModeCallback implements ActionMode.Callback {

    private Context mContext;
    private FileListAdapter mAdapter;

    public ActionModeCallback(Context mContext, FileListAdapter mAdapter) {
        this.mContext = mContext;
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        ((AppCompatActivity)mContext).getMenuInflater().inflate(R.menu.select_mode_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeAsUp:
                Log.v("yang", "homeAsUP");
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        MyApplication.getInstance().runStatus = RunStatus.NORMAL_MODE; //进入正常模式
        mAdapter.notifyDataSetChanged(); //刷新界面
    }
}
