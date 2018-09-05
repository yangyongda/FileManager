package com.yyd.fjsd.filemanager.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadFileListTask extends AsyncTask<String, Integer, Boolean> {
    private Context mContext;
    private Handler mHandler;
    ArrayList<MyFile> myFileList;

    public LoadFileListTask(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    @Override
    protected Boolean doInBackground(String... paras) {
        String path = paras[0];
        if(!new File(path).exists()){
            return false;
        }

        List<File> list = FileUtil.getFileList(path); //获取列表
        myFileList = FileUtil.FileToMyFile(list);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            Message message = new Message();
            message.what = 0x123;
            message.obj = myFileList;
            mHandler.sendMessageDelayed(message,200);
        }else{
            Toast.makeText(mContext, "路径不存在！", Toast.LENGTH_SHORT).show();
        }
    }
}
