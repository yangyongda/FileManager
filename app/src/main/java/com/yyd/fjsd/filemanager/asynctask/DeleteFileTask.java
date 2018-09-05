package com.yyd.fjsd.filemanager.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

public class DeleteFileTask extends AsyncTask<Iterator<String>, Integer, Boolean> {

    private Context mContext;

    public DeleteFileTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Boolean doInBackground(Iterator<String>... iterators) {

        Iterator<String> iterator = iterators[0];   //删除的文件列表
        ArrayList<String> fileNames = new ArrayList<>();
        boolean success = false; //删除的状态
        /*
        *遍历保存到ArrayList中，不能直接使用迭代器进行删除，会导致 java.util.ConcurrentModificationException
        * 因为迭代器不允许在使用过程中修改集合，而删除文件就是在修改集合内容。
        * */
        while (iterator.hasNext()){
            String filePath = iterator.next();
            fileNames.add(filePath);
        }

        for(int i = 0; i < fileNames.size(); i++){
            if(FileUtil.deleteFiles(fileNames.get(i))){
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
                Toast.makeText(mContext, "剪切成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(MyApplication.getInstance().runStatus == RunStatus.CUT_MODE){
                Toast.makeText(mContext, "剪切失败", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }
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

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

}
