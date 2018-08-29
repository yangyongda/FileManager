package com.yyd.fjsd.filemanager.utils;

import com.yyd.fjsd.filemanager.bean.MyFile;

import java.util.Comparator;

public class MyFileComparator implements Comparator<MyFile> {
    @Override
    public int compare(MyFile myFile1, MyFile myFile2) {
        //先比较是否是目录
        int isDirectory = myFile1.getIsDirectory() - myFile2.getIsDirectory(); //文件夹优先
        if(isDirectory == 0){ //再比较文件名
            return myFile1.getFileName().compareTo(myFile2.getFileName());
        }
        return - isDirectory;
    }
}
