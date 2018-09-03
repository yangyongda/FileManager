package com.yyd.fjsd.filemanager.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;

import java.io.File;
import java.util.Iterator;

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
            Toast.makeText(mContext, "拷贝成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "拷贝失败", Toast.LENGTH_SHORT).show();
        }
        MyApplication.getInstance().runStatus = RunStatus.NORMAL_MODE;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

}
