package com.yyd.fjsd.filemanager.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.fragment.FileListFragment;
import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CopyFileTask extends AsyncTask<Iterator<String>, Integer, Boolean> {

    private Context mContext;

    public CopyFileTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Boolean doInBackground(Iterator<String>... iterators) {
        String toPath = MyApplication.getInstance().currPath;   //复制的目标路径
        Iterator<String> iterator = iterators[0];   //复制的文件列表
        boolean success = false; //拷贝的状态
        while (iterator.hasNext()){
            String fromPath = iterator.next();
            //Log.v("yang", "fromPath = " + fromPath + "toPath = " + toPath);
            if(new File(fromPath).isDirectory()){
                toPath = toPath + File.separator + new File(fromPath).getName();
            }
            if(FileUtil.copy(fromPath, toPath )){
                success = true;
            }else{
                success = false;
                return success;
            }
        }
        return success;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success){
            if(MyApplication.getInstance().runStatus == RunStatus.CUT_MODE){
                //剪切模式要删除掉文件
                new DeleteFileTask(mContext).execute(MyApplication.getInstance().getSelectedList().values().iterator());
            }else{
                Toast.makeText(mContext, "拷贝成功", Toast.LENGTH_SHORT).show();
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
            }
        }else{
            if(MyApplication.getInstance().runStatus == RunStatus.CUT_MODE) {
                Toast.makeText(mContext, "剪切失败", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "拷贝失败", Toast.LENGTH_SHORT).show();
            }
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
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

}
