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
import com.yyd.fjsd.filemanager.asynctask.CopyFileTask;
import com.yyd.fjsd.filemanager.asynctask.DeleteFileTask;

import java.util.Collection;
import java.util.Iterator;

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
            case R.id.copy:
                mode.finish();  //关闭ActionMode
                MyApplication.getInstance().runStatus = RunStatus.COPY_MODE;
                mAdapter.notifyDataSetChanged(); //刷新界面
                break;
            case R.id.cut:
                mode.finish();
                MyApplication.getInstance().runStatus = RunStatus.CUT_MODE;
                mAdapter.notifyDataSetChanged(); //刷新界面
                break;
            case R.id.delete:
                mode.finish();  //关闭ActionMode
                new DeleteFileTask(mContext).execute(MyApplication.getInstance().getSelectedList().values().iterator());
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //Log.v("yang", "DestroyActionMode");
        MyApplication.getInstance().runStatus = RunStatus.NORMAL_MODE; //进入正常模式
        mAdapter.notifyDataSetChanged(); //刷新界面
    }

}
