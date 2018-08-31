package com.yyd.fjsd.filemanager.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.bean.MyFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    public static String getInterPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    public static List<File> getFileList(String path) {
        List<File> list = new ArrayList<>();
        File file = new File(path);
        File[] allFile = file.listFiles();
        for (int i = 0; i < allFile.length; i++) {
            list.add(allFile[i]);
        }

        return list;
    }

    public static ArrayList<MyFile> FileToMyFile(List<File> files) {
        ArrayList<MyFile> myFiles = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String fileName = file.getName();
            String path = file.getAbsolutePath();

            MyFile myFile;
            if (file.isDirectory()) {
                myFile = new MyFile(R.drawable.folder, fileName, path, 0, 1);
            } else {
                long size = file.length();
                myFile = new MyFile(R.drawable.files, fileName, path, size, 0);
            }
            myFiles.add(myFile);
            Collections.sort(myFiles, new MyFileComparator());
        }

        return myFiles;
    }

    public static String sizeTransform(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static void openFile(Context context, String file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(file)), MapTable.getMIMEType(file));
            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 拷贝成功返回true
     * */
    public static boolean copyFile(String fromFile, String toFile){
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如果不存在则 return出去
        if (!root.exists()) {
            return false;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //目录不存在则创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) { //如果当前项为子目录 进行递归
                copyFile(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else {//如果当前项为文件则进行文件拷贝
                try {
                    InputStream fisfrom = new FileInputStream(currentFiles[i].getPath());
                    OutputStream fosto = new FileOutputStream(toFile + currentFiles[i].getName());
                    byte data[] = new byte[1024];
                    int num;
                    while ((num = fisfrom.read(data)) > 0) {
                        fosto.write(data, 0, num);
                    }
                    fosto.flush();
                    fisfrom.close();
                    fosto.close();
                }catch (IOException e){
                    //e.printStackTrace();
                    return false;
                }
            }
        }
        return true;

    }
}

