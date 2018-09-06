package com.yyd.fjsd.filemanager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.FileListAdapter;
import com.yyd.fjsd.filemanager.asynctask.CopyFileTask;
import com.yyd.fjsd.filemanager.asynctask.DeleteFileTask;
import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.fragment.FileListFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
            case R.id.rename:
                if(MyApplication.getInstance().getSelectedList().size() > 1){
                    Toast.makeText(mContext, "不能同时对多个文件或文件夹进行重命名", Toast.LENGTH_SHORT).show();
                }else if(MyApplication.getInstance().getSelectedList().size() == 0){
                    Toast.makeText(mContext, "请选择要重命名的文件或文件夹", Toast.LENGTH_SHORT).show();
                }else{
                    View view = ((AppCompatActivity)mContext).getLayoutInflater().inflate(R.layout.rename_layout, null);
                    final EditText rename = view.findViewById(R.id.rename);
                    new AlertDialog.Builder(mContext)
                            .setTitle("重命名")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String name = rename.getText().toString();
                                    if(name != null && !name.equals("")){
                                        String oldName = MyApplication.getInstance().getSelectedList().values().iterator().next();
                                        String newName = new File(MyApplication.getInstance().getSelectedList().values().iterator().next()).getParent() + File.separator + name;
                                        if(FileUtil.renameFile(oldName, newName)){
                                            MyApplication.getInstance().runStatus = RunStatus.NORMAL_MODE;
                                            //清空选择列表
                                            MyApplication.getInstance().getSelectedList().clear();
                                            //刷新列表
                                            List<File> list = FileUtil.getFileList(MyApplication.getInstance().currPath);
                                            ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
                                            //FileListFragment
                                            FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, 0);
                                            FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                                            transaction.replace(R.id.primary_content, fileListFragment);
                                            transaction.commit();
                                        }else {
                                            Toast.makeText(mContext, "重命名失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(mContext, "请输入新的名称", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                }
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
