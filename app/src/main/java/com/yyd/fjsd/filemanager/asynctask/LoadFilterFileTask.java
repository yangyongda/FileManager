package com.yyd.fjsd.filemanager.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.NoMediaFilter;
import com.yyd.fjsd.filemanager.utils.TypeConstant;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;


public class LoadFilterFileTask extends AsyncTask<Integer, Integer, Boolean> {

    private Context mContext;
    private Handler mHandler;
    private List<String> filterFiles;

    public LoadFilterFileTask(Context context, Handler handler){
        mContext = context;
        mHandler = handler;
        filterFiles = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Integer... type) {
        boolean status = false;
        switch (type[0]){
            case TypeConstant.PICTURE:
                status = getAllFilePath(FileUtil.getInterPath(), mContext.getResources().getStringArray(R.array.picture));
                break;
            case TypeConstant.MUSIC:
                break;
            case TypeConstant.VIDEO:
                break;
            case TypeConstant.DOCUMENT:
                break;
            case TypeConstant.APK:
                break;
            case TypeConstant.ZIP:
                break;
        }
        return status;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if(status){
            Message message = new Message();
            message.what = TypeConstant.PICTURE;
            message.obj = filterFiles;
            mHandler.sendMessage(message);
        }else{
            Toast.makeText(mContext, "路径不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getAllFilePath(String path, String[] suffixs){
        File root = new File(path);
        if(!root.exists()){
            return false;
        }
        boolean isShow = root.listFiles(new NoMediaFilter()).length > 0 ? false: true;
        if(isShow) {
            File[] files = root.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) { // 判断是文件还是文件夹
                        getAllFilePath(files[i].getAbsolutePath(), suffixs); //递归
                    } else {
                        String fileName = files[i].getName(); //获取文件名
                        for (String suffix : suffixs) {  //遍历所有匹配的后缀
                            if (fileName.endsWith(suffix)) {
                                filterFiles.add(files[i].getPath());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
